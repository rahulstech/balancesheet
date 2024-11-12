package rahulstech.jfx.balancesheet.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import net.synedra.validatorfx.Validator;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpense;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpenseType;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.util.Log;
import rahulstech.jfx.balancesheet.frontend.control.DescriptionPane;
import rahulstech.jfx.balancesheet.frontend.stage.Toast;
import rahulstech.jfx.balancesheet.frontend.util.ToggleUtil;
import rahulstech.jfx.balancesheet.frontend.validator.*;
import rahulstech.jfx.balancesheet.service.BaseBalanceSheetDBCRUDService;
import rahulstech.jfx.balancesheet.service.IncomeExpenseCRUDService;
import rahulstech.jfx.balancesheet.core.service.ServiceResult;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InputIncomeExpenseController extends BaseController {

    private static final String TAG = InputIncomeExpenseController.class.getSimpleName();

    @FXML
    private DescriptionPane dateDescription;

    @FXML
    private DescriptionPane typeDescription;

    @FXML
    private DescriptionPane amountDescription;

    @FXML
    private DatePicker inputDate;

    @FXML
    private TextField inputAmount;

    @FXML
    private TextArea inputNote;

    @FXML
    private ToggleButton typeIncome;

    @FXML
    private ToggleButton typeExpense;

    private ToggleGroup typeGroup;

    private Validator validator;

    private final StringConverter<BigDecimal> STRING_TO_BIG_DECIMAL_CONVERTER = new StringConverter<>() {

        @Override
        public String toString(BigDecimal object) {
            if (null == object) {
                return null;
            }
            return object.toPlainString();
        }

        @Override
        public BigDecimal fromString(String string) {
            if (null == string) {
                return null;
            }
            return new BigDecimal(string);
        }
    };

    public InputIncomeExpenseController(Context parent) {
        super(parent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputAmount.setTextFormatter(new TextFormatter<>(STRING_TO_BIG_DECIMAL_CONVERTER));
        typeGroup = ToggleUtil.exactlyOne(null,typeIncome,typeExpense);

        validator = new Validator();
        validator.createCheck()
                .dependsOn("amount", inputAmount.getTextFormatter().valueProperty())
                .withMethod(context -> {
                    if (null == context.get("amount")) {
                        context.error("amount not set");
                    }}
                )
                .decorates(amountDescription).decoratingWith(DescriptionPaneDecoration::new);
        validator.createCheck().dependsOn("date", inputDate.valueProperty())
                .withMethod(context -> {
                    if (null == context.get("date")) {
                        context.error("date not set");
                    }
                })
                .decorates(dateDescription).decoratingWith(DescriptionPaneDecoration::new);
        validator.createCheck().dependsOn("type",typeGroup.selectedToggleProperty())
                .withMethod(context -> {
                    if (null == context.get("type")) {
                        context.error("type not set");
                    }
                })
                .decorates(typeDescription).decoratingWith(DescriptionPaneDecoration::new);
    }

    private IncomeExpenseType getSelectedType() {
        Toggle toggle = typeGroup.getSelectedToggle();
        if (toggle == typeIncome) {
            return IncomeExpenseType.INCOME;
        }
        if (toggle == typeExpense) {
            return IncomeExpenseType.EXPENSE;
        }
        return null;
    }

    public void save() {
        LocalDate date = inputDate.getValue();
        BigDecimal amount = (BigDecimal) inputAmount.getTextFormatter().getValue();
        String note = inputNote.getText();
        IncomeExpenseType type = getSelectedType();

        if (!validator.validate()) {
            Log.info(TAG, "one or more inputs are not valid");
            return;
        }

        IncomeExpense value = new IncomeExpense();
        value.setDate(date);
        value.setAmount(amount);
        value.setNote(note);
        value.setType(type);

        BaseBalanceSheetDBCRUDService.CrudServiceParameter parameter = IncomeExpenseCRUDService.createSingleServiceParameter(value);
        getIncomeExpenseCRUDService().start(parameter);
    }

    public void clear() {
        inputDate.setValue(null);
        inputAmount.setText(null);
        inputNote.setText(null);
        ToggleUtil.selectNone(typeGroup);
        validator.immediateClear();
    }

    // IncomeExpenseCRUDService

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
        if (result.isSuccessful()) {
            IncomeExpense value = (IncomeExpense) result.getValue();
            Toast.make(this,value.getType().name()+" saved successfully",Toast.AUTO_HIDE_DELAY_LONG).showToast();
        } else {
            Toast.make(this,"not saved",Toast.AUTO_HIDE_DELAY_LONG).showToast();
        }
    }
}
