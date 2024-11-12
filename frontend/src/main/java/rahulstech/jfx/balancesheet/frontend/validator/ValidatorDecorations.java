package rahulstech.jfx.balancesheet.frontend.validator;

import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import net.synedra.validatorfx.Decoration;
import net.synedra.validatorfx.ValidationMessage;
import rahulstech.jfx.balancesheet.frontend.util.DecorationUtil;

import java.util.function.Function;

public class ValidatorDecorations {

    public static Function<ValidationMessage, Decoration> toggleGroupDecoration() {
        return message -> new Decoration() {
            @Override
            public void add(Node node) {
                ToggleGroup group = ((ValidatorDecorationNode) node).getTarget();
                DecorationUtil.error(group);
                DecorationUtil.setSupportingTextText(group,message.getText());
            }

            @Override
            public void remove(Node node) {
                ToggleGroup group = ((ValidatorDecorationNode) node).getTarget();
                DecorationUtil.removeError(group);
                DecorationUtil.setSupportingTextText(group,null);
            }
        };
    }
    
    public static Function<ValidationMessage, Decoration> textfieldDecoration() {
        return message -> new Decoration() {
            @Override
            public void add(Node node) {
                DecorationUtil.error(node);
                DecorationUtil.setSupportingTextText(node,message.getText());
            }

            @Override
            public void remove(Node node) {
                DecorationUtil.removeError(node);
                DecorationUtil.setSupportingTextText(node,null);
            }
        };
    }

    public static Function<ValidationMessage, Decoration> datePickerDecoration() {
        return message -> new Decoration() {
            @Override
            public void add(Node node) {
                DecorationUtil.error(node);
                DecorationUtil.setSupportingTextText(node,message.getText());
            }

            @Override
            public void remove(Node node) {
                DecorationUtil.removeError(node);
                DecorationUtil.setSupportingTextText(node,null);
            }
        };
    }
}
