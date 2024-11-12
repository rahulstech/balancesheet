package rahulstech.jfx.balancesheet.core.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

public class Parameter {

    private final HashMap<String, Object> parameters = new HashMap<>();

    public Parameter() {
        this(null);
    }

    public Parameter(Parameter src) {
        if (null != src) {
            parameters.putAll(src.parameters);
        }
    }

    public void put(String name, long value) {
        parameters.put(name,value);
    }

    public long getLong(String name) {
        return (long) parameters.get(name);
    }

    public void put(String name, long[] values) {
        parameters.put(name,values);
    }

    public long[] getLongArray(String name) {
        return (long[]) parameters.get(name);
    }

    public void put(String name, String value) {
        parameters.put(name,value);
    }

    public String getString(String name) {
        return (String) parameters.get(name);
    }

    public void put(String name, LocalDate value) {
        parameters.put(name,value);
    }

    public LocalDate getLocalDate(String name) {
        return (LocalDate) parameters.get(name);
    }

    public void put(String name, BigDecimal value) {
        parameters.put(name,value);
    }

    public BigDecimal getBigDecimal(String name) {
        return (BigDecimal) parameters.get(name);
    }

    public void put(String name, Object value) {
        parameters.put(name,value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) parameters.get(name);
    }

    public boolean contains(String name) {
        return parameters.containsKey(name);
    }
}
