package org.fx.b2bfront.controller.auth;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fx.b2bfront.api.AuthApi;

public class ResetPasswordController {

    @FXML private TextField tokenField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;
    @FXML private Button resetBtn;

    // =========================
    // RESET PASSWORD
    // =========================
    @FXML
    public void onReset() {

        String token = tokenField.getText().trim();
        String pass1 = newPasswordField.getText();
        String pass2 = confirmPasswordField.getText();

        if (token.isEmpty()) {
            showStatus("Token is required.", true);
            return;
        }

        if (pass1.length() < 8) {
            showStatus("Password must be at least 8 characters.", true);
            return;
        }

        if (!pass1.equals(pass2)) {
            showStatus("Passwords do not match.", true);
            return;
        }

        resetBtn.setDisable(true);
        showStatus("Resetting password...", false);

        // 🚫 NEVER BLOCK FX THREAD
        new Thread(() -> {
            try {
                AuthApi.resetPassword(token, pass1);

                Platform.runLater(() -> {
                    showStatus("Password reset successful.", false);

                    // Small delay then go back to login
                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    pause.setOnFinished(e -> navigateTo("/fxml/auth.fxml"));
                    pause.play();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    showStatus("Invalid or expired token.", true);
                    resetBtn.setDisable(false);
                });
            }
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
