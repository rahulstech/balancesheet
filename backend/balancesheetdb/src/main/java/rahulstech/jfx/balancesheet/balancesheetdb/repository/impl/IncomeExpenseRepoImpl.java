package rahulstech.jfx.balancesheet.balancesheetdb.repository.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import rahulstech.jfx.balancesheet.balancesheetdb.BalanceSheetDB;
import rahulstech.jfx.balancesheet.balancesheetdb.DatabaseException;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpense;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpenseType;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.IncomeExpenseRepo;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.QueryParameter;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class IncomeExpenseRepoImpl extends BaseRepositoryImpl<IncomeExpense> implements IncomeExpenseRepo {

    private static final String TAG = IncomeExpenseRepoImpl.class.getSimpleName();

    public IncomeExpenseRepoImpl(BalanceSheetDB db) throws Exception {
        super(db,IncomeExpense.class);
    }
    
    public static void createTable(BalanceSheetDB db) {
        try {
            ConnectionSource source = db.getConnectionSource();
            TableUtils.createTableIfNotExists(source, IncomeExpense.class);
        }
        catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<IncomeExpense> query(QueryParameter params) throws Exception {
        String phrase = params.getString(KEY_PHRASE);
        LocalDate dateStart = params.getLocalDate(KEY_DATE_START);
        LocalDate dateEnd = params.getLocalDate(KEY_DATE_END);
        IncomeExpenseType[] types = params.get(KEY_TYPE);
        Long limit = params.getLimit();
        Long offset = params.getOffset();
        String[] orderByColumns = params.getOrderByColumns();
        QueryParameter.SortOrder[] orderByOrders = params.getOrderByOrders();
        Dao<IncomeExpense,Long> dao = getDao();

        Where<IncomeExpense,Long> where = dao.queryBuilder().where();
        where.raw("1=1");
        if (null != types) {
            where.and().in("type", (Object[]) types);
        }
        if (null != dateStart) {
            where.and().ge("date", dateStart);
        }
        if (null != dateEnd) {
            where.and().le("date", dateEnd);
        }
        if (null != phrase && !phrase.isBlank()) {
            where.and().like("note", "%"+phrase+"%");
        }

        QueryBuilder<IncomeExpense,Long> builder = dao.queryBuilder()
                .limit(limit).offset(offset);
        builder.setWhere(where);
        if (null != orderByColumns) {
            for (int i = 0; i < orderByColumns.length; i++) {
                String column = orderByColumns[i];
                boolean ascending = orderByOrders[i] == QueryParameter.SortOrder.ASCENDING;
                builder.orderBy(column,ascending);
            }
        }

        return builder.query();
    }

    @Override
    public long count(QueryParameter params) throws Exception {
        LocalDate date = params.getLocalDate(KEY_DATE);
        LocalDate dateStart = params.getLocalDate(KEY_DATE_START);
        LocalDate dateEnd = params.getLocalDate(KEY_DATE_END);
        IncomeExpenseType[] types = params.get(KEY_TYPE);
        Long limit = params.getLimit();
        Long offset = params.getOffset();
        Dao<IncomeExpense,Long> dao = getDao();

        Where<IncomeExpense,Long> where = dao.queryBuilder().where();
        where.raw("1=1");
        if (null != types) {
            where.and().in("type", (Object[]) types);
        }
        if (null != date) {
            where.and().eq("date",date);
        }
        else {
            if (null != dateStart) {
                where.and().ge("date", dateStart);
            }
            if (null != dateEnd) {
                where.and().le("date", dateEnd);
            }
        }
        QueryBuilder<IncomeExpense,Long> builder = dao.queryBuilder().setCountOf(true)
                .limit(limit).offset(offset);
        builder.setWhere(where);
        return dao.countOf(builder.prepare());
    }
}
