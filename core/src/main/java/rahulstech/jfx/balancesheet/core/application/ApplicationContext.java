package rahulstech.jfx.balancesheet.core.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

public abstract class ApplicationContext extends Application implements Context {

    public ExecutorService getIoExecutor() {
        return AppExecutor.getIOExecutor();
    }

    public ExecutorService getCpuExecutor() {
        return AppExecutor.getCpuExecutor();
    }

    private Stage appWindow;

    public Stage getAppWindow() {
        return appWindow;
    }

    @Override
    public final void start(Stage stage) throws Exception {
        appWindow = stage;
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        onStart(stage);
    }

    protected void onStart(Stage stage) throws Exception {}

    ////////////////////////////////////////////////////
    ///              Context Methods                ///
    //////////////////////////////////////////////////

    @Override
    public ApplicationContext getApplicationContext() {
        return this;
    }

    @Override
    public Context getParentContext() {
        return this;
    }
}
