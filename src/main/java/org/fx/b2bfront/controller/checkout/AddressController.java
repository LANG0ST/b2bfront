package org.fx.b2bfront.controller.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fx.b2bfront.store.CheckoutStore;
import org.fx.b2bfront.utils.AppNavigator;

public class AddressController {

    @FXML private TextField firstName;
    @FXML private TextField address;
    @FXML private ComboBox<String> city;      // changed from TextField → ComboBox
    @FXML private TextField zipcode;
    @FXML private CheckBox saveInfo;

    @FXML
    public void initialize() {

        // ----------------------------
        // 1) Load Moroccan cities
        // ----------------------------
        city.getItems().addAll(
                "Casablanca", "Rabat", "Marrakech", "Fès", "Tanger",
                "Agadir", "Meknès", "Oujda", "Tetouan", "Kenitra",
                "Safi", "Mohammedia", "El Jadida", "Beni Mellal", "Nador",
                "Khouribga", "Settat", "Taza", "Errachidia", "Larache",
                "Ksar El Kebir", "Guelmim", "Dakhla", "Laayoune", "Essaouira",
                "Ouarzazate", "Taourirt", "Berrechid", "Skhirat", "Salé"
        );

        // ----------------------------------------------------
        // 2) Auto-clean zipcode: only digits allowed
        // ----------------------------------------------------
        zipcode.textProperty().addListener((obs, oldV, newV) -> {
            zipcode.setText(newV.replaceAll("[^0-9]", ""));
        });

        // ----------------------------------------------------
        // 3) Restore stored values (Back-navigation)
        // ----------------------------------------------------
        if (CheckoutStore.contactName != null) {
            firstName.setText(CheckoutStore.contactName);
            address.setText(CheckoutStore.address);
            city.setValue(CheckoutStore.city);
            zipcode.setText(CheckoutStore.zipcode);
        }
    }

    // ==========================================================
    // GO TO NEXT STEP (SHIPPING) WITH VALIDATION
    // ==========================================================
    @FXML
    private void goToShipping() {

        String name = firstName.getText().trim();
        String addr = address.getText().trim();
        String c = city.getValue();
        String zip = zipcode.getText().trim();

        // --------------------------------------
        // VALIDATION
        // --------------------------------------

        if (name.length() < 2) {
            showAlert("Le nom doit contenir au moins 2 caractères.");
            return;
        }

        if (addr.length() < 5) {
            showAlert("L'adresse doit contenir au moins 5 caractères.");
            return;
        }

        if (c == null || c.isBlank()) {
            showAlert("Veuillez sélectionner une ville.");
            return;
        }

        if (!zip.matches("\\d{4,6}")) {
            showAlert("Le code postal doit contenir uniquement des chiffres (4 à 6).");
            return;
        }

        // --------------------------------------
        // SAVE TO STORE
        // --------------------------------------
        CheckoutStore.contactName = name;
        CheckoutStore.address = addr;
        CheckoutStore.city = c;
        CheckoutStore.zipcode = zip;

        AppNavigator.navigateTo("Checkout/Shipping.fxml");
    }

    // ==========================================================
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
