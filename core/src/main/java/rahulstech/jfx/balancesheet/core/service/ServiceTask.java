package rahulstech.jfx.balancesheet.core.service;

import javafx.concurrent.Task;
import rahulstech.jfx.balancesheet.core.application.Context;

public abstract class ServiceTask<Parameter extends ServiceParameter> extends Task<ServiceResult> {

    private final Parameter parameter;

    public ServiceTask(Parameter parameter) {
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    @Override
    protected final ServiceResult call() throws Exception {
        final Parameter parameter = this.parameter;
        ServiceResult result = call(parameter);
        if (null == result) {
            throw new NullPointerException("ServiceResult is null");
        }
        return result;
    }

    protected abstract ServiceResult call(Parameter parameter);
}
