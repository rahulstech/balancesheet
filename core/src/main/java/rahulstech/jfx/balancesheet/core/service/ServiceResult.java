package rahulstech.jfx.balancesheet.core.service;

import rahulstech.jfx.balancesheet.core.util.Parameter;

public interface ServiceResult {

    int getTaskId();

    boolean isSuccessful();

    Object getValue();

    Throwable getError();

    Parameter getExtras();
}
