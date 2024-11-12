package rahulstech.jfx.balancesheet.core.service;

import rahulstech.jfx.balancesheet.core.util.Parameter;

public class SimpleServiceParameter implements ServiceParameter {

    private final int taskId;

    private Parameter extras;

    public SimpleServiceParameter(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public int getTaskId() {
        return taskId;
    }

    @Override
    public Parameter getExtras() {
        return null;
    }

    public void setExtras(Parameter extras) {
        this.extras = extras;
    }
}
