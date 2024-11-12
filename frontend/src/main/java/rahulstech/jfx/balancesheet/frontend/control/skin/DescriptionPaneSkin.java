package rahulstech.jfx.balancesheet.frontend.control.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

import rahulstech.jfx.balancesheet.core.util.TextUtil;
import rahulstech.jfx.balancesheet.frontend.control.DescriptionPane;

public class DescriptionPaneSkin extends SkinBase<DescriptionPane> {

    private static final String STYLE_CLASS_LABEL_TEXT = "rst-label-text";

    private static final String STYLE_CLASS_SUPPORTING_TEXT = "rst-supporting-text";

    private static final PseudoClass PSEUDO_CLASS_ERROR = PseudoClass.getPseudoClass("error");

    private Node labelTextNode;

    private Node supportingTextNode;

    private final ObjectBinding<Node> labelTextNodeBinding;

    private final ObjectBinding<Node> supportingTextNodeBiding;

    public DescriptionPaneSkin(DescriptionPane control) {
        super(control);

        labelTextNodeBinding = Bindings.createObjectBinding(this::getOrCreateLabelTextNode,
                control.labelTextProperty(), control.labelTextNodeProperty());

        supportingTextNodeBiding = Bindings.createObjectBinding(this::getOrCreateSupportingTextNode,
                control.descriptionTextProperty(), control.errorTextProperty(), control.supportingTextNodeProperty());

        registerChangeListener(control.contentProperty(), value -> updateChildren());
        registerChangeListener(control.contentLabelTextGapProperty(), value -> control.requestLayout());
        registerChangeListener(control.contentSupportingTextGapProperty(), value -> control.requestLayout());
//        registerChangeListener(control.labelTextProperty(), value -> updateLabelTextNode());
//        registerChangeListener(control.labelTextNodeProperty(), value -> updateLabelTextNode());
//        registerChangeListener(control.descriptionTextProperty(), value -> updateSupportingTextNode());
//        registerChangeListener(control.errorTextProperty(), value -> updateSupportingTextNode());
//        registerChangeListener(control.supportingTextNodeProperty(), value -> updateSupportingTextNode());
        registerChangeListener(control.errorProperty(), value -> updateChildNodeErrorState());

        registerChangeListener(labelTextNodeBinding, value -> updateLabelTextNode());
        registerChangeListener(supportingTextNodeBiding, value -> updateSupportingTextNode());

        updateChildren();
    }

    @Override
    public void dispose() {
        DescriptionPane control = getSkinnable();
        unregisterChangeListeners(control.contentProperty());
        unregisterChangeListeners(control.contentLabelTextGapProperty());
        unregisterChangeListeners(control.contentSupportingTextGapProperty());
        unregisterChangeListeners(control.labelTextProperty());
        unregisterChangeListeners(control.labelTextNodeProperty());
        unregisterChangeListeners(control.descriptionTextProperty());
        unregisterChangeListeners(control.errorTextProperty());
        unregisterChangeListeners(control.supportingTextNodeProperty());
        unregisterChangeListeners(control.errorProperty());
        super.dispose();
    }

    private void updateChildren() {
        DescriptionPane control = getSkinnable();
        Node contentNode = control.getContent();

        getChildren().clear();
        if (null == contentNode) {
            return;
        }
        getChildren().add(contentNode);

        Node labelTextNode = getOrCreateLabelTextNode();
        replaceChildNode(this.labelTextNode, labelTextNode);
        this.labelTextNode = labelTextNode;

        Node supportingTextNode = getOrCreateSupportingTextNode();
        replaceChildNode(this.supportingTextNode, supportingTextNode);
        this.supportingTextNode = supportingTextNode;
    }

    private void updateLabelTextNode() {
        Node labelTextNode = labelTextNodeBinding.get();
        replaceChildNode(this.labelTextNode, labelTextNode);
        this.labelTextNode = labelTextNode;
        getSkinnable().requestLayout();
    }

    private void updateSupportingTextNode() {
        Node supportingTextNode = supportingTextNodeBiding.get();
        replaceChildNode(this.supportingTextNode, supportingTextNode);
        this.supportingTextNode = supportingTextNode;
        getSkinnable().requestLayout();
    }

    private Node getOrCreateLabelTextNode() {
        DescriptionPane control = getSkinnable();
        Node controlLabelTextNode = control.getLabelTextNode();
        Node labelTextNode = null == controlLabelTextNode ? createLabelTextNode(control) : controlLabelTextNode;
        if (null != labelTextNode) {
            labelTextNode.getStyleClass().remove(STYLE_CLASS_LABEL_TEXT);
            labelTextNode.getStyleClass().add(STYLE_CLASS_LABEL_TEXT);
        }
        return labelTextNode;
    }

    private Label createLabelTextNode(DescriptionPane control) {
        String labelText = control.getLabelText();
        if (TextUtil.isEmpty(labelText)) {
            return null;
        }
        Label label = new Label();
        label.setText(labelText);
        return label;
    }

    private Node getOrCreateSupportingTextNode() {
        DescriptionPane control = getSkinnable();
        Node controlSupportingTextNode = control.getSupportingTextNode();
        Node supportingTextNode = null == controlSupportingTextNode ? createSupportingTextNode(control) : controlSupportingTextNode;
        if (null != supportingTextNode) {
            supportingTextNode.getStyleClass().remove(STYLE_CLASS_SUPPORTING_TEXT);
            supportingTextNode.getStyleClass().add(STYLE_CLASS_SUPPORTING_TEXT);
            supportingTextNode.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, control.getError());
        }
        return supportingTextNode;
    }

    private Node createSupportingTextNode(DescriptionPane control) {
        String text = control.getError() ? control.getErrorText() : control.getDescriptionText();
        if (!TextUtil.isEmpty(text)) {
            Label label = new Label(text);
            label.setWrapText(true);
            return label;
        }
        return null;
    }

    private void replaceChildNode(Node oldNode, Node newNode) {
        if (null != oldNode) getChildren().remove(oldNode);
        if (null != newNode) getChildren().add(newNode);
    }

    private void updateChildNodeErrorState() {
        DescriptionPane control = getSkinnable();
        Node contentNode = control.getContent();
        Node labelTextNode = this.labelTextNode;
        Node supportingTextNode = this.supportingTextNode;
        boolean hasError = control.getError();

        if (null == contentNode) {
            return;
        }

        contentNode.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, hasError);
        if (null != labelTextNode) {
            labelTextNode.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, hasError);
        }
        if (null != supportingTextNode) {
            supportingTextNode.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, hasError);
        }
    }

    /*****************************************
     *       Measurement and Layout          *
     *****************************************/

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        Node contentNode = getSkinnable().getContent();
        if (null == contentNode) {
            return topInset + bottomInset;
        }
        return topInset + snapSizeY(contentNode.minHeight(-1)) + bottomInset;
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        Node contentNode = getSkinnable().getContent();
        if (null == contentNode) {
            return leftInset + rightInset;
        }
        return topInset + snapSizeX(contentNode.minWidth(-1)) + bottomInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        DescriptionPane control = getSkinnable();
        Node contentNode = control.getContent();
        if (null == contentNode) {
            return topInset + bottomInset;
        }

        double contentNodeWidth = Math.min(snapSizeX(contentNode.prefWidth(-1)), width);
        double height = contentNode.prefHeight(width);

        if (labelTextNode != null) {
            height += labelTextNode.prefHeight(contentNodeWidth) + control.getContentLabelTextGap();
        }
        if (supportingTextNode != null) {
            height += supportingTextNode.prefHeight(contentNodeWidth) + control.getContentSupportingTextGap();
        }
        return topInset + height + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        DescriptionPane control = getSkinnable();
        Node contentNode = control.getContent();
        if (null == contentNode) {
            return leftInset + rightInset;
        }
        return leftInset + snapSizeX(contentNode.prefWidth(height)) + rightInset;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width,topInset,rightInset,bottomInset,leftInset);
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        DescriptionPane control = getSkinnable();
        Node contentNode = control.getContent();
        if (null == contentNode) {
            return;
        }

        double contentNodeHeight =  snapSizeY(contentNode.prefHeight(contentWidth));
        double contentNodeWidth = snapSizeX(contentNode.prefWidth(contentNodeHeight));
        double labelTextNodeHeight = null == labelTextNode ? 0 : snapSizeY(labelTextNode.prefHeight(contentNodeWidth));
        double supportingTextNodeHeight = null == supportingTextNode ? 0 : snapSizeY(supportingTextNode.prefHeight(contentNodeWidth));

        if (null != labelTextNode) {
            layoutInArea(labelTextNode, contentX, contentY, contentNodeWidth, labelTextNodeHeight,0, HPos.LEFT, VPos.TOP);
            contentY += labelTextNodeHeight + control.getContentLabelTextGap();
        }

        layoutInArea(contentNode, contentX, contentY, contentNodeWidth,contentNodeHeight, 0, HPos.LEFT,VPos.CENTER);
        contentY += contentNodeHeight;

        if (null != supportingTextNode) {
            contentY += control.getContentSupportingTextGap();
            layoutInArea(supportingTextNode,contentX,contentY,contentNodeWidth,supportingTextNodeHeight,0,HPos.LEFT,VPos.BOTTOM);
        }
    }
}
