package rahulstech.jfx.balancesheet.frontend.stage;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.frontend.util.AnimationUtil;

public class AlertDialog extends Dialog {

    private static final double CLIP_OFFSET = 6;

    private final EventHandler<DialogButtonEvent> DEFAULT_DIALOG_BUTTON_EVENT_HANDLER = e -> e.getSource().hide();

    private Rectangle clip;

    public AlertDialog(Builder builder) {
        super(builder.getContext());
        setHideOnEscape(builder.hideOnEscape);
        initialize(builder);
    }

    /**********************************************
     *                Properties                  *
     **********************************************/

    /**********************************************
     *              Protected Method              *
     **********************************************/

    protected Node createContent(Builder builder) {
        Label messageNode = new Label(builder.contentMessage);
        messageNode.setWrapText(true);
        return messageNode;
    }

    @Override
    protected EventHandler<ActionEvent> createDialogButtonActionEventHandler(DialogButton dialogButton) {
        return actionEvent -> {
            EventHandler<DialogButtonEvent> handler = dialogButton.getOnAction();
            if (null != handler) {
                DialogButtonEvent buttonEvent = new DialogButtonEvent(this, dialogButton.getType());
                handler.handle(buttonEvent);
                if (!buttonEvent.isConsumed()) {
                    hide();
                }
            }
        };
    }

    @Override
    protected Animation getShowAnimation(Node node) {
        node.setClip(clip);
        double height = getHeight() + 2 * CLIP_OFFSET;
        Duration duration = AnimationUtil.DURATION_LONG1;
        Interpolator interpolator = AnimationUtil.STANDARD_DECELERATE;
        Timeline animation = new Timeline();
        animation.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(clip.heightProperty(),0)));
        animation.getKeyFrames().add(new KeyFrame(duration, new KeyValue(clip.heightProperty(),height,interpolator)));
        return animation;
    }

    @Override
    protected Animation getHideAnimation(Node node) {
        double height = getHeight() + 2 * CLIP_OFFSET;
        Duration duration = AnimationUtil.DURATION_MEDIUM3;
        Interpolator interpolator = AnimationUtil.EMPHASIZED_ACCELERATE;
        Timeline animation = new Timeline();
        animation.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(clip.heightProperty(),height)));
        animation.getKeyFrames().add(new KeyFrame(duration, new KeyValue(clip.heightProperty(),0,interpolator)));
        return animation;
    }

    private void initialize(Builder builder) {

        setTitle(builder.title);
        setShowCloseButton(false);

        Node content = createContent(builder);
        setContent(content);

        DialogButton btnPositive = prepareDialogButton(builder.positiveButton);
        DialogButton btnNegative = prepareDialogButton(builder.negativeButton);
        DialogButton btnNeutral = prepareDialogButton(builder.neutralButton);

        setPositiveButton(btnPositive);
        setNegativeButton(btnNegative);
        setNeutralButton(btnNeutral);

        double clipHeight = 700 + 2 * CLIP_OFFSET;
        double clipWidth = 560 + 2* CLIP_OFFSET;
        clip = new Rectangle(clipWidth,clipHeight);
        clip.setTranslateX(-CLIP_OFFSET);
        clip.setTranslateY(-CLIP_OFFSET);
    }

    private DialogButton prepareDialogButton(DialogButton button) {
        if (null != button) {
            if (null == button.getOnAction()) {
                button.setOnAction(DEFAULT_DIALOG_BUTTON_EVENT_HANDLER);
            }
            button.onActionProperty().addListener((ov, oldHandler, newHandler) -> {
                if (null == oldHandler) {
                    button.setOnAction(DEFAULT_DIALOG_BUTTON_EVENT_HANDLER);
                }
            });
        }
        return button;
    }

    public static class Builder {

        String title;
        String contentMessage;
        DialogButton positiveButton;
        DialogButton negativeButton;
        DialogButton neutralButton;
        boolean hideOnEscape;

        private final Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this; // Enables method chaining
        }

        public Builder setContentMessage(String contentMessage) {
            this.contentMessage = contentMessage;
            return this;
        }

        public Builder setPositiveButton(String label, EventHandler<DialogButtonEvent> onAction) {
            DialogButton button = new DialogButton(ButtonType.POSITIVE,label,onAction);
            return setPositiveButton(button);
        }

        public Builder setPositiveButton(String label, Node icon, EventHandler<DialogButtonEvent> onAction) {
            DialogButton button = new DialogButton(ButtonType.POSITIVE,label,icon,onAction,false);
            return setPositiveButton(button);
        }

        public Builder setPositiveButton(DialogButton positiveButton) {
            this.positiveButton = positiveButton;
            return this;
        }

        public Builder setNegativeButton(String label, EventHandler<DialogButtonEvent> onAction) {
            DialogButton button = new DialogButton(ButtonType.NEGATIVE,label,onAction);
            return setNegativeButton(button);
        }

        public Builder setNegativeButton(String label, Node icon, EventHandler<DialogButtonEvent> onAction) {
            DialogButton button = new DialogButton(ButtonType.NEGATIVE,label,icon,onAction,false);
            return setNegativeButton(button);
        }

        public Builder setNegativeButton(DialogButton negativeButton) {
            this.negativeButton = negativeButton;
            return this;
        }

        public Builder setNeutralButton(String label, EventHandler<DialogButtonEvent> onAction) {
            DialogButton button = new DialogButton(ButtonType.NEUTRAL,label,onAction);
            return setNeutralButton(button);
        }

        public Builder setNeutralButton(String label, Node icon, EventHandler<DialogButtonEvent> onAction) {
            DialogButton button = new DialogButton(ButtonType.NEUTRAL,label,icon,onAction,false);
            return setNeutralButton(button);
        }

        public Builder setNeutralButton(DialogButton neutralButton) {
            this.neutralButton = neutralButton;
            return this;
        }

        public Builder setHideOnEscape(boolean hideOnEscape) {
            this.hideOnEscape = hideOnEscape;
            return this;
        }

        public AlertDialog build() {
            return new AlertDialog(this);
        }
    }
}