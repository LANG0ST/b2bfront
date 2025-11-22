package org.fx.b2bfront.controller.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.fx.b2bfront.utils.AppNavigator;

public class PaymentController {

    @FXML
    private TextField cardName;
    @FXML private TextField cardNumber;
    @FXML private TextField expiration;
    @FXML private TextField cvv;

    @FXML
    private void placeOrder() {
        // TODO: send to backend later
        System.out.println("Order confirmed (fake save)");

        AppNavigator.navigateTo("fxml/checkout/Success.fxml");
    }
}
