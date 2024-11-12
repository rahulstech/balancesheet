package rahulstech.jfx.balancesheet.core.service;

import javafx.concurrent.Task;
import rahulstech.jfx.balancesheet.core.application.Context;

public interface AppService<Parameter extends ServiceParameter> {

    Context getContext();

    void start(Parameter param);

    Task<ServiceResult> createTask(Parameter param);
}
