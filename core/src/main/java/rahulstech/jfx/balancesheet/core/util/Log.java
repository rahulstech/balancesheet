package rahulstech.jfx.balancesheet.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private static final Logger LOGGER = Logger.getLogger(Log.class.getSimpleName());

    public static void info(String tag, String message) {
        log(Level.INFO,tag,message,null);
    }

    public static void debug(String tag, String message) {
        log(Level.FINE,tag,message,null);
    }

    public static void error(String tag, String message) {
        log(Level.SEVERE,tag,message,null);
    }

    public static void error(String tag, String message, Throwable throwable) {
        log(Level.SEVERE,tag,message,throwable);
    }

    private static void log(Level level, String tag, String message, Throwable throwable) {
        LOGGER.log(level,"["+tag+"] "+message,throwable);
    }
}
