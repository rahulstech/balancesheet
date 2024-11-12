package rahulstech.jfx.balancesheet.balancesheetdb.repository;

import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpense;

public interface IncomeExpenseRepo extends BaseRepository<IncomeExpense> {

    String COLUMN_DATE = "date";

    String KEY_PHRASE = "balancesheet.incomexpenserepo.parameter.PHRASE";

    String KEY_DATE = "balancesheet.incomexpenserepo.parameter.DATE";

    String KEY_DATE_START = "balancesheet.incomexpenserepo.parameter.DATE_START";

    String KEY_DATE_END = "balancesheet.incomexpenserepo.parameter.DATE_END";

    String KEY_TYPE = "balancesheet.incomexpenserepo.parameter.TYPE";

}
