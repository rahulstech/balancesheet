package rahulstech.jfx.balancesheet.balancesheetdb.repository;

import java.util.Collection;
import java.util.List;

public interface BaseRepository<T> {

    long count() throws Exception;

    long count(QueryParameter params) throws Exception;

    T create(T value) throws Exception;

    T queryById(Object id) throws Exception;

    List<T> query(QueryParameter params) throws Exception;

    boolean update(T value) throws Exception;

    int updateMultiple(Collection<T> values) throws Exception;

    boolean delete(T value) throws Exception;

    int deleteMultiple(Collection<T> values) throws Exception;
}
