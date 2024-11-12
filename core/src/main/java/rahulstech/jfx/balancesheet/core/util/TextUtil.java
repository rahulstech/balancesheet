package rahulstech.jfx.balancesheet.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class TextUtil {

    public static boolean isEmpty(String text) {
        return null == text || text.isBlank();
    }

    public static boolean containsIgnoreCase(String search, String target) {
        if (isEmpty(search)) return true;
        if (isEmpty(target)) return false;
        return target.toLowerCase().contains(search.toLowerCase());
    }

    public static String formatCurrency(BigDecimal value, Locale locale) {
        Locale desiredLocale = null == locale ? Locale.getDefault() : locale;
        DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance(desiredLocale);
        String negativePrefix = format.getNegativePrefix();
        if (!"-".equals(negativePrefix)) {
            String currencySymbol = format.getCurrency().getSymbol();
            String symbol = null == currencySymbol ? "" : currencySymbol;
            format.setNegativePrefix(symbol+"-");
        }
        return format.format(value);
    }
}
