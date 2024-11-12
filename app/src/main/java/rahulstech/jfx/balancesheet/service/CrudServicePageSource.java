package rahulstech.jfx.balancesheet.service;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.QueryParameter;
import rahulstech.jfx.balancesheet.core.service.ServiceParameter;
import rahulstech.jfx.balancesheet.core.service.ServiceResult;
import rahulstech.jfx.balancesheet.core.util.Log;

import java.util.List;
import java.util.Objects;

public class CrudServicePageSource<T> implements PageSource<T> {

    private static final String TAG = CrudServicePageSource.class.getSimpleName();

    private final BaseBalanceSheetDBCRUDService<T> service;

    private final QueryParameter parameter;

    private final long originalOffset;

    private ChangeListener<ServiceResult> totalCountChangeListener;

    private long totalCount = 0;

    private Runnable waitingTask;

    public CrudServicePageSource(BaseBalanceSheetDBCRUDService<T> service, QueryParameter param) {
        this.service = Objects.requireNonNull(service,"BaseBalanceSheetDBCRUDService is null");
        this.parameter = Objects.requireNonNull(param,"QueryParameter is null");
        Long offset = param.getOffset();
        this.originalOffset = null == offset ? 0 : offset;
        totalCountChangeListener = countTotalEntries();
        service.valueProperty().addListener((ov,oldResult,newResult) -> onServiceResult(newResult));
    }

    public BaseBalanceSheetDBCRUDService<T> getService() {
        return service;
    }

    public QueryParameter getParameter() {
        return parameter;
    }

    private ChangeListener<ServiceResult> countTotalEntries() {
        if (null != totalCountChangeListener) {
            return totalCountChangeListener;
        }
        QueryParameter qParam = getParameter();
        ChangeListener<ServiceResult> listener = (ov, oldResult, newResult) -> {
            if (null != newResult && BaseBalanceSheetDBCRUDService.TASK_COUNT == newResult.getTaskId()) {
                if (newResult.isSuccessful()) {
                    this.totalCount = (long) newResult.getValue();
                    Log.info(TAG, "totalCount=" + totalCount);
                    if (null != waitingTask) {
                        Platform.runLater(() -> {
                            waitingTask.run();
                            waitingTask = null;
                        });
                    }
                } else {
                    Log.error(TAG, "count query failed", newResult.getError());
                }
                service.valueProperty().removeListener(totalCountChangeListener);
                totalCountChangeListener = null;
            }
        };
        service.valueProperty().addListener(listener);
        BaseBalanceSheetDBCRUDService.CrudServiceParameter param = new BaseBalanceSheetDBCRUDService.CrudServiceParameter(BaseBalanceSheetDBCRUDService.TASK_COUNT,qParam);
        service.start(param);
        return listener;
    }

    @Override
    public void loadPage(long limit, long offset) {
        if (totalCountChangeListener != null) {
            Log.info(TAG,"count query not finished adding waiting task");
            waitingTask = () -> loadPage(limit,offset);
            return;
        }
        if (totalCount < 1) {
            Log.info(TAG,"total count is less than 1");
            PageData<T> page = new PageData<>(totalCount,offset,limit,null);
            setRecentPage(page);
            return;
        }
        Log.info(TAG,"load page  with offset="+offset+" and limit="+limit);
        long adjustedOffset = originalOffset + offset;
        long lastRecordIndex = adjustedOffset + totalCount - 1;
        long targetLastRecordIndex = adjustedOffset + limit - 1;
        long adjustedLimit = (lastRecordIndex < targetLastRecordIndex)
                ? lastRecordIndex - adjustedOffset + 1
                : limit;
        QueryParameter copy = new QueryParameter(getParameter());
        copy.limit(adjustedLimit);
        copy.offset(adjustedOffset);
        BaseBalanceSheetDBCRUDService.CrudServiceParameter param
                = new BaseBalanceSheetDBCRUDService.CrudServiceParameter(BaseBalanceSheetDBCRUDService.TASK_READ_MULTIPLE,copy,copy);
        getService().start(param);
    }

    private final ObjectProperty<PageData<T>> recentPage = new SimpleObjectProperty<>();

    @Override
    public ObjectProperty<PageData<T>> recentPageProperty() {
        return recentPage;
    }

    @Override
    public PageData<T> getRecentPage() {
        return recentPage.get();
    }

    protected void setRecentPage(PageData<T> page) {
        recentPage.set(page);
    }

    private void onServiceResult(ServiceResult result) {
        if (null == result) {
            return;
        }
        int taskId = result.getTaskId();
        if (!result.isSuccessful()) {
            Log.error(TAG,"service task with id="+taskId+" failed",result.getError());
            return;
        }
        if (taskId == BaseBalanceSheetDBCRUDService.TASK_READ_MULTIPLE) {
            List<T> items = (List<T>) result.getValue();
            QueryParameter extras = (QueryParameter) result.getExtras();
            PageData<T> page = new PageData<>(totalCount,extras.getOffset(),extras.getLimit(),items);
            setRecentPage(page);
        }

    }
}
