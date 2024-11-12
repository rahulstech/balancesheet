package rahulstech.jfx.balancesheet.core.application;

import java.io.InputStream;
import java.net.URL;

public abstract class ContextImpl implements Context {

    private final Context parent;

    protected ContextImpl(Context parent) {
        this.parent = parent;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return parent.getApplicationContext();
    }

    @Override
    public Context getParentContext() {
        return parent;
    }

    @Override
    public String getDatabaseDirPath() {
        return getApplicationContext().getDatabaseDirPath();
    }

    @Override
    public URL getResourceUrl(String path) {
        return getApplicationContext().getResourceUrl(path);
    }

    @Override
    public InputStream getResourceStream(String path) {
        return getApplicationContext().getResourceStream(path);
    }
}
