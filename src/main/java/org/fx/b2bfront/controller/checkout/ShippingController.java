package org.fx.b2bfront.controller.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import org.fx.b2bfront.utils.AppNavigator;

public class ShippingController {

    @FXML
    private RadioButton optionAmana;
    @FXML private RadioButton optionAramex;
    @FXML private RadioButton optionDHL;

    @FXML
    private void goToPayment() {
        // TODO: store selected shipping in a shared Order object

        AppNavigator.navigateTo("fxml/checkout/Payment.fxml");
    }
}
