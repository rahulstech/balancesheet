module balancesheetdb {

    requires core;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires ormlite.core;
    requires ormlite.jdbc;

    opens rahulstech.jfx.balancesheet.balancesheetdb.model to ormlite.core;
    opens rahulstech.jfx.balancesheet.balancesheetdb.converter to ormlite.core;

    exports rahulstech.jfx.balancesheet.balancesheetdb;
    exports rahulstech.jfx.balancesheet.balancesheetdb.model;
    exports rahulstech.jfx.balancesheet.balancesheetdb.repository;
}