package rahulstech.jfx.balancesheet.frontend.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class ToggleUtil {

    public static ToggleGroup exactlyOne(Toggle selected, Toggle... toggles) {
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(toggles);
        if (null != selected) {
            group.selectToggle(selected);
        }
        group.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            if (newToggle == null) {
                group.selectToggle(oldToggle);
            }
        });
        return group;
    }

    public static void selectNone(ToggleGroup group) {
        Toggle selected = group.getSelectedToggle();
        ObservableList<Toggle> copy = FXCollections.observableList(group.getToggles().stream().toList());
        group.getToggles().clear();
        group.selectToggle(null);
        if (null != selected) {
            selected.setSelected(false);
        }
        group.getToggles().addAll(copy);
    }


}
