package rahulstech.jfx.balancesheet.frontend.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.application.ContextImpl;

public abstract class ControllerImpl extends ContextImpl implements Controller {

    protected ControllerImpl(Context parent) {
        super(parent);
    }

    private final ObjectProperty<Node> rootNode = new SimpleObjectProperty<>(this,"rootNode",null);

    @Override
    public ObjectProperty<Node> rootNodeProperty() {
        return rootNode;
    }

    @Override
    public void setRootNode(Node root) {
        Node oldRoot = rootNode.getValue();
        if (null != oldRoot) {
            oldRoot.sceneProperty().removeListener(onRootNodeSceneChangeListener);
        }

        rootNode.setValue(root);

        Node newRoot = rootNode.getValue();
        if (null != newRoot) {
            newRoot.sceneProperty().addListener(onRootNodeSceneChangeListener);
        }
    }

    @Override
    public Node getRootNode() {
        return rootNode.getValue();
    }

    private final ObjectProperty<Scene> scene = new SimpleObjectProperty<>(this,"scene",null);

    private final ChangeListener<Scene> onRootNodeSceneChangeListener = (ov,oldScene,newScene) -> setScene(newScene);

    public final ObjectProperty<Scene> sceneProperty() {
        return scene;
    }

    public void setScene(Scene scene) {
        Scene oldScene = this.scene.getValue();
        if (null != oldScene) {
            oldScene.windowProperty().addListener(onSceneWindowChangeListener);
        }

        this.scene.setValue(scene);

        Scene newScene = this.scene.getValue();
        if (null != newScene) {
            newScene.windowProperty().addListener(onSceneWindowChangeListener);
        }
    }

    public Scene getScene() {
        return scene.getValue();
    }

    private final ObjectProperty<Window> window = new SimpleObjectProperty<>(this,"window",null);

    private final ChangeListener<Window> onSceneWindowChangeListener = (ov,oldWindow,newWindow) -> setWindow(newWindow);

    public final ObjectProperty<Window> windowProperty() {
        return window;
    }

    public void setWindow(Window window) {
        this.window.setValue(window);
    }

    public Window getWindow() {
        return window.getValue();
    }
}
