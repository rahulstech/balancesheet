package rahulstech.jfx.balancesheet.frontend.validator;

import javafx.scene.Node;

public class ValidatorDecorationNode extends Node {

    private static final String KEY_DECORATION_TARGET = "rahulstech.validator.decoration.target";

    public ValidatorDecorationNode(Object target) {
        addProperty(KEY_DECORATION_TARGET,target);
    }

    public void addProperty(Object key, Object value) {
        getProperties().put(key,value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(Object key) {
        return (T) getProperties().get(key);
    }

    public <T> T getTarget() {
        return getProperty(KEY_DECORATION_TARGET);
    }
}
