package org.fx.b2bfront.controller.checkout;

import javafx.fxml.FXML;
import org.fx.b2bfront.utils.AppNavigator;

public class ConfirmationController {

    @FXML
    private void goToDashboard() {
        AppNavigator.navigateTo("dashboard/Dashboard.fxml");
    }
}
