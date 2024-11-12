package rahulstech.jfx.balancesheet.frontend.routing;

import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.routing.BaseRouterContext;

import java.io.InputStream;
import java.net.URL;
import java.util.stream.Stream;

public class ContextAwareRouterContext extends BaseRouterContext {

    private final Context context;

    public ContextAwareRouterContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public InputStream getRouterConfigurationAsStrem(String name) {
        return context.getResources().getConfigStream(name);
    }

    @Override
    public URL getResource(String name, String type) {
        return switch (type) {
            case "fxml" -> context.getResources().getLayout(name);
            default -> context.getResourceUrl(name);
        };
    }

    @Override
    public InputStream getResourceAsStream(String name, String type) {
        return context.getResourceStream(name);
    }

    @Override
    public Object newControllerInstance(Class<?> clazz, Object... args) {
        Object[] _args = Stream.concat(Stream.of(context),Stream.of(args)).toArray();
        return super.newControllerInstance(clazz, _args);
    }
}
