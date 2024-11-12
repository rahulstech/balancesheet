package rahulstech.jfx.balancesheet.dialog;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;
import rahulstech.jfx.balancesheet.controller.InputIncomeExpenseController;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.frontend.stage.Dialog;
import rahulstech.jfx.balancesheet.frontend.controller.FxmlLoaderBuilder;
import rahulstech.jfx.balancesheet.frontend.util.AnimationUtil;

import java.io.IOException;

public class InputIncomeExpenseDialog extends Dialog {

    private final InputIncomeExpenseController controller;

    public InputIncomeExpenseDialog(Context context) {
        super(context);

        try {
            InputIncomeExpenseController controller = (InputIncomeExpenseController) new FxmlLoaderBuilder()
                    .setLayout("input_income_expense.fxml").load(context);
            Node root = controller.getRootNode();

            setContent(root);
            setShowCloseButton(true);
            setPositiveButton(newPositiveDialogButton("Save",this::onClickSave));
            setNegativeButton(newNegativeDialogButton("Clear",this::onClickClear));

            this.controller = controller;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void onClickSave(DialogEvent e) {
        controller.save();
    }

    private void onClickClear(DialogEvent e) {
        controller.clear();
    }

    @Override
    protected Animation getShowAnimation(Node node) {
        Duration duration = AnimationUtil.DURATION_SHORT3;
        Interpolator interpolator = AnimationUtil.EMPHASIZED_ACCELERATE;
        ScaleTransition scale = new ScaleTransition();
        scale.setFromX(0.6);
        scale.setFromY(0.6);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(duration);
        scale.setInterpolator(interpolator);
        scale.setNode(node);
        return scale;
    }

    @Override
    protected Animation getHideAnimation(Node node) {
        Duration durationFade = AnimationUtil.DURATION_MEDIUM2;
        Interpolator interpolatorFade = AnimationUtil.EMPHASIZED_ACCELERATE;

        FadeTransition fade =  new FadeTransition();
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDuration(durationFade);
        fade.setInterpolator(interpolatorFade);
        fade.setNode(node);

        return fade;
    }
}
