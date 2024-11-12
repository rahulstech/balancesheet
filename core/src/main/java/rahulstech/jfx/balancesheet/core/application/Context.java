package rahulstech.jfx.balancesheet.core.application;

import java.io.InputStream;
import java.net.URL;

public interface Context {

    ApplicationContext getApplicationContext();

    Context getParentContext();

    String getDatabaseDirPath();

    URL getResourceUrl(String path);

    InputStream getResourceStream(String path);

    default Resources getResources() {
        return new Resources(getApplicationContext());
    }
}
