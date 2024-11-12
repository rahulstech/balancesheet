module app {
    
    requires javafx.fxml;
    requires javafx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign;
    requires net.synedra.validatorfx;

    requires core;
    requires balancesheetdb;
    requires frontend;
    requires rahulstech.jfx.routing;

    opens rahulstech.jfx.balancesheet to javafx.graphics;
    opens rahulstech.jfx.balancesheet.controller to javafx.controls, javafx.fxml, core, rahulstech.jfx.routing, frontend;

    exports rahulstech.jfx.balancesheet.controller;
}
