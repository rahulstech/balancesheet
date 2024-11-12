package rahulstech.jfx.balancesheet.frontend.stage;

import javafx.animation.Animation;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.PopupWindow;
import rahulstech.jfx.balancesheet.core.application.Context;

import java.util.Objects;

public class Dialog extends PopupWindow  {

    private static final String TAG = Dialog.class.getSimpleName();

    private DialogButton positiveButton;

    private DialogButton negativeButton;

    private DialogButton neutralButton;

    private final Context context;

    public Dialog(Context context) {
        super();
        Objects.requireNonNull(context,"Context is null");
        this.context = context;
    }

    public Dialog(Context context, String title, boolean showCloseButton, Node content,
                  DialogButton btnPositive, DialogButton btnNegative, DialogButton btnNeutral) {
        this(context);
        setTitle(title);
        setShowCloseButton(showCloseButton);
        setContent(content);
        setPositiveButton(btnPositive);
        setNegativeButton(btnNegative);
        setNeutralButton(btnNeutral);
    }

    /****************************************
     *           Private Methods            *
     ****************************************/

    private void initialize() {

        HBox header = createHeader();

        Parent body = createBody();

        ButtonBar buttons = createButtonBar();

        BorderPane main = new BorderPane();
        main.getStyleClass().add("rst-dialog-main");

        main.setCenter(body);

        if (null != buttons) {
            BorderPane.setMargin(buttons, new Insets(24, 0, 0, 0));
            main.setBottom(buttons);
        }

        if (null != header) {
            BorderPane.setMargin(header, new Insets(0, 0, 16, 0));
            main.setTop(header);
        }

        StackPane root = new StackPane(main);
        root.getStyleClass().add("rst-dialog");

        getScene().setRoot(root);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("rst-dialog-header");
        header.setAlignment(Pos.TOP_RIGHT);

        VBox titleSection = createTitleSection();
        if (null != titleSection) {
            HBox.setHgrow(titleSection, Priority.ALWAYS);
            header.getChildren().add(titleSection);
        }

        if (isShowCloseButton()) {
            Node btnClose = createCloseButton();
            HBox.setHgrow(btnClose, Priority.NEVER);
            header.getChildren().add(btnClose);
        }

        if (header.getChildren().isEmpty()) {
            return null;
        }

        return header;
    }

    private VBox createTitleSection() {
        String titleText = getTitle();
        if (null == titleText || titleText.isBlank()) {
            return null;
        }

        Label title = new Label();
        title.getStyleClass().add("rst-dialog-title");
        title.setText(titleText);

        VBox titleSection = new VBox();
        titleSection.setAlignment(Pos.TOP_LEFT);
        titleSection.getChildren().add(title);
        return titleSection;
    }

    private Node createCloseButton() {
        Button btnClose = new Button();
        btnClose.getStyleClass().add("rst-close-button");
        StackPane btnCloseIcon = new StackPane();
        btnCloseIcon.getStyleClass().add("rst-close-icon");
        btnClose.setGraphic(btnCloseIcon);
        btnClose.setOnAction(e -> hide());
        return btnClose;
    }

    private Parent createBody() {
        StackPane body = new StackPane();
        body.getStyleClass().addAll("rst-dialog-body");
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(body.widthProperty());
        clip.heightProperty().bind(body.heightProperty());
        body.setClip(clip);
        Node content = getContent();
        body.getChildren().add(content);
        return body;
    }

    private ButtonBar createButtonBar() {
        DialogButton btnPositive = this.positiveButton;
        DialogButton btnNegative = this.negativeButton;
        DialogButton btnNeutral = this.neutralButton;

        ButtonBar buttons = new ButtonBar();
        buttons.setButtonOrder("L+NY");
        buttons.getStyleClass().add("rst-dialog-button-bar");

        if (btnPositive != null) {
            Button button = createDialogButtonControl(btnPositive);
            buttons.getButtons().add(button);
        }
        if (btnNegative != null) {
            Button button = createDialogButtonControl(btnNegative);
            buttons.getButtons().add(button);
        }
        if (btnNeutral != null) {
            Button button = createDialogButtonControl(btnNeutral);
            buttons.getButtons().add(button);
        }

        if (buttons.getButtons().isEmpty()) {
            return null;
        }

        return buttons;
    }

    protected Button createDialogButtonControl(DialogButton dialogButton) {
        Button button = dialogButton.createButton();
        button.setOnAction(createDialogButtonActionEventHandler(dialogButton));
        ButtonBar.setButtonData(button, dialogButton.getButtonData());
        return button;
    }

    protected EventHandler<ActionEvent> createDialogButtonActionEventHandler(DialogButton dialogButton) {
        return new DialogButtonActionEventHandler(this,dialogButton);
    }

    /****************************************
     *         Lifecycle Methods            *
     ****************************************/

    public void showDialog() {
        super.show(context.getApplicationContext().getAppWindow());
    }

    @Override
    protected void show() {
        initialize();

        super.show();

        Node root = getSceneRoot();
        Animation animation = getShowAnimation(root);
        if (null != animation) {
            animation.play();
        }
    }

    @Override
    public void hide() {
        Node root = getSceneRoot();
        Animation animation = getHideAnimation(root);
        if (null != animation) {
            animation.setOnFinished(e -> super.hide());
            animation.play();
        }
        else {
            super.hide();
        }
    }

    protected Animation getShowAnimation(Node node) {
        return null;
    }

    protected Animation getHideAnimation(Node node) {
        return null;
    }

    /****************************************
     *              Properties              *
     ****************************************/

    public final Context getContext() {
        return context;
    }

    public final Node getSceneRoot() {
        return getScene().getRoot();
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        ensureDialogNotShowing();
        title = newTitle;
    }

    private boolean showCloseButton;

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        ensureDialogNotShowing();
        this.showCloseButton = showCloseButton;
    }

    private Node content;

    public Node getContent() {
        return content;
    }

    public void setContent(Node newContent) {
        ensureDialogNotShowing();
        content = newContent;
    }

    public void setPositiveButton(DialogButton btnPositive) {
        ensureDialogNotShowing();
        this.positiveButton = btnPositive;
    }

    public DialogButton getPositiveButton() {
        return positiveButton;
    }

    public void setNegativeButton(DialogButton btnNegative) {
        ensureDialogNotShowing();
        this.negativeButton = btnNegative;
    }

    public DialogButton getNegativeButton() {
        return negativeButton;
    }

    public void setNeutralButton(DialogButton btnNeutral) {
        ensureDialogNotShowing();
        this.neutralButton = btnNeutral;
    }

    public DialogButton getNeutralButton() {
        return neutralButton;
    }

    private void ensureDialogNotShowing() {
        if (isShowing()) {
            throw new IllegalStateException("updating ui is restricted while dialog is showing");
        }
    }

    /****************************************
     *             Static Methods           *
     ****************************************/

    public static DialogButton newPositiveDialogButton(String label, EventHandler<DialogButtonEvent> onAction) {
        return new DialogButton(ButtonType.POSITIVE,label,onAction);
    }

    public static DialogButton newNegativeDialogButton(String label, EventHandler<DialogButtonEvent> onAction) {
        return new DialogButton(ButtonType.NEGATIVE,label,onAction);
    }

    public static DialogButton newNeutralDialogButton(String label, EventHandler<DialogButtonEvent> onAction) {
        return new DialogButton(ButtonType.NEUTRAL,label,onAction);
    }

    private static void setButtonType(Node button, ButtonType type) {
        button.getProperties().put(ButtonType.class, type);
    }

    private static ButtonType getButtonType(Node button) {
        return (ButtonType) button.getProperties().get(ButtonType.class);
    }

    /****************************************
     *             Sub Classes              *
     ****************************************/

    public enum ButtonType {
        POSITIVE(ButtonBar.ButtonData.YES),

        NEGATIVE(ButtonBar.ButtonData.NO),

        NEUTRAL(ButtonBar.ButtonData.LEFT);

        final ButtonBar.ButtonData data;

        ButtonType(ButtonBar.ButtonData data) {
            this.data = data;
        }

        public ButtonBar.ButtonData getData() {
            return data;
        }
    }

    public static class DialogEvent extends Event {

        public static final EventType<DialogEvent> DIALOG_EVENT_TYPE = new EventType<>(Event.ANY,"DialogEvent");

        public DialogEvent(Dialog source) {
            super(source,null, DIALOG_EVENT_TYPE);
        }

        @Override
        public Dialog getSource() {
            return (Dialog) super.getSource();
        }

        @SuppressWarnings("unchecked")
        @Override
        public EventType<DialogEvent> getEventType() {
            return (EventType<DialogEvent>) super.getEventType();
        }
    }

    public static class DialogButtonEvent extends DialogEvent {

        public static final EventType<DialogEvent> DIALOG_BUTTON_EVENT_TYPE = new EventType<>(DIALOG_EVENT_TYPE,"DialogButtonEvent");

        private final ButtonType type;

        public DialogButtonEvent(Dialog source, ButtonType type) {
            super(source);
            this.type = type;
        }

        public ButtonType getType() {
            return type;
        }
    }

    public static class DialogButton {

        private final ButtonType type;

        public DialogButton(ButtonType type) {
            this(type, null, null, null, false);
        }

        public DialogButton(ButtonType type, String label, EventHandler<DialogButtonEvent> onAction) {
            this(type, label, null, onAction, false);
        }

        public DialogButton(ButtonType type, String label, Node icon, EventHandler<DialogButtonEvent> onAction, boolean disabled) {
            this.type = type;
            setLabel(label);
            setIcon(icon);
            setOnAction(onAction);
            setDisabled(disabled);
        }

        private final StringProperty label = new SimpleStringProperty(null);

        public final StringProperty labelProperty() {
            return label;
        }

        public void setLabel(String newLabel) {
            label.set(newLabel);
        }

        public String getLabel() {
            return label.get();
        }

        private final ObjectProperty<Node> icon = new SimpleObjectProperty<>(null);

        public final ObjectProperty<Node> iconProperty() {
            return icon;
        }

        public void setIcon(Node newIcon) {
            icon.set(newIcon);
        }

        public Node getIcon() {
            return icon.get();
        }

        private final BooleanProperty disabled = new SimpleBooleanProperty(false);

        public final BooleanProperty disabledProperty() {
            return disabled;
        }

        public void setDisabled(boolean isDisabled) {
            disabled.set(isDisabled);
        }

        public boolean isDisabled() {
            return disabled.get();
        }

        private final ObjectProperty<EventHandler<DialogButtonEvent>> onAction = new SimpleObjectProperty<>(null);

        public final ObjectProperty<EventHandler<DialogButtonEvent>> onActionProperty() {
            return onAction;
        }

        public void setOnAction(EventHandler<DialogButtonEvent> handler) {
            onAction.set(handler);
        }

        public EventHandler<DialogButtonEvent> getOnAction() {
            return onAction.get();
        }

        public ButtonType getType() {
            return type;
        }

        public ButtonBar.ButtonData getButtonData() {
            return type.getData();
        }

        Button createButton() {
            Button button = new Button();
            button.getStyleClass().add("rst-dialog-button");
            button.textProperty().bind(label);
            button.graphicProperty().bind(icon);
            button.disableProperty().bind(disabled);
            setButtonType(button,type);
            if (type == ButtonType.POSITIVE) {
                button.setDefaultButton(true);
            }
            return button;
        }
    }

    public static class DialogButtonActionEventHandler implements EventHandler<ActionEvent> {

        private final Dialog dialog;
        private final DialogButton dialogButton;

        DialogButtonActionEventHandler(Dialog dialog, DialogButton dialogButton) {
            this.dialog = dialog;
            this.dialogButton = dialogButton;
        }

        public Dialog getDialog() {
            return dialog;
        }

        public DialogButton getDialogButton() {
            return dialogButton;
        }

        @Override
        public void handle(ActionEvent event) {
            EventHandler<DialogButtonEvent> handler = getDialogButton().getOnAction();
            if (null != handler) {
                DialogButtonEvent buttonEvent = newDialogButtonEvent();
                handler.handle(buttonEvent);
            }
        }

        public DialogButtonEvent newDialogButtonEvent() {
            return new DialogButtonEvent(getDialog(),getDialogButton().getType());
        }
    }
}
