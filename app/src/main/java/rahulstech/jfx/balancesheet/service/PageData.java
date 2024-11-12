package rahulstech.jfx.balancesheet.service;

import java.util.List;

public class PageData<T> {

    private final long totalEntries;

    private final long offset;

    private final long limit;

    private final List<T> data;

    public PageData(long totalEntries, long offset, long limit, List<T> data) {
        this.totalEntries = totalEntries;
        this.offset = offset;
        this.limit = limit;
        this.data = data;
    }

    public long getTotalEntries() {
        return totalEntries;
    }

    public long getOffset() {
        return offset;
    }

    public long getLimit() {
        return limit;
    }

    public List<T> getData() {
        return data;
    }

    public int getDataSize() {
        return null == data ? 0 : data.size();
    }
}
