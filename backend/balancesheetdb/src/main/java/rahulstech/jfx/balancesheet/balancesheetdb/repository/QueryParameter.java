package rahulstech.jfx.balancesheet.balancesheetdb.repository;

import rahulstech.jfx.balancesheet.core.util.Parameter;

import java.util.Arrays;

public class QueryParameter extends Parameter {

    private static final String KEY_QUERY_LIMIT = "query.limit";

    private static final String KEY_QUERY_OFFSET = "query.offset";

    private static final String KEY_ORDER_BY_COLUMNS = "query.order_by.columns";

    private static final String KEY_ORDER_BY_ORDERS = "query.order_by.orders";

    public enum SortOrder {
        ASCENDING,

        DESCENDING,
    }

    public QueryParameter() {
        super();
    }

    public QueryParameter(QueryParameter src) {
        super(src);
    }

    public void limit(long limit) {
        put(KEY_QUERY_LIMIT,limit);
    }

    public Long getLimit() {
        return get(KEY_QUERY_LIMIT);
    }

    public void offset(long offset) {
        put(KEY_QUERY_OFFSET,offset);
    }

    public Long getOffset() {
        return get(KEY_QUERY_OFFSET);
    }

    public void orderBy(String column, SortOrder order) {
        orderBy(new String[]{column},new SortOrder[]{order});
    }

    public void orderBy(String... columns) {
        if (null == columns) {
            return;
        }
        int count = columns.length;
        SortOrder[] orders = new SortOrder[count];
        Arrays.fill(orders,SortOrder.ASCENDING);
        orderBy(columns,orders);
    }

    public void orderBy(String[] columns, SortOrder[] orders) {
        put(KEY_ORDER_BY_COLUMNS,columns);
        put(KEY_ORDER_BY_ORDERS,orders);
    }

    public String[] getOrderByColumns() {
        return get(KEY_ORDER_BY_COLUMNS);
    }

    public SortOrder[] getOrderByOrders() {
        return get(KEY_ORDER_BY_ORDERS);
    }
}
