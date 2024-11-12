package rahulstech.jfx.balancesheet.frontend.routing;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.frontend.controller.Controller;
import rahulstech.jfx.balancesheet.frontend.controller.ControllerImpl;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.lifecycle.LifecycleAwareController;

public class BaseRoutingController extends ControllerImpl implements Controller, LifecycleAwareController {

    private final ObjectProperty<Router> router = new SimpleObjectProperty<>(this,"router", null);

    public final ObjectProperty<Router> routerProperty() {
        return router;
    }

    public void setRouter(Router router) {
        this.router.setValue(router);
    }

    public Router getRouter() {
        return router.getValue();
    }

    @Override
    public final void setRoot(Node root) {
        setRootNode(root);
    }

    @Override
    public final Node getRoot() {
        return getRootNode();
    }

    public BaseRoutingController(Context parent) {
        super(parent);
    }

    @Override
    public void onLifecycleCreate() {}

    @Override
    public void onLifecycleInitialize() {}

    @Override
    public void onLifecycleShow() {}

    @Override
    public void onLifecycleHide() {}

    @Override
    public void onLifecycleDestroy() {}
}
