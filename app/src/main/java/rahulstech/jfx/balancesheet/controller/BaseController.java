package rahulstech.jfx.balancesheet.controller;

import javafx.fxml.Initializable;
import rahulstech.jfx.balancesheet.BalanceSheetApp;
import rahulstech.jfx.balancesheet.core.application.Context;
import rahulstech.jfx.balancesheet.frontend.controller.Controller;
import rahulstech.jfx.balancesheet.frontend.routing.BaseRoutingController;
import rahulstech.jfx.routing.lifecycle.LifecycleAwareController;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseController extends BaseRoutingController implements Initializable, Controller, LifecycleAwareController {

    public BaseController(Context parent) {
        super(parent);
    }

    @Override
    public BalanceSheetApp getApplicationContext() {
        return (BalanceSheetApp) super.getApplicationContext();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
