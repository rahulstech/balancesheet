package rahulstech.jfx.balancesheet;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rahulstech.jfx.balancesheet.core.application.ApplicationContext;
import rahulstech.jfx.balancesheet.frontend.routing.ContextAwareRouterContext;
import rahulstech.jfx.balancesheet.frontend.util.Theme;
import rahulstech.jfx.routing.Router;

import java.io.InputStream;
import java.net.URL;

public class BalanceSheetApp extends ApplicationContext {

    public static final String DIR_DB = "./balancesheet/database";

    @Override
    protected void onStart(Stage stage) throws Exception {
        super.onStart(stage);

        ContextAwareRouterContext routerContext = new ContextAwareRouterContext(this);
        StackPane root = new StackPane();

        Router mainRouter = new Router(routerContext, root);
        mainRouter.parse(routerContext.getRouterConfigurationAsStrem("main_router.xml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Theme.getDefaultTheme());

        stage.setScene(scene);
        stage.setOnShown(e -> mainRouter.begin());
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public BalanceSheetApp getApplicationContext() {
        return this;
    }

    @Override
    public String getDatabaseDirPath() {
        return DIR_DB;
    }

    @Override
    public URL getResourceUrl(String path) {
        return BalanceSheetApp.class.getResource(path);
    }

    @Override
    public InputStream getResourceStream(String path) {
        return BalanceSheetApp.class.getResourceAsStream(path);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
