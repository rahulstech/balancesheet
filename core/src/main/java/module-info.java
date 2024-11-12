module core {

    requires transitive java.logging;
    requires transitive javafx.graphics;

    exports rahulstech.jfx.balancesheet.core.application;
    exports rahulstech.jfx.balancesheet.core.service;
    exports rahulstech.jfx.balancesheet.core.util;
}