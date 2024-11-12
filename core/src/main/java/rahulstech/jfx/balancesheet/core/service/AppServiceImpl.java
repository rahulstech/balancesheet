package rahulstech.jfx.balancesheet.core.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.util.Log;

import java.util.Objects;

public abstract class AppServiceImpl<Parameter extends ServiceParameter> extends Service<ServiceResult> implements AppService<Parameter> {

    private static final String TAG = AppService.class.getSimpleName();

    private final Context context;

    private Parameter parameter;

    protected AppServiceImpl(Context context) {
        Objects.requireNonNull(context,"Context is null");
        this.context = context.getApplicationContext();
    }

    @Override
    public final void start() {}

    @Override
    public final void restart() {}

    public void start(Parameter param) {
        Objects.requireNonNull(param,"ServiceParameter is null");
        this.parameter = param;
        Log.info(TAG,"will start service with new parameter");

        // cancel the running task
        super.cancel();

        // reset
        super.reset();

        // fresh start a task with new parameters
        super.start();
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected final Task<ServiceResult> createTask() {
        final Parameter parameter = this.parameter;
        Log.info(TAG,"will create new task for id "+parameter.getTaskId());
        return createTask(parameter);
    }
}
