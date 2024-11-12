package rahulstech.jfx.balancesheet.service;

import rahulstech.jfx.balancesheet.balancesheetdb.BalanceSheetDB;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.QueryParameter;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.service.*;
import rahulstech.jfx.balancesheet.core.util.Log;
import rahulstech.jfx.balancesheet.core.util.Parameter;

import java.util.Collections;
import java.util.List;

public abstract class BaseBalanceSheetDBCRUDService<T> extends AppServiceImpl<BaseBalanceSheetDBCRUDService.CrudServiceParameter> {

    private static final String TAG = BaseBalanceSheetDBCRUDService.class.getSimpleName();

    public static final int TASK_CREATE_SINGLE = 1;

    public static final int TASK_CREATE_MULTIPLE = 2;

    public static final int TASK_UPDATE_SINGLE = 3;

    public static final int TASK_UPDATE_MULTIPLE = 4;

    public static final int TASK_DELETE_SINGLE = 5;

    public static final int TASK_DELETE_MULTIPLE = 6;

    public static final int TASK_READ_SINGLE = 7;

    public static final int TASK_READ_MULTIPLE = 8;

    public static final int TASK_COUNT = 9;

    private final BalanceSheetDB database;

    protected BaseBalanceSheetDBCRUDService(Context context) {
        super(context);
        setExecutor(context.getApplicationContext().getIoExecutor());
        this.database = BalanceSheetDB.getInstance(context);
    }

    /*******************************************
     *              Public API                 *
     ******************************************/

    public BalanceSheetDB getDatabase() {
        return database;
    }

    public PageSource<T> newPageSource(QueryParameter parameter) {
        return new CrudServicePageSource<>(this,parameter);
    }

    /*******************************************
     *              Static Methods             *
     ******************************************/

    public static CrudServiceParameter createSingleServiceParameter(Object value) {
        return new CrudServiceParameter(TASK_CREATE_SINGLE, value);
    }

    public static CrudServiceParameter readSingleServiceParameter(Parameter param) {
        return new CrudServiceParameter(TASK_READ_SINGLE,param);
    }

    public static CrudServiceParameter readMultipleServiceParameter(Parameter param) {
        return new CrudServiceParameter(TASK_READ_MULTIPLE,param);
    }

    public static CrudServiceParameter updateSingleServiceParameter(Object value) {
        return new CrudServiceParameter(TASK_UPDATE_SINGLE, value);
    }

    public static CrudServiceParameter updateMultipleServiceParameter(List<?> values) {
        return new CrudServiceParameter(TASK_UPDATE_MULTIPLE, values);
    }

    public static CrudServiceParameter deleteSingleServiceParameter(Object value) {
        return new CrudServiceParameter(TASK_DELETE_SINGLE, value);
    }

    public static CrudServiceParameter deleteMultipleServiceParameter(List<?> values) {
        return new CrudServiceParameter(TASK_DELETE_MULTIPLE, values);
    }

    /*******************************************
     *              Sub Class                  *
     ******************************************/

    public static class CrudServiceParameter implements ServiceParameter {

        private final int id;

        private final Object value;

        private final Parameter extras;

        public CrudServiceParameter(int id, Object value) {
            this(id,value,null);
        }

        public CrudServiceParameter(int id, Object value, Parameter extras) {
            this.id = id;
            this.value = value;
            this.extras = extras;
        }

        @Override
        public int getTaskId() {
            return id;
        }

        public Object getParameter() {
            return value;
        }

        @Override
        public Parameter getExtras() {
            return extras;
        }
    }

    public static class CrudServiceResult<T> implements ServiceResult {

        private final int id;

        private final T value;

        private final Throwable error;

        private final boolean successful;

        private final Parameter extras;

        public CrudServiceResult(int id, T value, Throwable error, boolean successful, Parameter extras) {
            this.id = id;
            this.value = value;
            this.error = error;
            this.successful = successful;
            this.extras = extras;
        }

        @Override
        public int getTaskId() {
            return id;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public Throwable getError() {
            return error;
        }

        @Override
        public boolean isSuccessful() {
            return successful;
        }

        @Override
        public Parameter getExtras() {
            return extras;
        }
    }

    public abstract static class CrudTask<T> extends ServiceTask<CrudServiceParameter> {

        public CrudTask(CrudServiceParameter parameter) {
            super(parameter);
        }

        @Override
        protected ServiceResult call(CrudServiceParameter param) {
            final int id = param.getTaskId();
            final Object input = param.getParameter();
            final Parameter extras = param.getExtras();
            Log.info(TAG, "calling task with id "+id);
            try {
                Object output = switch (id) {
                    case TASK_CREATE_SINGLE -> createSingle((T) input);
                    case TASK_READ_SINGLE -> readSingle((QueryParameter) input);
                    case TASK_READ_MULTIPLE -> readMultiple((QueryParameter) input);
                    case TASK_UPDATE_SINGLE -> updateSingle((T) input);
                    case TASK_UPDATE_MULTIPLE -> updateMultiple((List<T>) input);
                    case TASK_DELETE_SINGLE -> deleteSingle((T) input);
                    case TASK_DELETE_MULTIPLE -> deleteMultiple((List<T>) input);
                    case TASK_COUNT -> count((QueryParameter) input);
                    default -> null;
                };
                return new CrudServiceResult<>(id,output,null,null != output, extras);
            }
            catch (Exception ex) {
                return new CrudServiceResult<>(id,null,ex,false, extras);
            }
        }

        protected T createSingle(T value) throws Exception {
            return null;
        }

        protected List<T> createSingle(List<T> values) throws Exception {
            return null;
        }

        protected T readSingle(QueryParameter params) throws Exception {
            return null;
        }

        protected List<T> readMultiple(QueryParameter params) throws Exception {
            return Collections.emptyList();
        }


        protected T updateSingle(T value) throws Exception {
            return null;
        }

        protected List<T> updateMultiple(List<T> values) throws Exception {
            return Collections.emptyList();
        }

        protected boolean deleteSingle(T value) throws Exception {
            return false;
        }

        protected int deleteMultiple(List<T> values) throws Exception {
            return 0;
        }

        protected long count(QueryParameter params) throws Exception {
            return 0;
        }
    }
}
