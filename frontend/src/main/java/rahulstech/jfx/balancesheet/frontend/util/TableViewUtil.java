package rahulstech.jfx.balancesheet.frontend.util;

import javafx.collections.ListChangeListener;
import javafx.scene.control.*;

public class TableViewUtil {

    public static <S> TableColumn<S,Void> addRowSelectionColumn(TableView<S> tableview) {
        TableColumn<S,Void> colRowSelection = new TableColumn<>();
        colRowSelection.getStyleClass().add("rst-row-selection-column");
        colRowSelection.setResizable(false);
        colRowSelection.setEditable(false);
        colRowSelection.setReorderable(false);
        colRowSelection.setSortable(false);
        colRowSelection.setCellFactory(cell -> new RowSelectionTableColumnCell<>());

        CheckBox checkbox = new CheckBox();
        checkbox.setOnAction(e -> {
            boolean selected = checkbox.isSelected();
            if (selected) {
                tableview.getSelectionModel().selectAll();
            }
            else {
                tableview.getSelectionModel().clearSelection();
            }
        });

        checkbox.visibleProperty().bind(tableview.getSelectionModel().selectionModeProperty().map(mode -> mode ==SelectionMode.MULTIPLE));

        checkbox.setSelected(isAllSelected(tableview));

        tableview.getSelectionModel().getSelectedIndices()
                .addListener((ListChangeListener<? super Integer>) change -> checkbox.setSelected(isAllSelected(tableview)));

        colRowSelection.setGraphic(checkbox);

        tableview.getColumns().add(0, colRowSelection);

        return colRowSelection;
    }

    private static <S> boolean isAllSelected(TableView<S> tableview) {
        int itemCount = tableview.getItems().size();
        int selectionCount = tableview.getSelectionModel().getSelectedIndices().size();
        return itemCount > 0 && itemCount == selectionCount;
    }

    private static class RowSelectionTableColumnCell<S> extends TableCell<S,Void> {

        private final CheckBox checkbox;

        public RowSelectionTableColumnCell() {
            super();
            checkbox = new CheckBox();
            checkbox.setOnAction(e -> onClickCheckBox());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            final ListChangeListener<? extends Integer> selectionListener = change -> updateCheckBoxSelection();

            tableViewProperty().addListener((ov, oldTableView, newTableView) -> {
                if (null != oldTableView) {
                    oldTableView.getSelectionModel().getSelectedIndices().removeListener((ListChangeListener<? super Integer>) selectionListener);
                }
                if (null != newTableView) {
                    newTableView.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<? super Integer>) selectionListener);
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            }
            else {
                setGraphic(checkbox);
                updateCheckBoxSelection();
            }
        }

        private void onClickCheckBox() {
            TableView<S> tableview = getTableView();
            if (null == tableview) {
                return;
            }
            int rowIndex = getTableRowIndex();
            if (checkbox.isSelected()) {
                tableview.getSelectionModel().select(rowIndex);
            }
            else {
                tableview.getSelectionModel().clearSelection(rowIndex);
            }
        }

        private int getTableRowIndex() {
            TableRow<S> row = getTableRow();
            if (null == row) {
                return -1;
            }
            return row.getIndex();
        }

        private void updateCheckBoxSelection() {
            int rowIndex = getTableRowIndex();
            boolean selected = getTableView().getSelectionModel().isSelected(rowIndex);
            checkbox.setSelected(selected);
        }
    }
}
