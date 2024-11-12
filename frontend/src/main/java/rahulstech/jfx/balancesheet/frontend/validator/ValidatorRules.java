package rahulstech.jfx.balancesheet.frontend.validator;

import net.synedra.validatorfx.Check;
import rahulstech.jfx.balancesheet.core.application.Context;

import java.util.function.Consumer;

public class ValidatorRules {

    public static ValidatorRule nonNull(String message) {
        return new SimpleValidatorRule() {
            @Override
            public void accept(Check.Context context) {
                if (null == getValue(context)) {
                    context.error(message);
                }
            }
        };
    }

    public static ValidatorRule nonEmptyString(String message) {
        return new SimpleValidatorRule() {
            @Override
            public void accept(Check.Context context) {
                String value = getValue(context);
                if (null == value || value.isBlank()) {
                    context.error(message);
                }
            }
        };
    }

    private abstract static class SimpleValidatorRule implements ValidatorRule {

        private String key;

        @Override
        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }

        public <T> T getValue(Check.Context context) {
            return context.get(getKey());
        }
    }
}
