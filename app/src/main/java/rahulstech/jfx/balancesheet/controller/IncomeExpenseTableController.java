package rahulstech.jfx.balancesheet.controller;

import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.util.Subscription;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpense;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpenseType;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.QueryParameter;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.service.ServiceResult;
import rahulstech.jfx.balancesheet.core.util.Log;
import rahulstech.jfx.balancesheet.core.util.TextUtil;
import rahulstech.jfx.balancesheet.dialog.InputIncomeExpenseDialog;
import rahulstech.jfx.balancesheet.frontend.control.ButtonRole;
import rahulstech.jfx.balancesheet.frontend.control.Chip;
import rahulstech.jfx.balancesheet.frontend.control.MaterialPagination;
import rahulstech.jfx.balancesheet.frontend.stage.AlertDialog;
import rahulstech.jfx.balancesheet.frontend.stage.Toast;
import rahulstech.jfx.balancesheet.frontend.util.TableViewUtil;
import rahulstech.jfx.balancesheet.service.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static rahulstech.jfx.balancesheet.balancesheetdb.repository.IncomeExpenseRepo.*;

public class IncomeExpenseTableController extends BaseController {

    private static final String TAG = IncomeExpenseTableController.class.getSimpleName();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    private static final ObservableList<Long> PAGINATION_ITEMS = FXCollections.observableArrayList(50L, 100L, 150L);

    @FXML
    private ToggleButton typeIncome;

    @FXML
    private ToggleButton typeExpense;

    @FXML
    private FlowPane sectionSelectedFilterChips;

    @FXML
    private ToggleButton toggleSectionFilter;

    @FXML
    private TitledPane sectionFilter;

    @FXML
    private TableView<IncomeExpense> tblInEx;

    @FXML
    private TableColumn<IncomeExpense, LocalDate> colDate;

    @FXML
    private TableColumn<IncomeExpense, BigDecimal> colAmount;

    @FXML
    private TableColumn<IncomeExpense, String> colType;

    @FXML
    private TableColumn<IncomeExpense, String> colNote;

    @FXML
    private TextField search; // TODO: implement search

    @FXML
    private DatePicker filterDateStart;

    @FXML
    private DatePicker filterDateEnd;

    @FXML
    private Button btnDelete;

    @FXML
    private MaterialPagination pagination;

    private PageSource<IncomeExpense> pageSource;

    private Subscription recentPageSubscription;

    private boolean maxPageNumberChanged = false;

    public IncomeExpenseTableController(Context parent) {
        super(parent);
    }

    @Override
    public void onLifecycleCreate() {
        super.onLifecycleCreate();

        TableViewUtil.addRowSelectionColumn(tblInEx);

        colDate.setCellValueFactory(cell -> new ObservableValueBase<>() {
            @Override
            public LocalDate getValue() {
                return cell.getValue().getDate();
            }
        });

        colDate.setCellFactory(column -> new TableCell<>(){
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    return;
                }
                String text = item.format(FORMATTER);
                setText(text);
            }
        });

        colAmount.setCellValueFactory(cell -> new ObservableValueBase<>() {
            @Override
            public BigDecimal getValue() {
                return cell.getValue().getAmount();
            }
        });

        colAmount.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    return;
                }
                String text = TextUtil.formatCurrency(item,null);
                setText(text);
            }
        });

        colType.setCellValueFactory(cell -> new ObservableValueBase<>() {
            @Override
            public String getValue() {
                return cell.getValue().getType().name();
            }
        });

        colNote.setCellValueFactory(cell -> new ObservableValueBase<>() {
            @Override
            public String getValue() {
                return cell.getValue().getNote();
            }
        });

        tblInEx.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblInEx.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super IncomeExpense>) change -> onChangeTableSelection());

        createNewPageSource();

        pagination.maxEntriesProperty().subscribe(maxEntries -> {
            maxPageNumberChanged = true;
            pageSource.loadPage(maxEntries.longValue(),0);
        });
        pagination.setMaxEntriesItems(PAGINATION_ITEMS);
        pagination.setMaxEntries(50);
        pagination.setCurrentPageNumber(1);

        pagination.currentPageNumberProperty().subscribe(newPageNumber -> {
            long pageNumber = newPageNumber.longValue();
            long limit = pagination.getMaxEntries();
            long offset = (pageNumber - 1) * limit;
            pageSource.loadPage(limit,offset);
        });

        pageSource.loadPage(pagination.getMaxEntries(), 0);

        sectionFilter.expandedProperty().bind(toggleSectionFilter.selectedProperty());

        search.textProperty().addListener((ov, old, phrase) -> filterNote(pageSource.getRecentPage().getData(), phrase));

        // chip

//        filterDateStart.valueProperty().addListener((v, o, n) -> {
//            if (null == n) {
//                return;
//            }
//            Chip chip = new Chip();
//            chip.setChipType(Chip.ChipType.INPUT);
//            chip.setText(n.toString());
//            chip.setLeadingIcon(new FontIcon(MaterialDesign.MDI_CALENDAR));
//            chip.setTrailingIcon(new FontIcon(MaterialDesign.MDI_CLOSE_CIRCLE));
//            chip.setTrailingIconRole(ButtonRole.CUSTOM);
//            chip.setOnTrailingIconAction(event -> {
//                chip.hide();
//                filterDateStart.setValue(null);
//            });
//            sectionSelectedFilterChips.getChildren().add(chip);
//        });
    }

    private QueryParameter createQueryParameter() {
        LocalDate dateStart = filterDateStart.getValue();
        LocalDate dateEnd = filterDateEnd.getValue();

        List<IncomeExpenseType> typesList = new ArrayList<>();
        if (typeIncome.isSelected()) {
            typesList.add(IncomeExpenseType.INCOME);
        }
        if (typeExpense.isSelected()) {
            typesList.add(IncomeExpenseType.EXPENSE);
        }
        IncomeExpenseType[] types = typesList.isEmpty() ? null : typesList.toArray(new IncomeExpenseType[0]);

        QueryParameter qParam = new QueryParameter();
        qParam.put(KEY_DATE_START,dateStart);
        qParam.put(KEY_DATE_END,dateEnd);
        qParam.put(KEY_TYPE,types);
        qParam.orderBy(COLUMN_DATE, QueryParameter.SortOrder.DESCENDING);

        return qParam;
    }

    private void setMaxPageNumber(long maxEntries, long totalEntries) {
        if (maxEntries < 1 || totalEntries < 1) {
            pagination.setMaxPageNumber(0);
            return;
        }
        long maxPageNumber = totalEntries / maxEntries;
        if (0 != totalEntries % maxEntries) {
            maxPageNumber++;
        }
        pagination.setMaxPageNumber(maxPageNumber);
        maxPageNumberChanged = false;
    }

    private void createNewPageSource() {
        if (null != pageSource) {
            recentPageSubscription.unsubscribe();
            recentPageSubscription = null;
            pageSource = null;
        }
        pageSource = getIncomeExpenseCRUDService().newPageSource(createQueryParameter());
        maxPageNumberChanged = true;
        recentPageSubscription = pageSource.recentPageProperty().subscribe(newPage -> {
            if (null == newPage || null == newPage.getData()) {
                tblInEx.setItems(FXCollections.emptyObservableList());
                pagination.setProgressText(null);
                return;
            }
            tblInEx.setItems(FXCollections.observableList(newPage.getData()));
            long start = newPage.getOffset() + 1;
            long end = start + newPage.getDataSize() - 1;
            long total = newPage.getTotalEntries();
            pagination.setProgressText(String.format("%d-%d of %d",start,end,total));
            if (maxPageNumberChanged) {
                setMaxPageNumber(pagination.getMaxEntries(),total);
            }
        });
    }

    private void onChangeTableSelection() {
        boolean hasSelection = !tblInEx.getSelectionModel().isEmpty();
        btnDelete.setVisible(hasSelection);
    }

    /*********************************************
     *              Event Handler                *
     *********************************************/

    @FXML
    private void onClickAddNew() {
        InputIncomeExpenseDialog dialog = new InputIncomeExpenseDialog(this);
        dialog.setTitle("Add New");
        dialog.showDialog();
    }

    @FXML
    private void onClickRefresh() {
        PageData<IncomeExpense> page = pageSource.getRecentPage();
        createNewPageSource();
        if (null != page) {
            pageSource.loadPage(page.getLimit(), page.getOffset());
        }
        else {
            pageSource.loadPage(pagination.getMaxEntries(), 0);
        }
    }

    @FXML
    private void onClickFilter() {
        createNewPageSource();
        pageSource.loadPage(pagination.getMaxEntries(), 0);
    }

    @FXML
    private void onClickDelete() {
        final List<IncomeExpense> items = new ArrayList<>(tblInEx.getSelectionModel().getSelectedItems());
        int count = items.size();

        AlertDialog alert = new AlertDialog.Builder(this)
                .setHideOnEscape(false)
                .setContentMessage(String.format("%d item(s) will be deleted permanently. Are you sure to proceed?",count))
                .setPositiveButton("No",null)
                .setNegativeButton("Yes",e -> {
                    BaseBalanceSheetDBCRUDService.CrudServiceParameter param = IncomeExpenseCRUDService.deleteMultipleServiceParameter(items);
                    getIncomeExpenseCRUDService().start(param);
                })
                .build();
        alert.showDialog();
    }

    /*********************************************
     *                  Service                  *
     *********************************************/

    // crud service

    private IncomeExpenseCRUDService service;

    private IncomeExpenseCRUDService getIncomeExpenseCRUDService() {
        if (null == service) {
            service = new IncomeExpenseCRUDService(this);
            service.valueProperty().addListener((ov,oldResult,newResult) -> onServiceResult(newResult));
        }
        return service;
    }

    private void onServiceResult(ServiceResult result) {
        if (null == result) {
            return;
        }
        int taskId = result.getTaskId();
        boolean successful = result.isSuccessful();
        if (taskId == IncomeExpenseCRUDService.TASK_DELETE_MULTIPLE) {
            if (successful) {
                int count = (int) result.getValue();
                Toast.showShort(this,String.format("%d item(s) deleted",count));
            }
            else {
                Log.error(TAG,"error in delete multiple income_expense", result.getError());
                Toast.make(this,"No items deleted",Toast.AUTO_HIDE_DELAY_SHORT).showToast();
            }
        }
    }

    // filter service

    private FilterService<IncomeExpense> filterService;

    private FilterService<IncomeExpense> getFilterService() {
        if (null == filterService) {
            filterService = new FilterService<>(this);
            filterService.valueProperty().addListener((ov, oldResult, newResult) -> onFilterResult(newResult));
        }
        return filterService;
    }

    private void filterNote(List<IncomeExpense> items, String phrase) {
        if (TextUtil.isEmpty(phrase)) {
            tblInEx.setItems(FXCollections.observableList(items));
            return;
        }

        FilterService.FilterServiceParameter<IncomeExpense> parameter
                = new FilterService.FilterServiceParameter<>(items, item -> TextUtil.containsIgnoreCase(phrase,item.getNote()));
        getFilterService().start(parameter);
    }

    @SuppressWarnings("unchecked")
    private void onFilterResult(ServiceResult result) {
        if (null == result) {
            return;
        }
        if (!result.isSuccessful()) {
            Log.error(TAG, "filter by note error",result.getError());
            return;
        }
        List<IncomeExpense> filtered = (List<IncomeExpense>) result.getValue();
        tblInEx.setItems(FXCollections.observableList(filtered));
    }
}
