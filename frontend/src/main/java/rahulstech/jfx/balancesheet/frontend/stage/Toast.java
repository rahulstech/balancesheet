package rahulstech.jfx.balancesheet.frontend.stage;

import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import javafx.util.Duration;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.util.Log;
import rahulstech.jfx.balancesheet.frontend.util.AnimationUtil;

public class Toast extends PopupWindow {

    private static final String TAG = Toast.class.getSimpleName();

    private static final double TOAST_PADDING_HORIZONTAL = 15.0;

    private static final double TOAST_PADDING_VERTICAL = 15.0;

    public static final Duration AUTO_HIDE_DELAY_SHORT = Duration.seconds(2);

    public static final Duration AUTO_HIDE_DELAY_LONG = Duration.seconds(3.5);

    private PauseTransition counter;

    public Toast(Context context) {
        super();
        this.context = context;
    }

    /*****************************************************
     *                 Static Methods                    *
     ****************************************************/

    public static Toast make(Context context, String message, Duration autoHideDelay) {
        Toast toast = new Toast(context);
        toast.setContentText(message);
        toast.setAutoHideDelay(autoHideDelay);
        return toast;
    }

    public static void showShort(Context context, String message) {
        Toast toast = make(context,message,AUTO_HIDE_DELAY_SHORT);
        toast.showToast();
    }

    public static void showLong(Context context, String message) {
        Toast toast = make(context,message,AUTO_HIDE_DELAY_LONG);
        toast.showToast();
    }

    public static Toast makeCancellable(Context context, String message) {
        Toast toast = new Toast(context);
        toast.setContentText(message);
        toast.setCancelable(true);
        return toast;
    }

    /*****************************************************
     *                   Properties                      *
     ****************************************************/

    private final StringProperty contentText = new SimpleStringProperty(null);

    public final StringProperty contentTextProperty() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText.set(contentText);
    }

    public String getContentText() {
        return contentText.get();
    }

    private Duration autoHideDelay;

    public void setAutoHideDelay(Duration autoHideDelay) {
        this.autoHideDelay = autoHideDelay;
    }

    public Duration getAutoHideDelay() {
        return autoHideDelay;
    }

    private  boolean cancelable = false;

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    private final Context context;

    public Context getContext() {
        return context;
    }

    /*****************************************************
     *               Lifecycle Methods                   *
     ****************************************************/

    @Override
    protected void show() {
        // initialize view
        initialize();

        // keep invisible until animation starts
        Node root = getSceneRoot();
        root.setVisible(false);

        // show the toast to get its height and width
        super.show();

        // place the toast at bottom right corner of owner
        place();

        Animation animation = getToastShowAnimation(root);
        if (null == animation) {
            autoHideAfter(autoHideDelay);
        }
        else {
            animation.setOnFinished(e -> autoHideAfter(autoHideDelay));
            root.setVisible(true);
            animation.play();
        }
    }

    public void showToast() {
        super.show(getContext().getApplicationContext().getAppWindow());
    }

    @Override
    public void hide() {
        Node root = getSceneRoot();
        Animation animation = getToastHideAnimation(root);
        if (null == animation) {
            super.hide();
        }
        else {
            animation.setOnFinished(e -> super.hide());
            animation.play();
        }
    }

    /*****************************************************
     *               Protected Methods                   *
     ****************************************************/

    protected Animation getToastShowAnimation(Node node) {
        Bounds bounds = node.getLayoutBounds();
        double height = bounds.getHeight();

        Duration duration = AnimationUtil.DURATION_SHORT4;
        Interpolator interpolator = AnimationUtil.STANDARD_ACCELERATE;

        Timeline transition = new Timeline();
        transition.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(node.translateYProperty(), height)));
        transition.getKeyFrames().add(new KeyFrame(duration, new KeyValue(node.translateYProperty(),0, interpolator)));
        return transition;
    }

    protected Animation getToastHideAnimation(Node node) {
        Bounds bounds = node.getLayoutBounds();
        double height = bounds.getHeight();

        Duration duration = AnimationUtil.DURATION_SHORT4;
        Interpolator interpolator = AnimationUtil.STANDARD_ACCELERATE;

        Timeline transition = new Timeline();
        transition.getKeyFrames().add(new KeyFrame(Duration.ZERO, new KeyValue(node.translateYProperty(), 0)));
        transition.getKeyFrames().add(new KeyFrame(duration, new KeyValue(node.translateYProperty(),height, interpolator)));
        return transition;
    }

    /*****************************************************
     *                Private Methods                    *
     ****************************************************/

    private void initialize() {
        HBox main = new HBox();
        main.getStyleClass().add("rst-toast-main");

        String contentText = getContentText();
        Label content = new Label(contentText);
        content.getStyleClass().add("rst-toast-content");
        main.getChildren().add(content);

        if (cancelable) {
            Button btnClose = new Button();
            btnClose.getStyleClass().add("rst-close-button");
            StackPane btnCloseIcon = new StackPane();
            btnCloseIcon.getStyleClass().add("rst-close-icon");
            btnClose.setGraphic(btnCloseIcon);
            btnClose.setOnAction(e -> hide());
            main.getChildren().add(btnClose);
        }

        StackPane root = new StackPane(main);
        root.getStyleClass().add("rst-toast");

        getScene().setRoot(root);
    }

    private Node getSceneRoot() {
        return getScene().getRoot();
    }

    private void place() {
        // Get owner window dimension and position
        Window owner = getOwnerWindow();
        double ownerX = owner.getX();
        double ownerY = owner.getY();
        double ownerHeight = owner.getHeight();
        double ownerWidth = owner.getWidth();

        double width = getWidth(); // Now, this will get actual rendered width
        double height = getHeight(); // Now, this will get actual rendered height

        // Calculate placement for bottom-right corner
        double placeX = ownerX + ownerWidth - width - TOAST_PADDING_HORIZONTAL;
        double placeY = ownerY + ownerHeight - height - TOAST_PADDING_VERTICAL;

        Log.info(TAG,"ownerWidth=" + ownerWidth + " ownerHeight=" + ownerHeight + " owner at (" + ownerX + "," + ownerY + ") " +
                "width=" + width + " height=" + height + " place at (" + placeX + "," + placeY + ")");

        setAnchorX(placeX);
        setAnchorY(placeY);
    }

    private void autoHideAfter(Duration delay) {
        if (counter != null) {
            counter.stop();
            System.out.println("stopped");
            counter = null;
        }
        PauseTransition transition = new PauseTransition();
        transition.setDuration(delay);
        transition.setOnFinished(e -> {
            if (isShowing()) {
                hide();
            }
            counter = null;
        });
        transition.play();
        counter = transition;
    }
}
