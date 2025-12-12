package org.fx.b2bfront.controller.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.fx.b2bfront.store.CheckoutStore;
import org.fx.b2bfront.utils.AppNavigator;

public class ShippingController {

    @FXML private RadioButton optionAmana;
    @FXML private RadioButton optionAramex;
    @FXML private RadioButton optionDHL;

    private final ToggleGroup shippingGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        // Attach toggle group
        optionAmana.setToggleGroup(shippingGroup);
        optionAramex.setToggleGroup(shippingGroup);
        optionDHL.setToggleGroup(shippingGroup);

        // Restore choice if user returns
        if ("AMANA".equalsIgnoreCase(CheckoutStore.shippingMethod)) {
            shippingGroup.selectToggle(optionAmana);
        } else if ("ARAMEX".equalsIgnoreCase(CheckoutStore.shippingMethod)) {
            shippingGroup.selectToggle(optionAramex);
        } else if ("DHL".equalsIgnoreCase(CheckoutStore.shippingMethod)) {
            shippingGroup.selectToggle(optionDHL);
        }
    }

    @FXML
    private void goToPayment() {
        RadioButton selected = (RadioButton) shippingGroup.getSelectedToggle();
        if (selected == null) {
            showAlert("Veuillez choisir un mode de livraison.");
            return;
        }

        if (selected == optionAmana) {
            CheckoutStore.shippingMethod = "AMANA";
            CheckoutStore.shippingCost = 29.0;
        } else if (selected == optionAramex) {
            CheckoutStore.shippingMethod = "ARAMEX";
            CheckoutStore.shippingCost = 49.0;
        } else {
            CheckoutStore.shippingMethod = "DHL";
            CheckoutStore.shippingCost = 99.0;
        }

        AppNavigator.navigateTo("Checkout/Payment.fxml");
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
