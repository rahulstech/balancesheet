package rahulstech.jfx.balancesheet.frontend.control;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import rahulstech.jfx.balancesheet.frontend.control.skin.ChipSkin;
import rahulstech.jfx.balancesheet.frontend.util.AnimationUtil;

import java.util.Objects;

public class Chip extends ButtonBase {

    private static final String DEFAULT_STYLE_CLASS = "rst-chip";

    private static final PseudoClass PSEUDO_ALERT = PseudoClass.getPseudoClass("alert");

    private static final PseudoClass PSEUDO_FILTER = PseudoClass.getPseudoClass("filter");

    private static final PseudoClass PSEUDO_INPUT = PseudoClass.getPseudoClass("input");

    private static final PseudoClass PSEUDO_SUGGESTION = PseudoClass.getPseudoClass("suggestion");

    private static final PseudoClass PSEUDO_SELECTED = PseudoClass.getPseudoClass("selected");

    public enum ChipType {
        ALERT(PSEUDO_ALERT),
        FILTER(PSEUDO_FILTER),
        INPUT(PSEUDO_INPUT),
        SUGGESTION(PSEUDO_SUGGESTION),
        ;

        private final PseudoClass pseudoClass;

        ChipType(PseudoClass pseudoClass) {
            this.pseudoClass = pseudoClass;
        }

        public PseudoClass getPseudoClass() {
            return pseudoClass;
        }
    }

    public Chip() {
        this(null,null,null);
    }

    public Chip(String text) {
        this(text, null, null);
    }

    public Chip(String text, Node leadingIcon, Node trailingIcon) {
        super(text);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setLeadingIcon(leadingIcon);
        setTrailingIcon(trailingIcon);

        this.leadingIcon.subscribe(icon -> {
           if (icon == null) {
               if (ChipType.FILTER == getChipType()) {
                   Node checkIcon = createIcon("rst-icon-material-check");
                   setLeadingIcon(checkIcon);
               }
           }
        });

        this.trailingIcon.subscribe(icon -> {
            if (icon == null) {
                if (ButtonRole.CANCEL_CLEAR == getTrailingIconRole()) {
                    Node cancelIcon = createIcon("rst-icon-material-cancel-filled");
                    setTrailingIcon(cancelIcon);
                }
            }
        });

        chipType.subscribe(type -> {
            if (type == ChipType.FILTER) {
                if (null == getLeadingIcon()) {
                    Node checkIcon = createIcon("rst-icon-material-check");
                    setLeadingIcon(checkIcon);
                }
            }
        });

        trailingIconRole.subscribe(role -> {
            if (role == ButtonRole.CANCEL_CLEAR) {
                if (null == getTrailingIcon()) {
                    Node cancelIcon = createIcon("rst-icon-material-cancel-filled");
                    setTrailingIcon(cancelIcon);
                }
            }
        });

        addEventHandlers();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ChipSkin(this);
    }

    @Override
    public void fire() {
        if (!isDisabled()) {
            setSelected(!isSelected());
            fireEvent(new ActionEvent(this,this));
        }
    }

    /*******************************************
     *             Properties                  *
     *******************************************/

    // selection

    private final BooleanProperty selected = new SimpleBooleanProperty(this,"selected",false) {

        @Override
        protected void invalidated() {
            pseudoClassStateChanged(PSEUDO_SELECTED, get());
            notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
        }
    };

    public final BooleanProperty selectedProperty() {
        return selected;
    }

    public final boolean isSelected() {
        return selected.get();
    }

    public final void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    // chip type

    private final ObjectProperty<ChipType> chipType = new SimpleObjectProperty<>(this,"chipType", ChipType.SUGGESTION) {

        @Override
        protected void invalidated() {
            PseudoClass newPseudoClass = get().getPseudoClass();
            pseudoClassStateChanged(newPseudoClass,true);
        }
    };

    public final ChipType getChipType() {
        return chipType.get();
    }

    public final void setChipType(ChipType type) {
        Objects.requireNonNull(type,"ChipType is null");
        chipType.set(type);
    }

    public final ObjectProperty<ChipType> chipTypeProperty() {
        return chipType;
    }

    // leading icon

    private final ObjectProperty<Node> leadingIcon = new SimpleObjectProperty<>(this,"leadingIcon");

    public final Node getLeadingIcon() {
        return leadingIcon.get();
    }

    public final void setLeadingIcon(Node icon) {
        leadingIcon.set(icon);
    }

    public final ObjectProperty<Node> leadingIconProperty() {
        return leadingIcon;
    }

    // trailing icon

    private final ObjectProperty<Node> trailingIcon = new SimpleObjectProperty<>(this,"trailingIcon");

    public final Node getTrailingIcon() {
        return trailingIcon.get();
    }

    public final void setTrailingIcon(Node icon) {
        trailingIcon.set(icon);
    }

    public final ObjectProperty<Node> trailingIconProperty() {
        return trailingIcon;
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

    // trailing icon action event

    private final ObjectProperty<EventHandler<ActionEvent>> onTrailingIconAction = new SimpleObjectProperty<>(this, "onActionTrailingIcon");

    public final EventHandler<ActionEvent> getOnTrailingIconAction() {
        return onTrailingIconAction.get();
    }

    public final void setOnTrailingIconAction(EventHandler<ActionEvent> handler) {
        onTrailingIconAction.set(handler);
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onTrailingIconActionProperty() {
        return onTrailingIconAction;
    }

    /************************************
     *         Lifecycle Methods        *
     ************************************/

    private WeakEventHandler<ActionEvent> onHidingHandler = new WeakEventHandler<>(null);

    private WeakEventHandler<ActionEvent> onHiddenHandler = new WeakEventHandler<>(null);

    public void hide() {
        Parent parent = getParent();

        Animation animation = getHideAnimation();
        animation.setOnFinished(e -> {
            ((Pane) parent).getChildren().remove(this);
            hidden();
            if (!onHiddenHandler.wasGarbageCollected()) {
                onHiddenHandler.handle(new ActionEvent(this,null));
            }
        });

        hiding();
        if (!onHidingHandler.wasGarbageCollected()) {
            onHidingHandler.handle(new ActionEvent(this,null));
        }
        animation.play();
    }

    public void setOnHiding(EventHandler<ActionEvent> onHiding) {
        this.onHidingHandler = new WeakEventHandler<>(onHiding);
    }

    public void setOnHide(EventHandler<ActionEvent> onHide) {
        this.onHiddenHandler = new WeakEventHandler<>(onHide);
    }

    protected Animation getHideAnimation() {
        FadeTransition fade = new FadeTransition();
        fade.setNode(this);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDuration(AnimationUtil.DURATION_MEDIUM2);
        fade.setInterpolator(AnimationUtil.STANDARD);

        return fade;
    }

    protected void hiding() {}

    protected void hidden() {}

    /********************************
     *         Helper Methods       *
     ********************************/

    private Node createIcon(String name) {
        Region icon = new Region();
        icon.getStyleClass().add(name);
        return icon;
    }

    /********************************
     *          Chip Behavior       *
     ********************************/

    private void addEventHandlers() {
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!isValidMouseClickEvent(event)) {
                return;
            }
            if (event.getTarget() == getTrailingIcon()) {
                event.consume();
                handleClickTrailingIcon();
            }
            else {
                fire();
            }
        });
    }

    private boolean isValidMouseClickEvent(MouseEvent e) {
        return (e.getButton() == MouseButton.PRIMARY &&
                ! (e.isMiddleButtonDown() || e.isSecondaryButtonDown() ||
                        e.isShiftDown() || e.isControlDown() || e.isAltDown() || e.isMetaDown()));
    }

    private void handleClickTrailingIcon() {
        ButtonRole role = getTrailingIconRole();
        if (role == ButtonRole.CANCEL_CLEAR) {
            hide();
        }
        else if (role == ButtonRole.CUSTOM) {
            EventHandler<ActionEvent> handler = onTrailingIconAction.get();
            if (null != handler) {
                handler.handle(new ActionEvent(this,this));
            }
        }
    }
}
