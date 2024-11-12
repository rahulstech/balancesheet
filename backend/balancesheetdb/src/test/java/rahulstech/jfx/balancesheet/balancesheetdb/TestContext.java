package rahulstech.jfx.balancesheet.balancesheetdb;

import rahulstech.jfx.balancesheet.core.application.ApplicationContext;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.application.Resources;

import java.io.InputStream;
import java.net.URL;

public class TestContext implements Context {

    public TestContext() {}

    @Override
    public ApplicationContext getApplicationContext() {
        return null;
    }

    @Override
    public Context getParentContext() {
        return null;
    }

    @Override
    public String getDatabaseDirPath() {
        return null;
    }

    @Override
    public Resources getResources() {
        return new Resources(this);
    }

    @Override
    public URL getResourceUrl(String path) {
        return TestContext.class.getResource(path);
    }

    @Override
    public InputStream getResourceStream(String path) {
        return TestContext.class.getResourceAsStream(path);
    }
}