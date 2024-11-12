package rahulstech.jfx.balancesheet.service;

import javafx.concurrent.Task;
import javafx.util.Callback;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.service.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FilterService<T> extends AppServiceImpl<FilterService.FilterServiceParameter<T>> {

    private static final int TASK_FILTER = 1;

    public FilterService(Context context) {
        super(context);
    }

    @Override
    public Task<ServiceResult> createTask(FilterServiceParameter<T> param) {
        return new ServiceTask<>(param) {
            @Override
            protected ServiceResult call(FilterServiceParameter<T> parameter) {
                int taskId = param.getTaskId();
                List<T> items = param.getItems();
                Predicate<T> operation = param.getOperation();
                SimpleServiceResult result = new SimpleServiceResult(taskId);
                result.setExtras(param.getExtras());

                try {
                    if (null == items || items.isEmpty()) {
                        result.setValue(Collections.emptyList());
                        result.setSuccessful(true);
                    } else {
                        List<T> filtered = items.stream().filter(operation).toList();
                        result.setValue(filtered);
                        result.setSuccessful(true);
                    }
                }
                catch (Exception ex) {
                    result.setSuccessful(false);
                    result.setError(ex);
                    result.setValue(null);
                }

                return result;
            }
        };
    }

    public static class FilterServiceParameter<T> extends SimpleServiceParameter {

        private final List<T> items;

        private final Predicate<T> operation;

        public FilterServiceParameter(List<T> items, Predicate<T> operation) {
            this(TASK_FILTER,items,operation);
        }

        public FilterServiceParameter(int id, List<T> items, Predicate<T> operation) {
            super(id);
            Objects.requireNonNull(operation,"operation Predicate is null");
            this.items = items;
            this.operation = operation;
        }

        public List<T> getItems() {
            return items;
        }

        public Predicate<T> getOperation() {
            return operation;
        }
    }
}
