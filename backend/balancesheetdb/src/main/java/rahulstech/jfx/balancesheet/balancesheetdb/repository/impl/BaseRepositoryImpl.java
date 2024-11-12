package rahulstech.jfx.balancesheet.balancesheetdb.repository.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import rahulstech.jfx.balancesheet.balancesheetdb.BalanceSheetDB;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.BaseRepository;

import java.util.Collection;
import java.util.Objects;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

    private final BalanceSheetDB database;

    private final Dao<T,Object> dao;

    protected BaseRepositoryImpl(BalanceSheetDB db, Class<T> clazz) throws Exception {
        this.database = db;
        this.dao = createDao(db,clazz);
    }

    protected Dao<T,Object> createDao(BalanceSheetDB db, Class<T> clazz) throws Exception {
        return DaoManager.createDao(db.getConnectionSource(),clazz);
    }

    public BalanceSheetDB getDatabase() {
        return database;
    }

    @SuppressWarnings("unchecked")
    public final <ID> Dao<T,ID> getDao() {
        return (Dao<T, ID>) dao;
    }

    @Override
    public long count() throws Exception {
        return getDao().countOf();
    }

    @Override
    public T create(T value) throws Exception {
        Objects.requireNonNull(value, "can not create from null instance");
        if (1 == dao.create(value)) {
            return value;
        }
        return null;
    }

    @Override
    public T queryById(Object id) throws Exception {
        return dao.queryForId(id);
    }

    @Override
    public boolean update(T value) throws Exception {
        Objects.requireNonNull(value, "can not update null instance");
        return 1 == dao.update(value);
    }

    @Override
    public int updateMultiple(Collection<T> values) throws Exception {
        if (values.isEmpty()) {
            return 0;
        }
        return TransactionManager.callInTransaction(dao.getConnectionSource(),() -> {
            int changes = 0;
            for (T value : values) {
                changes += dao.update(value);
            }
            return changes;
        });
    }

    @Override
    public boolean delete(T value) throws Exception {
        Objects.requireNonNull(value, "can not delete null instance");
        return 1 == dao.delete(value);
    }

    @Override
    public int deleteMultiple(Collection<T> values) throws Exception {
        return dao.delete(values);
    }
}
