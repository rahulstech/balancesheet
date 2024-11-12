package rahulstech.jfx.balancesheet.frontend.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.util.ReflectionUtil;

import java.io.IOException;
import java.net.URL;
import java.util.function.BiFunction;

public class FxmlLoaderBuilder {

    private static final BiFunction<Class<?>,Context,Controller> DEFAULT_CONTROLLER_FACTORY =
            (clazz,context) -> (Controller) ReflectionUtil.newInstance(clazz,new Object[]{context});

    private String layout;

    private Controller controller;

    private BiFunction<Class<?>,Context,Controller> controllerFactory = DEFAULT_CONTROLLER_FACTORY;

    private String bundleBaseName;

    public FxmlLoaderBuilder() {}

    public FxmlLoaderBuilder setLayout(String layout) {
        this.layout = layout;
        return this;
    }

    public FxmlLoaderBuilder addResourceBundle(String name) {
        this.bundleBaseName = name;
        return this;
    }

    public FxmlLoaderBuilder setController(Controller controller) {
        this.controller = controller;
        return this;
    }

    public FxmlLoaderBuilder setControllerFactory(BiFunction<Class<?>, Context, Controller> factory) {
        this.controllerFactory = factory == null ? DEFAULT_CONTROLLER_FACTORY : factory;
        return this;
    }

    public FXMLLoader build(Context context) {
        URL location = context.getResources().getLayout(layout);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setControllerFactory(clazz -> controllerFactory.apply(clazz,context));
        if (null != controller) {
            loader.setController(controller);
        }
        return loader;
    }

    public Controller load(Context context) throws IOException {
        FXMLLoader loader = build(context);
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setRootNode(root);
        return controller;
    }
}
