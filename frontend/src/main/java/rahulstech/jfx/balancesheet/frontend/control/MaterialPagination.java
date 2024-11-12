package rahulstech.jfx.balancesheet.frontend.control;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import rahulstech.jfx.balancesheet.frontend.control.skin.MaterialPaginationSkin;

import java.lang.ref.WeakReference;

public class MaterialPagination extends Control {

    private static final String TAG = MaterialPagination.class.getSimpleName();

    public MaterialPagination() {
        super();

        setOnActionFirstPage(e->goToFirstPage());
        setOnActionPreviousPage(e->goToPreviousPage());
        setOnActionNextPage(e->goToNextPage());
        setOnActionLastPage(e->goToLastPage());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MaterialPaginationSkin(this);
    }

    /****************************************
     *              Public API              *
     ****************************************/

    public void goToFirstPage() {
        setCurrentPageNumber(1);
    }

    public void goToNextPage() {
        setCurrentPageNumber(getCurrentPageNumber() + 1);
    }

    public void goToPreviousPage() {
        setCurrentPageNumber(getCurrentPageNumber() - 1);
    }

    public void goToLastPage() {
        setCurrentPageNumber(getMaxPageNumber());
    }

    /****************************************
     *              Properties              *
     ****************************************/

    private final LongProperty maxPageNumber = new SimpleLongProperty(this,"maxPageNumber", Long.MAX_VALUE);

    public final LongProperty maxPageNumberProperty() {
        return maxPageNumber;
    }

    public final void setMaxPageNumber(long pageNumber) {
        maxPageNumber.set(pageNumber);
    }

    public final long getMaxPageNumber() {
        return maxPageNumber.get();
    }

    private final LongProperty currentPageNumber = new SimpleLongProperty(this,"currentPageNumber") {

        @Override
        public void set(long newValue) {
            long maxPageNumber = getMaxPageNumber();
            if (maxPageNumber < 1) {
                super.set(0);
                return;
            }
            long pageNumber = Math.min(maxPageNumber, Math.max(newValue, 1));
            super.set(pageNumber);
        }
    };

    public final LongProperty currentPageNumberProperty() {
        return currentPageNumber;
    }

    public final void setCurrentPageNumber(long pageNumber) {
        currentPageNumber.set(pageNumber);
    }

    public final long getCurrentPageNumber() {
        return currentPageNumber.get();
    }

    private final StringProperty maxEntriesText = new SimpleStringProperty(this,"maxEntriesText",null);

    public final StringProperty maxEntriesTextProperty() {
        return maxEntriesText;
    }

    public final void setMaxEntriesText(String text) {
        maxEntriesText.set(text);
    }

    public final String getMaxEntriesText() {
        return maxEntriesText.get();
    }

    private final ObjectProperty<ObservableList<Long>> maxEntriesItems
            = new SimpleObjectProperty<>(this,"maxEntriesItems"){

        WeakReference<ObservableList<Long>> itemsRef;

        @Override
        protected void invalidated() {
            final ObservableList<Long> oldItems = itemsRef == null ? null : itemsRef.get();
            final ObservableList<Long> newItems = getMaxEntriesItems();
            // Fix for RT-36425
            if (newItems != null && newItems == oldItems) {
                return;
            }
            itemsRef = new WeakReference<>(newItems);
        }
    };

    public final ObjectProperty<ObservableList<Long>> maxEntriesItemsProperty() {
        return maxEntriesItems;
    }

    public final void setMaxEntriesItems(ObservableList<Long> items) {
        maxEntriesItems.set(null == items ? FXCollections.emptyObservableList() : items);
    }

    private final LongProperty maxEntries = new SimpleLongProperty(this,"maxEntries") {

        @Override
        public void set(long newValue) {
            ObservableList<Long> items = getMaxEntriesItems();
            if (null == items || items.isEmpty()) {
                super.set(-1);
                return;
            }
            super.set(newValue);
        }
    };

    public final LongProperty maxEntriesProperty() {
        return maxEntries;
    }

    public final void setMaxEntries(long entries) {
        maxEntries.set(entries);
    }

    public final long getMaxEntries() {
        return maxEntries.get();
    }

    public final ObservableList<Long> getMaxEntriesItems() {
        return maxEntriesItems.get();
    }

    private final StringProperty progressText = new SimpleStringProperty(this,"progressText", null);

    public final StringProperty progressTextProperty() {
        return progressText;
    }

    public final void setProgressText(String progress) {
        progressText.set(progress);
    }

    public final String getProgressText() {
        return progressText.get();
    }

    // onActionFirstPage property
    private final ObjectProperty<EventHandler<ActionEvent>> onActionFirstPage = new SimpleObjectProperty<>(this, "onActionFirstPage", null);

    public final ObjectProperty<EventHandler<ActionEvent>> onActionFirstPageProperty() {
        return onActionFirstPage;
    }

    public void setOnActionFirstPage(EventHandler<ActionEvent> onAction) {
        onActionFirstPage.set(onAction);
    }

    public EventHandler<ActionEvent> getOnActionFirstPage() {
        return onActionFirstPage.get();
    }

    // onActionLastPage property
    private final ObjectProperty<EventHandler<ActionEvent>> onActionLastPage = new SimpleObjectProperty<>(this, "onActionLastPage", null);

    public final ObjectProperty<EventHandler<ActionEvent>> onActionLastPageProperty() {
        return onActionLastPage;
    }

    public void setOnActionLastPage(EventHandler<ActionEvent> onAction) {
        onActionLastPage.set(onAction);
    }

    public EventHandler<ActionEvent> getOnActionLastPage() {
        return onActionLastPage.get();
    }

    // onActionNextPage property
    private final ObjectProperty<EventHandler<ActionEvent>> onActionNextPage = new SimpleObjectProperty<>(this, "onActionNextPage", null);

    public final ObjectProperty<EventHandler<ActionEvent>> onActionNextPageProperty() {
        return onActionNextPage;
    }

    public void setOnActionNextPage(EventHandler<ActionEvent> onAction) {
        onActionNextPage.set(onAction);
    }

    public EventHandler<ActionEvent> getOnActionNextPage() {
        return onActionNextPage.get();
    }

    // onActionPreviousPage property
    private final ObjectProperty<EventHandler<ActionEvent>> onActionPreviousPage = new SimpleObjectProperty<>(this, "onActionPreviousPage", null);

    public final ObjectProperty<EventHandler<ActionEvent>> onActionPreviousPageProperty() {
        return onActionPreviousPage;
    }

    public void setOnActionPreviousPage(EventHandler<ActionEvent> onAction) {
        onActionPreviousPage.set(onAction);
    }

    public EventHandler<ActionEvent> getOnActionPreviousPage() {
        return onActionPreviousPage.get();
    }
}
