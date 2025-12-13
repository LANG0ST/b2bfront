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

    // SIGNUP
    @FXML private TextField signupName;
    @FXML private TextField signupIce;
    @FXML private TextField signupEmail;
    @FXML private PasswordField signupPassword;
    @FXML private TextField signupAddress;
    @FXML private ComboBox<String> signupCity;   // UPDATED
    @FXML private TextField signupPhone;
    @FXML private Button signupButton;

    private boolean showingLogin = true;

    @FXML
    public void initialize() {

        // ---------------------------
        // CITY LIST (30 most known)
        // ---------------------------
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


    /* ================================
                 TOGGLE PANEL
       ================================ */
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


    /* ================================
              VALIDATION
       ================================ */
    private String validateSignup(String name, String ice, String email, String password,
                                  String address, String city, String phone) {

        if (name.isEmpty()) return "Company name is required.";
        if (email.isEmpty()) return "Email is required.";
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            return "Invalid email format.";

        if (password.length() < 8)
            return "Password must be at least 8 characters.";

        if (address.length() < 4)
            return "Address is too short.";

        if (city == null || city.isEmpty())
            return "Please select a city.";

        if (!phone.matches("\\d{8,12}"))
            return "Phone must contain only digits (8–12).";

        if (!ice.isEmpty() && !ice.matches("\\d{6,15}"))
            return "ICE must be 6–15 digits.";

        return null;
    }


    /* ================================
               REGISTER
       ================================ */
    @FXML
    public void registerCompany() {

        String name = signupName.getText().trim();
        String ice = signupIce.getText().trim();
        String email = signupEmail.getText().trim();
        String password = signupPassword.getText().trim();
        String address = signupAddress.getText().trim();
        String city = signupCity.getValue(); // from ComboBox
        String phone = signupPhone.getText().trim();

        // VALIDATION
        String error = validateSignup(name, ice, email, password, address, city, phone);
        if (error != null) {
            showAlert(error);
            return;
        }

        RegisterRequestFront req = new RegisterRequestFront(
                name, ice, email, password, address, city, phone
        );

        var result = AuthApi.register(req);

        if (!result.success()) {
            showAlert("Registration failed:\n" + result.message());
            return;
        }

        showAlert("Registration successful! You can now log in.");
        toggleToLogin();
    }


    /* ================================
                LOGIN
       ================================ */
    private void loginCompany() {

        String email = loginEmail.getText().trim();
        String password = loginPassword.getText().trim();

        // ADMIN BYPASS
        if (email.equals("admin@b2b.ensa") && password.equals("admin1234")) {
            AppNavigator.navigateTo("Admin/AdminDashBoard.fxml");
            return;
        }

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Please enter email and password.");
            return;
        }

        var req = new LoginRequestFront(email, password);
        var result = AuthApi.login(req);

        if (!result.success()) {
            showAlert("Login failed:\n" + result.error());
            return;
        }

        var data = result.data();
        AuthStore.jwt = data.token;
        AuthStore.role = data.user.role;
        AuthStore.companyId = data.user.companyId;
        AuthStore.email = data.user.email;

        // Load cart
        CartStore.loadCartFromBackend(AuthStore.companyId);

        // Load notifications state
        new Thread(() -> {
            try {
                var list = NotificationApi.getNotifications(AuthStore.companyId);
                boolean unread = list.stream().anyMatch(n -> !n.isRead());
                AppStore.setHasNotifications(unread);
            } catch (Exception ignored) {}
        }).start();

        AppNavigator.navigateTo("homepage.fxml");
    }


    /* ================================
                ALERT
       ================================ */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
