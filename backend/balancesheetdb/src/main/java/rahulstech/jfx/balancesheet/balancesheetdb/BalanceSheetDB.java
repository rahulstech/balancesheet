package rahulstech.jfx.balancesheet.balancesheetdb;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.IncomeExpenseRepo;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.impl.IncomeExpenseRepoImpl;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class BalanceSheetDB {

    private static final String TAG = BalanceSheetDB.class.getSimpleName();

    private static final String DATABASE_NAME = "balance_sheet.sqlite3";

    private static BalanceSheetDB instance;

    public static BalanceSheetDB getInstance(Context context) {
        if (null == instance) {
            instance = new BalanceSheetDB(context);
        }
        return instance;
    }

    private final Context context;

    private ConnectionSource source;

    BalanceSheetDB(Context context) {
        this.context = context;
        initialize();
    }

    void initialize() {
        try {
            String jdbcUrl = getJdbcUrl();
            this.source = new JdbcConnectionSource(jdbcUrl);

            IncomeExpenseRepoImpl.createTable(this);
        }
        catch (Exception ex) {
            throw new DatabaseException(ex);
        }
    }

    String getJdbcUrl() {
        String path = getDatabasePath();
        return "jdbc:sqlite:"+path;
    }

    String getDatabasePath() {
        String dirPath = context.getDatabaseDirPath();
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String path = Paths.get(context.getDatabaseDirPath(),DATABASE_NAME).toString();
        Log.info(TAG,"DB_PATH '"+path+"'");
        return path;
    }

    ////////////////////////////////////////////////////
    ///               Public Methods                ///
    //////////////////////////////////////////////////

    public ConnectionSource getConnectionSource() {
        return this.source;
    }

    public void close() {
        if (null != source) {
            try {
                source.close();
            }
            catch (IOException ex) {
                throw new DatabaseException(ex);
            }
        }
    }

    ////////////////////////////////////////////////////
    ///               Repository Methods            ///
    //////////////////////////////////////////////////

    private IncomeExpenseRepo repoIncomeExpense;

    public IncomeExpenseRepo getIncomeExpenseRepo() {
        if (null == repoIncomeExpense) {
            try {
                repoIncomeExpense = new IncomeExpenseRepoImpl(this);
            }
            catch (Exception ex) {
                Log.error(TAG, "can not create IncomeExpenseRepo",ex);
                throw new DatabaseException(ex);
            }
        }
        return repoIncomeExpense;
    }
}
