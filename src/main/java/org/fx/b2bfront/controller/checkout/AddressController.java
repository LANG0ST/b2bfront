package org.fx.b2bfront.controller.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.fx.b2bfront.utils.AppNavigator;

public class AddressController {

    @FXML
    private TextField firstName;
    @FXML private TextField address;
    @FXML private TextField city;
    @FXML private TextField zipcode;
    @FXML private CheckBox saveInfo;

    @FXML
    private void goToShipping() {
        // TODO: Save data to shared OrderContext object

        AppNavigator.navigateTo("fxml/checkout/Shipping.fxml");
    }
}
