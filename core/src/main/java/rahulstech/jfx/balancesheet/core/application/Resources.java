package rahulstech.jfx.balancesheet.core.application;

import rahulstech.jfx.balancesheet.core.util.Log;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.spi.ResourceBundleProvider;

public class Resources {

    private static final String TAG = Resources.class.getSimpleName();

    public static final String DIR_LAYOUTS = "layouts";

    public static final String DIR_STYLES = "styles";

    public static final String DIR_VALUES = "values";

    public static final String DIR_CONFIG = "config";

    private final Context context;

    public Resources(Context context) {
        this.context = context;
    }

    public URL getLayout(String name) {
        return getResourceUrl(DIR_LAYOUTS,name);
    }

    public String getStyles(String name) {
        URL url = getResourceUrl(DIR_STYLES,name);
        if (null == url) {
            return null;
        }
        return url.toExternalForm();
    }

    public InputStream getConfigStream(String name) {
        return getResourceStream(DIR_CONFIG, name);
    }

    public ResourceBundle getResourceBundle(String name) {
        return null;
    }

    public URL getResourceUrl(String subdir, String name) {
        String path = getResourcePath(getResourceRoot(),subdir,name);
        return context.getResourceUrl(path);
    }

    public InputStream getResourceStream(String subdir, String name) {
        String path = getResourcePath(getResourceRoot(),subdir,name);
        return context.getResourceStream(path);
    }

    public String getResourcePath(String root, String subdir, String name) {
        String path = Paths.get(root,subdir,name).toString();
        Log.info(TAG,"resource-path: root='"+root+"' subdir='"+subdir+"' name='"+name+"' path='"+path+"'");
        return path;
    }

    public String getResourceRoot() {
        String appPkg = context.getApplicationContext().getClass().getPackageName();
        String dir = appPkg.replace(".","/");
        return new File("/"+dir+"/").getPath();
    }
}
