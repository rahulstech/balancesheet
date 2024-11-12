package rahulstech.jfx.balancesheet.frontend.validator;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.Decoration;
import net.synedra.validatorfx.ValidationMessage;

import java.util.function.Consumer;
import java.util.function.Function;

public class ValidatorCheckBuilder {

    private final String key;

    private final Check check;

    public ValidatorCheckBuilder(String key, ObservableValue<?> value) {
        Check check = new Check();
        check.dependsOn(key,value);
        this.check = check;
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public ValidatorCheckBuilder rules(Consumer<Check.Context>... rules) {
        for (Consumer<Check.Context> rule : rules) {
            if (rule instanceof ValidatorRule) {
                ((ValidatorRule) rule).setKey(key);
            }
            check.withMethod(rule);
        }
        return this;
    }

    public ValidatorCheckBuilder decorate(Node target, Function<ValidationMessage, Decoration> factory) {
        check.decorates(target)
                .decoratingWith(factory);
        return this;
    }

    public Check build() {
        return check;
    }
}
