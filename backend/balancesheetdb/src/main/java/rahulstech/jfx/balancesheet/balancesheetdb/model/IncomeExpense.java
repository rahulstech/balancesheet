package rahulstech.jfx.balancesheet.balancesheetdb.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import rahulstech.jfx.balancesheet.balancesheetdb.converter.LocalDateType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable(tableName = "incomes_expenses")
public class IncomeExpense {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false, persisterClass = LocalDateType.class)
    private LocalDate date;

    @DatabaseField(canBeNull = false)
    private BigDecimal amount;

    @DatabaseField
    private String note;

    @DatabaseField(canBeNull = false)
    private IncomeExpenseType type;

    public IncomeExpense() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public IncomeExpenseType getType() {
        return type;
    }

    public void setType(IncomeExpenseType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IncomeExpense) {
            IncomeExpense that = (IncomeExpense) o;
            return this.id == that.id;
        }
        return false;
    }

    public boolean equalContent(IncomeExpense that) {
        if (this == that) return true;
        return id == that.id && Objects.equals(date, that.date) && Objects.equals(amount, that.amount)
                && Objects.equals(note, that.note) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, amount, note, type);
    }
}
