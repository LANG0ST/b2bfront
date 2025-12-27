package org.fx.b2bfront.controller.auth;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.fx.b2bfront.api.AuthApi;

public class ForgotPasswordController {

    @FXML private TextField emailField;
    @FXML private Label statusLabel;
    @FXML private Button sendBtn;

    // =========================
    // SEND EMAIL
    // =========================
    @FXML
    public void onSend() {

        String email = emailField.getText().trim();

        if (email.isEmpty() || !email.contains("@")) {
            showStatus("Please enter a valid email address.", true);
            return;
        }

        sendBtn.setDisable(true);
        showStatus("Sending reset token...", false);

        new Thread(() -> {
            try {
                AuthApi.forgotPassword(email);
            } catch (Exception ignored) {

            }

            Platform.runLater(() -> {
                navigateTo("/fxml/ResetPassword.fxml");
            });

        }).start();
    }
    // =========================
    // CANCEL → BACK TO LOGIN
    // =========================
    @FXML
    public void onCancel() {
        navigateTo("/fxml/auth.fxml");
    }

    // =========================
    // HELPERS
    // =========================
    private void showStatus(String msg, boolean error) {
        statusLabel.setText(msg);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        statusLabel.getStyleClass().removeAll("error", "success");

        if (error) {
            statusLabel.getStyleClass().add("error");
        } else {
            statusLabel.getStyleClass().add("success");
        }
    }

    private void navigateTo(String fxmlPath) {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
