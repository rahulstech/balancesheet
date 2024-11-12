package rahulstech.jfx.balancesheet.frontend.control.skin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import rahulstech.jfx.balancesheet.frontend.control.MaterialPagination;

public class MaterialPaginationSkin extends SkinBase<MaterialPagination> {

    private final HBox container;
    private final Button btnFirstPage, btnPreviousPage, btnNextPage, btnLastPage;
    private final ComboBox<Long> maxEntriesComboBox;

    private boolean isUpdating = false;

    public MaterialPaginationSkin(MaterialPagination control) {
        super(control);

        btnFirstPage = createPaginationButton("first-page-button", control.onActionFirstPageProperty());
        btnPreviousPage = createPaginationButton("previous-page-button", control.onActionPreviousPageProperty());
        btnNextPage = createPaginationButton("next-page-button", control.onActionNextPageProperty());
        btnLastPage = createPaginationButton("last-page-button", control.onActionLastPageProperty());

        // Setup forward and backward button disabling behavior
        bindButtonDisableBehavior(control);

        // ComboBox for selecting max entries per page
        maxEntriesComboBox = new ComboBox<>();
        maxEntriesComboBox.getStyleClass().add("max-entries");
        selectedMaxEntries(control.getMaxEntries());

        // Sync ComboBox and maxEntriesProperty with proper updating control
        syncComboBoxWithProperty();

        // Labels for displaying max entries and progress
        Label maxEntriesLabel = createLabel("max-entries-label", control.maxEntriesTextProperty());
        Label progressLabel = createLabel("progress-label", control.progressTextProperty());

        // Container setup
        container = new HBox();
        container.getStyleClass().add("rst-pagination");
        container.getChildren().addAll(maxEntriesLabel, maxEntriesComboBox, progressLabel, btnFirstPage, btnPreviousPage, btnNextPage, btnLastPage);

        getChildren().add(container);
    }

    // Helper method for creating buttons with default action and user-defined handler
    private Button createPaginationButton(String styleClass, ObjectProperty<EventHandler<ActionEvent>> userHandler) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);
        button.setGraphic(createIcon());
        userHandler.subscribe(button::setOnAction);
        return button;
    }

    // Helper method to create icons
    private StackPane createIcon() {
        StackPane icon = new StackPane();
        icon.getStyleClass().addAll("rst-icon", "arrow");
        return icon;
    }

    // Helper method to create labels
    private Label createLabel(String styleClass, ObservableValue<String> textProperty) {
        Label label = new Label();
        label.getStyleClass().add(styleClass);
        label.textProperty().bind(textProperty);
        return label;
    }

    // Synchronize ComboBox selection with the maxEntriesProperty
    private void syncComboBoxWithProperty() {
        MaterialPagination control = getSkinnable();
        LongProperty maxEntriesProperty = control.maxEntriesProperty();
        ObjectProperty<ObservableList<Long>> maxEntriesItemsProperty = control.maxEntriesItemsProperty();

        //maxEntriesComboBox.itemsProperty().bind(maxEntriesItemsProperty);
        maxEntriesItemsProperty.subscribe(newItems -> {
            if (null == newItems || newItems.isEmpty()) {
                maxEntriesComboBox.setItems(FXCollections.emptyObservableList());
                maxEntriesComboBox.getSelectionModel().select(null);
                return;
            }
            maxEntriesComboBox.setItems(newItems);
            long maxEntries = control.getMaxEntries();
            if (maxEntries < 1) {
                maxEntriesComboBox.getSelectionModel().select(null);
            }
            else {
                maxEntriesComboBox.getSelectionModel().select(maxEntries);
            }
        });

        maxEntriesComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (isUpdating) return;

            isUpdating = true;
            long maxEntries = null == newValue ? 0 : newValue;
            maxEntriesProperty.set(maxEntries);
            isUpdating = false;

            control.goToFirstPage();
        });

        maxEntriesProperty.addListener((obs, oldValue, newValue) -> {
            if (isUpdating) return;

            isUpdating = true;
            selectedMaxEntries(newValue.longValue());
            isUpdating = false;
        });
    }

    private void selectedMaxEntries(long maxEntries) {
        ObservableList<Long> items = maxEntriesComboBox.getItems();
        if (null != items && items.contains(maxEntries)) {
            maxEntriesComboBox.getSelectionModel().select(maxEntries);
        }
        else {
            maxEntriesComboBox.getSelectionModel().clearSelection();
        }
    }

    // Bind forward and backward button disable behavior
    private void bindButtonDisableBehavior(MaterialPagination control) {
        BooleanProperty firstPageDisabled = new SimpleBooleanProperty();
        BooleanProperty lastPageDisabled = new SimpleBooleanProperty();

        firstPageDisabled.bind(control.currentPageNumberProperty().map(pageNumber -> {
            long current = pageNumber.longValue();
            return current < 1 || current == 1;
        }));

        lastPageDisabled.bind(control.currentPageNumberProperty().map(pageNumber -> {
            long current = pageNumber.longValue();
            return current < 1 || current == control.getMaxPageNumber();
        }));

        btnFirstPage.disableProperty().bind(firstPageDisabled);
        btnPreviousPage.disableProperty().bind(firstPageDisabled);
        btnNextPage.disableProperty().bind(lastPageDisabled);
        btnLastPage.disableProperty().bind(lastPageDisabled);
    }

    // Layout calculations
    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + container.minHeight(width) + bottomInset;
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + container.minWidth(height) + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + container.prefHeight(width) + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + container.prefWidth(height) + rightInset;
    }
}
