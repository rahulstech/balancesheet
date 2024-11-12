package rahulstech.jfx.balancesheet.balancesheetdb.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rahulstech.jfx.balancesheet.balancesheetdb.BalanceSheetDB;
import rahulstech.jfx.balancesheet.balancesheetdb.TestBalanceSheetDB;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpense;
import rahulstech.jfx.balancesheet.balancesheetdb.model.IncomeExpenseType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IncomeExpenseRepoTest {

    BalanceSheetDB db;

    IncomeExpenseRepo repo;

    @BeforeEach
    void setUp() throws Exception {
        db = new TestBalanceSheetDB();
        repo = db.getIncomeExpenseRepo();

        addSample();
    }

    @AfterEach
    void tearDown() {
        if (null != db) {
            db.close();
        }
    }

    @Test
    void create() throws Exception {
        IncomeExpense value = new IncomeExpense();
        value.setDate(LocalDate.now());
        value.setAmount(BigDecimal.valueOf(1500));
        value.setType(IncomeExpenseType.INCOME);
        value.setNote("this is profit against sell of derivative");

        IncomeExpense saved = repo.create(value);

        assertNotNull(saved, "IncomeExpense not saved");

        assertEquals(4,saved.getId(), "expected id not returned on save");
    }

    @Test
    void queryById() throws Exception {

        IncomeExpense expected = new IncomeExpense();
        expected.setId(1);
        expected.setDate(LocalDate.of(2024,7,1));
        expected.setAmount(new BigDecimal("1556.75"));
        expected.setType(IncomeExpenseType.INCOME);
        expected.setNote("this is a profit");

        IncomeExpense actual = repo.queryById(1);

        assertTrue(expected.equalContent(actual));
    }

    @Test
    void update() throws Exception {

        IncomeExpense value = new IncomeExpense();
        value.setId(1);
        value.setDate(LocalDate.of(2024,8,1));
        value.setAmount(new BigDecimal("1565.7556"));
        value.setType(IncomeExpenseType.INCOME);
        value.setNote("this is a profit updated");

        boolean actual = repo.update(value);

        assertTrue(actual);
    }

    @Test
    void deleteMultiple() throws Exception {

        IncomeExpense val1 = new IncomeExpense();
        val1.setId(1);
        val1.setDate(LocalDate.of(2024,7,1));
        val1.setAmount(new BigDecimal("1556.75"));
        val1.setType(IncomeExpenseType.INCOME);
        val1.setNote("this is a profit");

        IncomeExpense val2 = new IncomeExpense();
        val2.setId(3);
        val2.setDate(LocalDate.of(2024,5,3));
        val2.setAmount(new BigDecimal("249"));
        val2.setType(IncomeExpenseType.EXPENSE);
        val2.setNote("mobile recharge");

        List<IncomeExpense> values = Arrays.asList(val1,val2);

        int actual = repo.deleteMultiple(values);

        assertEquals(2,actual);
    }

    @Test
    void count() throws Exception {
        long count = repo.count();
        assertEquals(3,count,"count all error");
    }

    @Test
    void countByQuery() throws Exception {
        QueryParameter param = new QueryParameter();
        param.put(IncomeExpenseRepo.KEY_DATE_START, LocalDate.of(2024,6,1));
        long count = repo.count(param);
        assertEquals(2,count,"count by query error");
    }

    /*****************************************
     *              Sample Data             *
     ****************************************/

    void addSample() throws SQLException {
        IncomeExpense val1 = new IncomeExpense();
        val1.setId(1);
        val1.setDate(LocalDate.of(2024,7,1));
        val1.setAmount(new BigDecimal("1556.75"));
        val1.setType(IncomeExpenseType.INCOME);
        val1.setNote("this is a profit");

        IncomeExpense val2 = new IncomeExpense();
        val2.setId(2);
        val2.setDate(LocalDate.of(2024,6,2));
        val2.setAmount(new BigDecimal("1700"));
        val2.setType(IncomeExpenseType.EXPENSE);
        val2.setNote("exam registration");

        IncomeExpense val3 = new IncomeExpense();
        val3.setId(3);
        val3.setDate(LocalDate.of(2024,5,3));
        val3.setAmount(new BigDecimal("249"));
        val3.setType(IncomeExpenseType.EXPENSE);
        val3.setNote("mobile recharge");

        List<IncomeExpense> values = Arrays.asList(val1,val2,val3);

        Dao<IncomeExpense,Long> dao = DaoManager.createDao(db.getConnectionSource(),IncomeExpense.class);
        dao.create(values);
    }
}