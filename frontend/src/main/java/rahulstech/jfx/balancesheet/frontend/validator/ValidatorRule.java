package rahulstech.jfx.balancesheet.frontend.validator;


import net.synedra.validatorfx.Check;

import java.util.function.Consumer;

public interface ValidatorRule extends Consumer<Check.Context> {

    void setKey(String key);

    String getKey();
}
