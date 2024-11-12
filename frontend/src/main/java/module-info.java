module frontend {

    requires transitive javafx.fxml;
    requires transitive javafx.controls;

    requires rahulstech.jfx.routing;
    requires net.synedra.validatorfx;

    requires transitive core;

    opens rahulstech.jfx.balancesheet.frontend.controller to javafx.controls, javafx.fxml;

    exports rahulstech.jfx.balancesheet.frontend.control;
    exports rahulstech.jfx.balancesheet.frontend.control.skin;
    exports rahulstech.jfx.balancesheet.frontend.controller;
    exports rahulstech.jfx.balancesheet.frontend.stage;
    exports rahulstech.jfx.balancesheet.frontend.routing;
    exports rahulstech.jfx.balancesheet.frontend.util;
    exports rahulstech.jfx.balancesheet.frontend.validator;

}