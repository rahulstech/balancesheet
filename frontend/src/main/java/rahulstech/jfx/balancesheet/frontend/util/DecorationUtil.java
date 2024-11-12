package rahulstech.jfx.balancesheet.frontend.util;

import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DecorationUtil {

    private static final String TAG = DecorationUtil.class.getSimpleName();

    private static final String KEY_LABEL = "rahulstech_decoration_label";

    private static final String KEY_SUPPORTING_TEXT = "rahulstech_decoration_supporting_text";

    private DecorationUtil() {}

    public static void initTextField(TextField textfield, Node label, Node supportingText) {
        init(textfield,label,supportingText);
    }

    public static void initDatePicker(DatePicker datepicker, Node label, Node supportingText) {
        init(datepicker,label,supportingText);
    }

    public static void initComboBox(ComboBox<?> combobox, Node label, Node supportingText) {
        init(combobox,label,supportingText);
    }

    public static void initToggleGroup(ToggleGroup group, Node label, Node supportingText) {
        if (null != label) {
            group.getProperties().put(KEY_LABEL,label);
        }
        if (null != supportingText) {
            group.getProperties().put(KEY_SUPPORTING_TEXT,supportingText);
        }
    }

    private static void init(Node node, Node label, Node supportingText) {
        if (null != label) {
            node.getProperties().put(KEY_LABEL,label);
        }
        if (null != supportingText) {
            node.getProperties().put(KEY_SUPPORTING_TEXT,supportingText);
        }
    }

    public static void error(Node node) {
        PseudoClass ERROR = PseudoClasses.ERROR;
        node.pseudoClassStateChanged(ERROR,true);
        Node label = (Node) node.getProperties().get(KEY_LABEL);
        Node supportingText = getSupportText(node);
        if (null != label) {
            label.pseudoClassStateChanged(ERROR,true);
        }
        if (null != supportingText) {
            supportingText.pseudoClassStateChanged(ERROR,true);
        }
    }

    public static void error(ToggleGroup group) {
        PseudoClass ERROR = PseudoClasses.ERROR;
        Node label = (Node) group.getProperties().get(KEY_LABEL);
        Node supportingText = (Node) group.getProperties().get(KEY_SUPPORTING_TEXT);
        if (null != label) {
            label.pseudoClassStateChanged(ERROR,true);
        }
        if (null != supportingText) {
            supportingText.pseudoClassStateChanged(ERROR,true);
        }
    }

    public static void removeError(Node node) {
        PseudoClass ERROR = PseudoClasses.ERROR;
        node.pseudoClassStateChanged(ERROR,false);
        Node label = (Node) node.getProperties().get(KEY_LABEL);
        Node supportingText = (Node) node.getProperties().get(KEY_SUPPORTING_TEXT);
        if (null != label) {
            label.pseudoClassStateChanged(ERROR,false);
        }
        if (null != supportingText) {
            supportingText.pseudoClassStateChanged(ERROR,false);
        }
    }

    public static void removeError(ToggleGroup group) {
        PseudoClass ERROR = PseudoClasses.ERROR;
        Node label = (Node) group.getProperties().get(KEY_LABEL);
        Node supportingText = getSupportText(group);
        if (null != label) {
            label.pseudoClassStateChanged(ERROR,false);
        }
        if (null != supportingText) {
            supportingText.pseudoClassStateChanged(ERROR,false);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T getSupportText(Node node) {
        return (T) node.getProperties().get(KEY_SUPPORTING_TEXT);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T getSupportText(ToggleGroup group) {
        return (T) group.getProperties().get(KEY_SUPPORTING_TEXT);
    }

    public static void setSupportingTextText(Node node, String text) {
        Node supportingTextNode = getSupportText(node);
        setTextToSupportTextNode(supportingTextNode,text);
    }

    public static void setSupportingTextText(ToggleGroup group, String text) {
        Node supportingTextNode = getSupportText(group);
        setTextToSupportTextNode(supportingTextNode,text);
    }

    private static void setTextToSupportTextNode(Node supportingTextNode, String text) {
        if (supportingTextNode instanceof Labeled) {
            ((Labeled) supportingTextNode).setText(text);
        }
        else if (supportingTextNode instanceof Text) {
            ((Text) supportingTextNode).setText(text);
        }
    }
}
