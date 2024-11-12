package rahulstech.jfx.balancesheet.frontend.control.skin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.skin.LabeledSkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import rahulstech.jfx.balancesheet.frontend.control.ButtonRole;
import rahulstech.jfx.balancesheet.frontend.control.Chip;

public class ChipSkin extends LabeledSkinBase<Chip> {

    private static final double HGAP = 8;

    private Node leadingIcon;
    private Node trailingIcon;

    public ChipSkin(Chip control) {
        super(control);

        // Register listeners for property changes
        registerChangeListener(control.leadingIconProperty(), value -> updateLeadingIcon());
        registerChangeListener(control.trailingIconProperty(), value -> updateTrailingIcon());
        registerChangeListener(control.trailingIconRoleProperty(), value -> updateIconRole(trailingIcon, control.getTrailingIconRole()));
        registerChangeListener(control.selectedProperty(), value -> getSkinnable().requestLayout());

        updateChildren();
    }

    /************************************
     *             API Methods          *
     ************************************/

    @Override
    protected void updateChildren() {
        super.updateChildren();

        Chip control = (Chip) getSkinnable();

        // Remove existing icons from children
        getChildren().removeAll(leadingIcon, trailingIcon);

        // Initialize and add icons to children
        leadingIcon = setupIcon(control.getLeadingIcon(), "rst-leading-icon");
        trailingIcon = setupIcon(control.getTrailingIcon(), "rst-trailing-icon");
        updateIconRole(trailingIcon, control.getTrailingIconRole());

        if (leadingIcon != null) getChildren().add(leadingIcon);
        if (trailingIcon != null) getChildren().add(trailingIcon);
    }

    @Override
    public void dispose() {
        Chip control = (Chip) getSkinnable();

        // Unregister listeners
        unregisterChangeListeners(control.leadingIconProperty());
        unregisterChangeListeners(control.trailingIconProperty());
        unregisterChangeListeners(control.trailingIconRoleProperty());
        unregisterChangeListeners(control.selectedProperty());

        super.dispose();

        leadingIcon = null;
        trailingIcon = null;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double leadingIconWidth = null == leadingIcon ? 0 : leadingIcon.prefWidth(-1);
        double trailingIconWidth = null == trailingIcon ? 0 : trailingIcon.prefWidth(-1);

        return super.computePrefWidth(height,topInset,rightInset,bottomInset,leftInset)
                + snapSizeX(leadingIconWidth) + snapSizeX(trailingIconWidth) + 2*HGAP;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double leadingIconWidth = !canShowLeadingIcon() ? 0 : calculateIconWidth(leadingIcon);
        double leadingIconHeight = !canShowLeadingIcon() ? 0 : calculateIconHeight(leadingIcon);
        double trailingIconWidth = calculateIconWidth(trailingIcon);
        double trailingIconHeight = calculateIconHeight(trailingIcon);
        double leadingGap = leadingIconWidth > 0 ? Math.max(snapSizeX(leftInset/2), HGAP) : 0;
        double trailingGap = trailingIconWidth > 0 ? Math.max(snapSizeX(rightInset/2), HGAP) : 0;

        double contentWidth = super.computePrefHeight(width - leadingIconWidth - leadingGap - trailingIconWidth - trailingGap,
                topInset, rightInset, bottomInset, leftInset);

        return Math.max(contentWidth, topInset + Math.max(leadingIconHeight, trailingIconHeight) + bottomInset);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        Chip chip = (Chip) getSkinnable();
        double leadingIconWidth = canShowLeadingIcon() ? snapSizeX(calculateIconWidth(leadingIcon)) : 0;
        double leadingIconHeight = canShowLeadingIcon() ? snapSizeY(calculateIconHeight(leadingIcon)) : 0;
        double trailingIconWidth = snapSizeX(calculateIconWidth(trailingIcon));
        double trailingIconHeight = snapSizeY(calculateIconHeight(trailingIcon));
        double leadingGap = leadingIconWidth > 0 ? calculateTextIconGap(snappedLeftInset()) : 0;
        double trailingGap = trailingIconWidth > 0 ? calculateTextIconGap(snappedRightInset()) : 0;
        double leadingSpace = leadingIconWidth + leadingGap;
        double trailingSpace = trailingIconWidth + trailingGap;
        double computeWidth = Math.max(chip.prefWidth(-1), chip.minWidth(-1));
        double labelWidth = Math.min(computeWidth, w) - leadingSpace - trailingSpace;
        double labelHeight = Math.min(chip.prefHeight(labelWidth), h);
        double maxHeight = Math.max(Math.max(leadingIconHeight,trailingIconHeight), labelHeight);
        double xOffset = computeXOffset(w, labelWidth + leadingSpace + trailingSpace, leadingGap, chip.getAlignment().getHpos()) + x;
        double yOffset = computeYOffset(h, maxHeight, chip.getAlignment().getVpos()) + y;

        layoutLabelInArea(xOffset+leadingSpace, yOffset, labelWidth, maxHeight, chip.getAlignment());
        if (null != leadingIcon) {
            leadingIcon.resize(leadingIconWidth,leadingIconHeight);
            positionInArea(leadingIcon,xOffset,yOffset,leadingIconWidth,maxHeight,0,chip.getAlignment().getHpos(),chip.getAlignment().getVpos());
        }
        if (null != trailingIcon) {
            trailingIcon.resize(trailingIconWidth,trailingIconHeight);
            positionInArea(trailingIcon,xOffset+leadingSpace+labelWidth+trailingGap,yOffset,trailingIconWidth,maxHeight,0,chip.getAlignment().getHpos(),chip.getAlignment().getVpos());
        }
    }

    /************************************
     *     Private Helper Methods       *
     ************************************/

    private void updateLeadingIcon() {
        Chip control = (Chip) getSkinnable();
        if (leadingIcon != control.getLeadingIcon()) {
            leadingIcon = setupIcon(control.getLeadingIcon(), "rst-leading-icon");
        }
        control.requestLayout();
    }

    private void updateTrailingIcon() {
        Chip control = (Chip) getSkinnable();
        if (trailingIcon != control.getTrailingIcon()) {
            trailingIcon = setupIcon(control.getTrailingIcon(), "rst-trailing-icon");
        }
        updateIconRole(trailingIcon, control.getTrailingIconRole());
        control.requestLayout();
    }

    private Node setupIcon(Node node, String styleClass) {
        if (node == null) return null;

        node.getStyleClass().add(styleClass);
        return node;
    }

    private void updateIconRole(Node icon, ButtonRole role) {
        if (icon != null) {
            icon.setCursor(role == ButtonRole.CANCEL_CLEAR || role == ButtonRole.CUSTOM ? Cursor.DEFAULT : Cursor.TEXT);
//            icon.addEventHandler(MouseEvent.MOUSE_CLICKED,(role == ButtonRole.CANCEL_CLEAR || role == ButtonRole.CUSTOM)
//                    ? createIconClickHandler(role) : null);
        }
    }

    private EventHandler<MouseEvent> createIconClickHandler(ButtonRole role) {
        Chip control = (Chip) getSkinnable();
        return event -> {
            System.out.println("mouse event on trailing button observed "+event);
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                if (role == ButtonRole.CANCEL_CLEAR) {
                    control.hide();
                }
                else if (role == ButtonRole.CUSTOM && control.getOnTrailingIconAction() != null) {
                    control.getOnTrailingIconAction().handle(new ActionEvent(control, null));
                }
            }
        };
    }

    private boolean canShowLeadingIcon() {
        Chip control = (Chip) getSkinnable();
        if (control.getChipType() == Chip.ChipType.FILTER) {
            return control.isSelected();
        }
        return true;
    }

    private double calculateIconWidth(Node node) {
        return null == node ? 0 : node.prefWidth(-1);
    }

    private double calculateIconHeight(Node node) {
        return null == node ? 0 : node.prefHeight(-1);
    }

    private double calculateTextIconGap(double inset) {
        return Math.max(snapSizeX(inset/2), HGAP);
    }

    private double computeXOffset(double width, double contentWidth, double leadingGap, HPos hpos) {
        if (hpos == null) {
            return 0;
        }

        switch(hpos) {
            case LEFT:
                return -leadingGap;
            case CENTER:
                return (width - contentWidth) / 2;
            case RIGHT:
                return width - contentWidth + leadingGap;
            default:
                return 0;
        }
    }

    private double computeYOffset(double height, double contentHeight, VPos vpos) {
        if (vpos == null) {
            return 0;
        }

        switch (vpos) {
            case TOP:
                return 0;
            case CENTER:
                return (height - contentHeight) / 2;
            case BOTTOM:
                return height - contentHeight;
            default:
                return 0;
        }
    }
}
