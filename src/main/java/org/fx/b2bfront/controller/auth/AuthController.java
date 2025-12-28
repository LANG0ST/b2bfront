package org.fx.b2bfront.controller.auth;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.fx.b2bfront.api.AuthApi;
import org.fx.b2bfront.api.NotificationApi;
import org.fx.b2bfront.dto.LoginRequestFront;
import org.fx.b2bfront.dto.RegisterRequestFront;
import org.fx.b2bfront.service.NotificationPoller;
import org.fx.b2bfront.store.AppStore;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.utils.AppNavigator;

public class AuthController {

    @FXML private Pane slider;
    @FXML private Label slideTitle;
    @FXML private Button slideButton;

    // LOGIN
    @FXML private TextField loginEmail;
    @FXML private PasswordField loginPassword;
    @FXML private Button loginButton;
    @FXML private Hyperlink forgotPasswordLink;

    // SIGNUP
    @FXML private TextField signupName;
    @FXML private TextField signupIce;
    @FXML private TextField signupEmail;
    @FXML private PasswordField signupPassword;
    @FXML private TextField signupAddress;
    @FXML private ComboBox<String> signupCity;
    @FXML private TextField signupPhone;
    @FXML private Button signupButton;

    private boolean showingLogin = true;

    // =====================================================
    // INITIALIZE
    // =====================================================
    @FXML
    public void initialize() {

        signupCity.setItems(FXCollections.observableArrayList(
                "Casablanca", "Rabat", "Marrakech", "Tangier", "Fes",
                "Agadir", "Tetouan", "Oujda", "Kenitra", "Safi",
                "Mohammedia", "El Jadida", "Beni Mellal", "Nador", "Taza",
                "Settat", "Larache", "Ksar El Kebir", "Berrechid", "Khouribga",
                "Meknes", "Ouarzazate", "Chefchaouen", "Al Hoceima", "Dakhla",
                "Laayoune", "Errachidia", "Guelmim", "Essaouira", "Taroudant"
        ));

        slideButton.setOnAction(e -> toggle());
        loginButton.setOnAction(e -> loginCompany());
    }

    // =====================================================
    // SLIDER
    // =====================================================
    private void toggle() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), slider);

        if (showingLogin) {
            slide.setToX(0);
            slideTitle.setText("ALREADY HAVE AN ACCOUNT?");
            slideButton.setText("LOG IN");
            showingLogin = false;
        } else {
            slide.setToX(800);
            slideTitle.setText("DON'T HAVE AN ACCOUNT?");
            slideButton.setText("SIGN UP");
            showingLogin = true;
        }

        slide.play();
    }

    private void toggleToLogin() {
        if (!showingLogin) toggle();
    }

    // =====================================================
    // FORGOT PASSWORD (NEW)
    // =====================================================
    @FXML
    private void onForgotPassword() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Mot de passe oublié");
        dialog.setHeaderText("Réinitialisation du mot de passe");
        dialog.setContentText("Entrez votre adresse email :");

        dialog.showAndWait().ifPresent(email -> {

            if (email.isBlank()) {
                showAlert("Veuillez saisir une adresse email valide.");
                return;
            }

            new Thread(() -> {
                try {
                    AuthApi.forgotPassword(email);

                    javafx.application.Platform.runLater(() ->
                            showAlert(
                                    "Si un compte existe avec cet email,\n" +
                                            "un lien de réinitialisation a été envoyé."
                            )
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                    javafx.application.Platform.runLater(() ->
                            showAlert("Impossible d'envoyer l'email pour le moment.")
                    );
                }
            }).start();
        });
    }

    // =====================================================
    // REGISTER
    // =====================================================
    @FXML
    public void registerCompany() {

        String name = signupName.getText().trim();
        String ice = signupIce.getText().trim();
        String email = signupEmail.getText().trim();
        String password = signupPassword.getText().trim();
        String address = signupAddress.getText().trim();
        String city = signupCity.getValue();
        String phone = signupPhone.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.length() < 8 ||
                city == null || !phone.matches("\\d{8,12}")) {
            showAlert("Veuillez remplir correctement tous les champs.");
            return;
        }

        RegisterRequestFront req =
                new RegisterRequestFront(name, ice, email, password, address, city, phone);

        var result = AuthApi.register(req);

        if (!result.success()) {
            showAlert("Registration failed:\n" + result.message());
            return;
        }

        showAlert("Inscription réussie. Vous pouvez maintenant vous connecter.");
        toggleToLogin();
    }

    // =====================================================
    // LOGIN
    // =====================================================
    private void loginCompany() {

        String email = loginEmail.getText().trim();
        String password = loginPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Veuillez entrer email et mot de passe.");
            return;
        }

        if (email.equals("admin@b2b.ensa") && password.equals("admin1234")) {
            AppNavigator.navigateTo("Admin/AdminDashBoard.fxml");
            return;
        }
        var result = AuthApi.login(new LoginRequestFront(email, password));

        if (!result.success()) {
            showAlert("Login échoué:\n" + result.error());
            return;
        }

        var data = result.data();
        AuthStore.jwt = data.token;
        AuthStore.role = data.user.role;
        AuthStore.companyId = data.user.companyId;
        AuthStore.email = data.user.email;

        CartStore.loadCartFromBackend(AuthStore.companyId);

        new Thread(() -> {
            try {
                var list = NotificationApi.getNotifications(AuthStore.companyId);
                AppStore.setHasNotifications(
                        list.stream().anyMatch(n -> !n.isRead())
                );
            } catch (Exception ignored) {}
        }).start();

        NotificationPoller.start();
        AppNavigator.navigateTo("homepage.fxml");
    }

    // =====================================================
    // ALERT
    // =====================================================
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void openForgotPassword() {
        AppNavigator.navigateTo("ForgotPassword.fxml");
    }

}
