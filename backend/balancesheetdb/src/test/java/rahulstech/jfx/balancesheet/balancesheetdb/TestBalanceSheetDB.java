package rahulstech.jfx.balancesheet.balancesheetdb;

import rahulstech.jfx.balancesheet.core.application.Context;

public class TestBalanceSheetDB extends BalanceSheetDB {

    public TestBalanceSheetDB() {
        super(new TestContext());
    }

    @Override
    String getJdbcUrl() {
        return "jdbc:sqlite::memory:";
    }

    @Override
    String getDatabasePath() {
        return null;
    }

    @Override
    public void close() {
        try {
            super.close();
        }
        catch (Throwable ignore) {}
    }
}
