package rahulstech.jfx.balancesheet.service;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface PageSource<T> {

    void loadPage(long limit, long offset);

    ReadOnlyObjectProperty<PageData<T>> recentPageProperty();

    PageData<T> getRecentPage();
}
