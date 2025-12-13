package org.fx.b2bfront.controller.checkout;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fx.b2bfront.api.CheckoutApi;
import org.fx.b2bfront.dto.CartItem;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.store.CheckoutStore;
import org.fx.b2bfront.utils.AppNavigator;

import java.util.List;

public class PaymentController {

    @FXML private TextField cardName;
    @FXML private TextField cardNumber;
    @FXML private TextField expiration;
    @FXML private TextField cvv;

    private boolean internalChange = false; // prevents infinite listeners

    @FXML
    public void initialize() {

        if (CheckoutStore.contactName != null) {
            cardName.setText(CheckoutStore.contactName);
        }

        // ----------------------------
        // CARD NUMBER GROUPING (XXXX XXXX XXXX ...)
        // ----------------------------
        cardNumber.textProperty().addListener((obs, oldV, newV) -> {
            if (internalChange) return;

            // Remove spaces
            String digits = newV.replaceAll("[^0-9]", "");

            // Enforce max 24 digits
            if (digits.length() > 24) digits = digits.substring(0, 24);

            // Group digits 4 by 4
            StringBuilder grouped = new StringBuilder();
            for (int i = 0; i < digits.length(); i++) {
                if (i > 0 && i % 4 == 0) grouped.append(" ");
                grouped.append(digits.charAt(i));
            }

            internalChange = true;
            cardNumber.setText(grouped.toString());
            cardNumber.positionCaret(grouped.length());
            internalChange = false;
        });

        // ----------------------------
        // CVV → digits only, max 3
        // ----------------------------
        cvv.textProperty().addListener((obs, o, n) -> {
            cvv.setText(n.replaceAll("[^0-9]", "").substring(0, Math.min(3, n.length())));
        });

        // ----------------------------
        // EXPIRATION auto-format MM/YY
        // ----------------------------
        expiration.textProperty().addListener((obs, oldV, newV) -> {
            if (internalChange) return;

            String digits = newV.replaceAll("[^0-9]", "");

            if (digits.length() > 4) digits = digits.substring(0, 4);

            String formatted = "";
            if (digits.length() <= 2) {
                formatted = digits;
            } else {
                formatted = digits.substring(0, 2) + "/" + digits.substring(2);
            }

            internalChange = true;
            expiration.setText(formatted);
            expiration.positionCaret(formatted.length());
            internalChange = false;
        });
    }

    // ============================================================
    // PLACE ORDER
    // ============================================================
    @FXML
    private void placeOrder() {

        // -------------------- VALIDATION --------------------
        if (isEmpty(cardName) || isEmpty(cardNumber) || isEmpty(expiration) || isEmpty(cvv)) {
            showAlert("Veuillez remplir tous les champs de paiement.");
            return;
        }

        // Card number: digits only (remove spaces first)
        String rawCard = cardNumber.getText().replaceAll("[^0-9]", "");
        if (!rawCard.matches("\\d{20,24}")) {
            showAlert("Le numéro de carte doit contenir entre 20 et 24 chiffres.");
            return;
        }

        // CVV
        if (!cvv.getText().matches("\\d{3}")) {
            showAlert("Le CVV doit contenir exactement 3 chiffres.");
            return;
        }

        // Expiration
        if (!expiration.getText().matches("\\d{2}/\\d{2}")) {
            showAlert("La date d'expiration doit être au format MM/YY.");
            return;
        }

        int month = Integer.parseInt(expiration.getText().substring(0, 2));
        int year = Integer.parseInt(expiration.getText().substring(3));

        if (month < 1 || month > 12) {
            showAlert("Le mois doit être entre 01 et 12.");
            return;
        }

        if (year < 24) { // Simple rule for your simulation
            showAlert("Année d'expiration invalide.");
            return;
        }

        // -------------------- SESSION CHECK --------------------
        Long buyerId = AuthStore.companyId;
        if (buyerId == null) {
            showAlert("Session expirée. Veuillez vous reconnecter.");
            AppNavigator.navigateTo("auth/auth.fxml");
            return;
        }

        // -------------------- CART CHECK --------------------
        List<CartItem> cart = CartStore.getItems();
        if (cart == null || cart.isEmpty()) {
            showAlert("Votre panier est vide.");
            return;
        }

        // -------------------- ORDER CREATION --------------------
        new Thread(() -> {
            try {

                var createdOrder = CheckoutApi.createOrder(buyerId, cart);

                if (createdOrder != null) {

                    CheckoutStore.cardNumber = rawCard;
                    CheckoutStore.expiration = expiration.getText();
                    CheckoutStore.cvv = cvv.getText();

                    CheckoutStore.reset();

                    Platform.runLater(() ->
                            AppNavigator.navigateTo("checkout/Confirmation.fxml")
                    );

                } else {
                    Platform.runLater(() ->
                            showAlert("Erreur : la commande n'a pas été créée.")
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        showAlert("Erreur lors de la commande : " + e.getMessage())
                );
            }
        }).start();
    }

    // ============================================================
    private boolean isEmpty(TextField tf) {
        return tf.getText() == null || tf.getText().trim().isEmpty();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Paiement");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
