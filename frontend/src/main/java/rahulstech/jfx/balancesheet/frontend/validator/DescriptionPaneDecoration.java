package rahulstech.jfx.balancesheet.frontend.validator;

import net.synedra.validatorfx.ValidationMessage;
import rahulstech.jfx.balancesheet.frontend.control.DescriptionPane;

public class DescriptionPaneDecoration extends BaseValidatorDecoration<DescriptionPane> {

    public DescriptionPaneDecoration() {}

    public DescriptionPaneDecoration(ValidationMessage message) {
        super(message);
    }

    @Override
    protected void applyDecoration(DescriptionPane node, ValidationMessage message) {
        node.setError(true);
        node.setErrorText(message.getText());
    }

    @Override
    protected void removeDecoration(DescriptionPane node) {
        node.setErrorText(null);
        node.setError(false);
    }
}
