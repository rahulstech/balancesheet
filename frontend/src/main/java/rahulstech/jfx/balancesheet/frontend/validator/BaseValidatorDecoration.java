package rahulstech.jfx.balancesheet.frontend.validator;

import javafx.scene.Node;
import net.synedra.validatorfx.Decoration;
import net.synedra.validatorfx.ValidationMessage;

public abstract class BaseValidatorDecoration<T extends Node> implements Decoration {

    private ValidationMessage message;

    public BaseValidatorDecoration() {}

    public BaseValidatorDecoration(ValidationMessage message) {
        this();
        setMessage(message);
    }

    public void setMessage(ValidationMessage message) {
        this.message = message;
    }

    public ValidationMessage getMessage() {
        return message;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void add(Node node) {
        applyDecoration((T) node, getMessage());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void remove(Node node) {
        removeDecoration((T) node);
    }

    protected abstract void applyDecoration(T node, ValidationMessage message);

    protected abstract void removeDecoration(T node);
}
