package rahulstech.jfx.balancesheet.frontend.control.skin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import rahulstech.jfx.balancesheet.core.util.TextUtil;
import rahulstech.jfx.balancesheet.frontend.control.ButtonRole;
import rahulstech.jfx.balancesheet.frontend.control.MaterialTextField;

public class MaterialTextFieldSkin extends TextFieldSkin {

    private static final double HGAP = 16.0;

    private static final double VGAP = 4.0;

    private Node leadingIcon;

    private Node trailingIcon;

    public MaterialTextFieldSkin(MaterialTextField control) {
        super(control);

        // Add listeners to properties
        registerChangeListener(control.leadingIconProperty(), value -> updateLeadingIcon());
        registerChangeListener(control.trailingIconProperty(), value -> updateTrailingIcon());
        registerChangeListener(control.trailingIconRoleProperty(), value -> updateIconRole(trailingIcon, control.getTrailingIconRole()));

        updateChildren(); // Initial children setup

        control.textProperty().subscribe(text -> {
            if (null == trailingIcon || control.getTrailingIconRole() != ButtonRole.CANCEL_CLEAR) {
                return;
            }
            trailingIcon.setVisible(!TextUtil.isEmpty(text));
        });
    }

    @Override
    public void dispose() {
        MaterialTextField control = (MaterialTextField) getSkinnable();

        unregisterChangeListeners(control.leadingIconProperty());
        unregisterChangeListeners(control.trailingIconProperty());
        unregisterChangeListeners(control.trailingIconRoleProperty());

        super.dispose();

        leadingIcon = null;
        trailingIcon = null;
    }

    public MaterialTextField getMaterialTextFieldSkinnable() {
        return (MaterialTextField) getSkinnable();
    }

    // Update icons and layout children
    protected void updateChildren() {
        MaterialTextField control = getMaterialTextFieldSkinnable();

        getChildren().removeAll(leadingIcon, trailingIcon);

        leadingIcon = setupIcon(control.getLeadingIcon(), "rst-leading-icon");
        trailingIcon = setupIcon(control.getTrailingIcon(), "rst-trailing-icon");
        updateIconRole(trailingIcon, control.getTrailingIconRole());

        if (leadingIcon != null) getChildren().add(leadingIcon);
        if (trailingIcon != null) getChildren().add(trailingIcon);
    }

    private Node setupIcon(Node icon, String styleClass) {
        if (icon != null) {
            icon.getStyleClass().remove(styleClass);
            icon.getStyleClass().add(styleClass);
        }
        return icon;
    }

    private void updateLeadingIcon() {
        MaterialTextField control = getMaterialTextFieldSkinnable();
        leadingIcon = setupIcon(control.getLeadingIcon(), "rst-leading-icon");
        control.requestLayout();
    }

    private void updateTrailingIcon() {
        MaterialTextField control = getMaterialTextFieldSkinnable();
        trailingIcon = setupIcon(control.getTrailingIcon(), "rst-trailing-icon");
        updateIconRole(trailingIcon, control.getTrailingIconRole());
        control.requestLayout();
    }

    private void updateIconRole(Node icon, ButtonRole role) {
        if (icon != null) {
            icon.setCursor(role == ButtonRole.CANCEL_CLEAR || role == ButtonRole.CUSTOM ? Cursor.DEFAULT : Cursor.TEXT);
            icon.setOnMouseClicked(role == ButtonRole.CANCEL_CLEAR || role == ButtonRole.CUSTOM
                    ? createIconClickHandler(role) : null);
            // TODO: do i need to unregister so many times
            if (role == ButtonRole.CANCEL_CLEAR) {
                registerChangeListener(getSkinnable().textProperty(), value -> {
                    Node target = getMaterialTextFieldSkinnable().getTrailingIcon();
                    if (null == target) return;
                    target.setVisible(!TextUtil.isEmpty((String) value.getValue()));
                });
            }
            else {
                unregisterChangeListeners(getSkinnable().textProperty());
            }
        }
        else {
            unregisterChangeListeners(getSkinnable().textProperty());
        }
    }

    private EventHandler<MouseEvent> createIconClickHandler(ButtonRole role) {
        MaterialTextField control = (MaterialTextField) getSkinnable();
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                if (role == ButtonRole.CANCEL_CLEAR) {
                    control.clear();
                }
                else if (role == ButtonRole.CUSTOM && control.getOnTrailingIconAction() != null) {
                    control.getOnTrailingIconAction().handle(new ActionEvent(control, control));
                }
            }
        };
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double leadingIconWidth = snapSizeX(calculatePrefWidth(leadingIcon));
        double trailingIconWidth = snapSizeX(calculatePrefWidth(trailingIcon));
        double leadingSpace = leadingIconWidth > 0 ? leadingIconWidth + HGAP : 0;
        double trailingSpace = trailingIconWidth > 0 ? trailingIconWidth + HGAP : 0;
        return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset)
                + leadingSpace + trailingSpace;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double leadingIconWidth = snapSizeX(calculatePrefWidth(leadingIcon));
        double trailingIconWidth = snapSizeX(calculatePrefWidth(trailingIcon));
        double leadingSpace = leadingIconWidth > 0 ? leadingIconWidth + HGAP : 0;
        double trailingSpace = trailingIconWidth > 0 ? trailingIconWidth + HGAP : 0;
        double contentWidth = width - leadingSpace - trailingSpace;
        return super.computePrefHeight(contentWidth, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        double leadingIconWidth = snapSizeX(calculatePrefWidth(leadingIcon));
        double trailingIconWidth = snapSizeX(calculatePrefWidth(trailingIcon));
        double leadingIconHeight = snapSizeY(calculatePrefHeight(leadingIcon));
        double trailingIconHeight = snapSizeY(calculatePrefHeight(trailingIcon));
        double leadingSpace = leadingIconWidth > 0 ? leadingIconWidth + HGAP : 0;
        double trailingSpace = trailingIconWidth > 0 ? trailingIconWidth + HGAP : 0;

        super.layoutChildren(x + leadingSpace, y, w - leadingSpace - trailingSpace, h);
        if (leadingIcon != null) {
            leadingIcon.resize(leadingIconWidth, leadingIconHeight);
            layoutInArea(leadingIcon, x, y + (h - leadingIconHeight) / 2,
                    leadingIconWidth, leadingIconHeight, 0, HPos.CENTER, VPos.CENTER);
        }
        if (trailingIcon != null) {
            trailingIcon.resize(trailingIconWidth,trailingIconHeight);
            layoutInArea(trailingIcon, x + w - trailingIconWidth, y + (h - trailingIconHeight) / 2,
                    trailingIconWidth, trailingIconHeight, 0, HPos.CENTER, VPos.CENTER);
        }
    }

    private double calculatePrefWidth(Node node) {
        return null == node ? 0 : node.prefWidth(-1);
    }

    private double calculatePrefHeight(Node node) {
        return null == node ? 0 : node.prefHeight(-1);
    }
}
