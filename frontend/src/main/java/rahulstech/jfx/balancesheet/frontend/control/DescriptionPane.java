package rahulstech.jfx.balancesheet.frontend.control;

import javafx.beans.DefaultProperty;
import javafx.beans.property.*;
import javafx.css.*;
import javafx.css.converter.SizeConverter;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import rahulstech.jfx.balancesheet.frontend.control.skin.DescriptionPaneSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DefaultProperty("content")
public class DescriptionPane extends Control {

    private static final String DEFAULT_STYLE_CLASS = "rst-description-pane";

    private static final double VGAP = 4.0;

    private static final PseudoClass PSEUDO_CLASS_ERROR = PseudoClass.getPseudoClass("error");

    public DescriptionPane() {
        super();
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DescriptionPaneSkin(this);
    }

    /*****************************************
     *              Properties               *
     *****************************************/

    private final StringProperty labelText = new SimpleStringProperty(this,"labelText");

    public final StringProperty labelTextProperty() {
        return labelText;
    }

    public final String getLabelText() {
        return labelText.get();
    }

    public void setLabelText(String newLabelText) {
        labelText.set(newLabelText);
    }

    private final ObjectProperty<Node> labelTextNode = new SimpleObjectProperty<>(this,"labelTextNode");

    public final ObjectProperty<Node> labelTextNodeProperty() {
        return labelTextNode;
    }

    public final Node getLabelTextNode() {
        return labelTextNode.get();
    }

    public final void setLabelTextNode(Node node) {
        labelTextNode.set(node);
    }

    private final ObjectProperty<Node> content = new SimpleObjectProperty<>(this,"content") {
        @Override
        protected void invalidated() {
            getChildren().add(get());
        }
    };

    public final ObjectProperty<Node> contentProperty() {
        return content;
    }

    public final Node getContent() {
        return content.get();
    }

    public final void setContent(Node contentNode) {
        content.set(contentNode);
    }

    private final StringProperty descriptionText = new SimpleStringProperty(this,"descriptionText");

    public final StringProperty descriptionTextProperty() {
        return descriptionText;
    }

    public final String getDescriptionText() {
        return descriptionText.get();
    }

    public final void setDescriptionText(String text) {
        descriptionText.set(text);
    }

    private final StringProperty errorText = new SimpleStringProperty(this,"errorText");

    public final StringProperty errorTextProperty() {
        return errorText;
    }

    public final String getErrorText() {
        return errorText.get();
    }

    public final void setErrorText(String text) {
        errorText.set(text);
    }

    private final ObjectProperty<Node> supportingTextNode = new SimpleObjectProperty<>(this,"supportingTextNode");

    public final ObjectProperty<Node> supportingTextNodeProperty() {
        return supportingTextNode;
    }

    public final Node getSupportingTextNode() {
        return supportingTextNode.get();
    }

    public final void setSupportingTextNode(Node node) {
        supportingTextNode.set(node);
    }

    private final BooleanProperty error = new SimpleBooleanProperty(this,"error", false) {
        @Override
        protected void invalidated() {
            boolean hasError = get();
            pseudoClassStateChanged(PSEUDO_CLASS_ERROR,hasError);
        }
    };

    public final BooleanProperty errorProperty() {
        return error;
    }

    public final boolean getError() {
        return error.get();
    }

    public final void setError(boolean hasError) {
        error.set(hasError);
    }

    private final DoubleProperty contentLabelTextGap = new SimpleStyleableDoubleProperty(CONTENT_LABEL_TEXT_GAP, this,"contentLabelTextGap",VGAP);

    public final DoubleProperty contentLabelTextGapProperty() {
        return contentLabelTextGap;
    }

    public final double getContentLabelTextGap() {
        return contentLabelTextGap.get();
    }

    public final void setContentLabelTextGap(double gap) {
        contentLabelTextGap.set(gap);
    }

    private final DoubleProperty contentSupportingTextGap = new SimpleStyleableDoubleProperty(CONTENT_SUPPORTING_TEXT_GAP, this, "contentSupportingTextGap", VGAP);

    public final DoubleProperty contentSupportingTextGapProperty() {
        return contentSupportingTextGap;
    }

    public final double getContentSupportingTextGap() {
        return contentSupportingTextGap.get();
    }

    public final void setContentSupportingTextGap(double gap) {
        contentSupportingTextGap.set(gap);
    }


    /*****************************************
     *            CSS Properties             *
     *****************************************/

    private static final CssMetaData<DescriptionPane, Number> CONTENT_SUPPORTING_TEXT_GAP = new CssMetaData<>("-rst-content-supporting-text-gap", SizeConverter.getInstance()) {
        @Override
        public boolean isSettable(DescriptionPane styleable) {
            return !styleable.contentLabelTextGapProperty().isBound();
        }

        @Override
        public StyleableProperty<Number> getStyleableProperty(DescriptionPane styleable) {
            return (StyleableDoubleProperty) styleable.contentLabelTextGapProperty();
        }
    };

    private static final CssMetaData<DescriptionPane, Number> CONTENT_LABEL_TEXT_GAP = new CssMetaData<>("-rst-content-label-text-gap", SizeConverter.getInstance()) {
        @Override
        public boolean isSettable(DescriptionPane styleable) {
            return !styleable.contentSupportingTextGapProperty().isBound();
        }

        @Override
        public StyleableProperty<Number> getStyleableProperty(DescriptionPane styleable) {
            return (StyleableDoubleProperty) styleable.contentSupportingTextGapProperty();
        }
    };

    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLE;

    static  {
        List<CssMetaData<? extends Styleable, ?>> styleable = new ArrayList<>(Control.getClassCssMetaData());
        styleable.add(CONTENT_LABEL_TEXT_GAP);
        styleable.add(CONTENT_SUPPORTING_TEXT_GAP);
        STYLEABLE = Collections.unmodifiableList(styleable);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLE;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
