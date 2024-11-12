package rahulstech.jfx.balancesheet.service;

import javafx.concurrent.Task;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpense;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.IncomeExpenseRepo;
import rahulstech.jfx.balancesheet.balancesheetdb.repository.QueryParameter;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.core.service.ServiceResult;

import java.util.List;

public class IncomeExpenseCRUDService extends BaseBalanceSheetDBCRUDService<IncomeExpense> {

    private static final String TAG = IncomeExpenseCRUDService.class.getSimpleName();

    private final IncomeExpenseRepo repo;

    public IncomeExpenseCRUDService(Context context) {
        super(context);
        repo = getDatabase().getIncomeExpenseRepo();
    }

    @Override
    public Task<ServiceResult> createTask(CrudServiceParameter param) {
        return new CrudTask<IncomeExpense>(param) {

            @Override
            protected IncomeExpense createSingle(IncomeExpense value) throws Exception {
                return repo.create(value);
            }

            @Override
            protected List<IncomeExpense> readMultiple(QueryParameter params) throws Exception {
                return repo.query(params);
            }

            @Override
            protected int deleteMultiple(List<IncomeExpense> values) throws Exception {
                return repo.deleteMultiple(values);
            }

            @Override
            protected long count(QueryParameter params) throws Exception {
                return null == params ? repo.count() : repo.count(params);
            }
        };
    }
}
