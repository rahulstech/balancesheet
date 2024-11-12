package rahulstech.jfx.balancesheet.frontend.control;

import javafx.scene.Node;
import javafx.scene.layout.Region;

public class SearchField extends MaterialTextField {

    private static final String DEFAULT_STYLE_CLASS = "rst-search-field";

    public SearchField() {
        super();
        initialize();
    }

    public SearchField(String text) {
        super(text);
        initialize();
    }

    private void initialize() {
        setTextFieldStyle(Style.FILLED);
        getStyleClass().add(DEFAULT_STYLE_CLASS);

        // default leading icon
        Node searchIcon = createIcon("rst-icon-material-search");
        setLeadingIcon(searchIcon);

        // default trailing icon
        Node cancelIcon = createIcon("rst-icon-material-cancel");
        setTrailingIcon(cancelIcon);
        setTrailingIconRole(ButtonRole.CANCEL_CLEAR);
    }

    private Node createIcon(String iconName) {
        Region icon = new Region();
        icon.getStyleClass().add(iconName);
        return icon;
    }
}
