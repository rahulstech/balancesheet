package rahulstech.jfx.balancesheet.frontend.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import rahulstech.jfx.balancesheet.frontend.control.skin.MaterialTextFieldSkin;

import java.util.Objects;

public class MaterialTextField extends TextField {

    public enum Style {
        FILLED("rst-filled-text-field"),
        OUTLINED("rst-outlined-text-field");

        private final String styleClass;

        Style(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    private static final PseudoClass PSEUDO_CLASS_ERROR = PseudoClass.getPseudoClass("error");

    private static final Style DEFAULT_STYLE = Style.FILLED;

    public MaterialTextField() {
        this(null);
    }

    public MaterialTextField(String text) {
        super(text);
        initialize();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MaterialTextFieldSkin(this);
    }

    private void initialize() {
        textFieldStyle.subscribe(newStyle -> getStyleClass().setAll(getTextFieldStyle().getStyleClass()));
    }

    /********************************
     *        Properties            *
     ********************************/

    // leading icon
    private final ObjectProperty<Node> leadingIcon = new SimpleObjectProperty<>(this, "leadingIcon");

    public final ObjectProperty<Node> leadingIconProperty() {
        return leadingIcon;
    }

    public final Node getLeadingIcon() {
        return leadingIcon.get();
    }

    public final void setLeadingIcon(Node icon) {
        leadingIcon.set(icon);
    }

    // trailing icon
    private final ObjectProperty<Node> trailingIcon = new SimpleObjectProperty<>(this, "trailingIcon");

    public final ObjectProperty<Node> trailingIconProperty() {
        return trailingIcon;
    }

    public final Node getTrailingIcon() {
        return trailingIcon.get();
    }

    public final void setTrailingIcon(Node icon) {
        trailingIcon.set(icon);
    }

    // trailing icon role
    private final ObjectProperty<ButtonRole> trailingIconRole = new SimpleObjectProperty<>(this, "trailingIconRole", ButtonRole.NONE);

    public final ObjectProperty<ButtonRole> trailingIconRoleProperty() {
        return trailingIconRole;
    }

    public final ButtonRole getTrailingIconRole() {
        return trailingIconRole.get();
    }

    public final void setTrailingIconRole(ButtonRole role) {
        trailingIconRole.set(role);
    }

    // trailing icon action handler
    private final ObjectProperty<EventHandler<ActionEvent>> onTrailingIconAction = new SimpleObjectProperty<>(this, "onTrailingIconAction");

    public final ObjectProperty<EventHandler<ActionEvent>> onTrailingIconActionProperty() {
        return onTrailingIconAction;
    }

    public final EventHandler<ActionEvent> getOnTrailingIconAction() {
        return onTrailingIconAction.get();
    }

    public final void setOnTrailingIconAction(EventHandler<ActionEvent> handler) {
        onTrailingIconAction.set(handler);
    }

    // text field style
    private final ObjectProperty<Style> textFieldStyle = new SimpleObjectProperty<>(this, "textFieldStyle", DEFAULT_STYLE);

    public final ObjectProperty<Style> textFieldStyleProperty() {
        return textFieldStyle;
    }

    public final Style getTextFieldStyle() {
        return textFieldStyle.get();
    }

    public final void setTextFieldStyle(Style style) {
        Objects.requireNonNull(style, "Style cannot be null");
        textFieldStyle.set(style);
    }

    // error state
    private final BooleanProperty error = new SimpleBooleanProperty(this, "error", false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(PSEUDO_CLASS_ERROR, get());
        }
    };

    public final BooleanProperty errorProperty() {
        return error;
    }

    public final boolean isError() {
        return error.get();
    }

    public final void setError(boolean isError) {
        error.set(isError);
    }
}
