package rahulstech.jfx.balancesheet.frontend.util;

public class Theme {

    @SuppressWarnings("DataFlowIssue")
    public static String getDefaultTheme() {
        return Theme.class.getResource("/rahulstech/jfx/balancesheet/frontend/styles/theme.css").toExternalForm();
    }
}
