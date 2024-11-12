package rahulstech.jfx.balancesheet.frontend.controller;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import rahulstech.jfx.balancesheet.core.application.Context;

public interface Controller extends Context {

    ObjectProperty<Node> rootNodeProperty();

    void setRootNode(Node root);

    Node getRootNode();
}
