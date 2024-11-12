package rahulstech.jfx.balancesheet.core.service;

import rahulstech.jfx.balancesheet.core.util.Parameter;

public class SimpleServiceResult implements ServiceResult{

    private final int taskId;

    private boolean successful;

    private Object value;

    private Throwable error;

    private Parameter extras;

    public SimpleServiceResult(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public int getTaskId() {
        return taskId;
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Throwable getError() {
        return error;
    }

    @Override
    public Parameter getExtras() {
        return extras;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public void setExtras(Parameter extras) {
        this.extras = extras;
    }
}
