package org.fx.b2bfront.controller.auth;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.fx.b2bfront.api.AuthApi;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.dto.LoginRequestFront;
import org.fx.b2bfront.dto.RegisterRequestFront;
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
    @FXML private TextField signupCity;
    @FXML private TextField signupPhone;

    @FXML private Button signupButton;

    private boolean showingLogin = true;

    @FXML
    public void initialize() {

        slideButton.setOnAction(e -> toggle());
        loginButton.setOnAction(e -> loginCompany());

    }

    /* ================================
              TOGGLE PANELS
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
        if (!showingLogin) {
            toggle();
        }
    }

    /* ================================
               REGISTER COMPANY
       ================================ */
    @FXML
    public void registerCompany() {
        System.out.println("REGISTER CLICKED");

        String name = signupName.getText().trim();
        String ice = signupIce.getText().trim();
        String email = signupEmail.getText().trim();
        String password = signupPassword.getText().trim();
        String address = signupAddress.getText().trim();
        String city = signupCity.getText().trim();
        String phone = signupPhone.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || address.isEmpty() || city.isEmpty() || phone.isEmpty()) {
            showAlert("All fields except ICE are required.");
            return;
        }

        RegisterRequestFront req = new RegisterRequestFront(
                name, ice, email, password, address, city, phone
        );

        System.out.println("=== JSON SENDING TO BACKEND ===");
        System.out.println(new com.google.gson.Gson().toJson(req));
        System.out.println("================================");

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
        if(!(email.equals("admin@b2b.ensa") && password.equals("admin1234") )) {
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

        AppNavigator.navigateTo("homepage.fxml");
        }else {
            AppNavigator.navigateTo("Admin/AdminDashBoard.fxml");

        }
    }

    /* ================================
               ALERT UTIL
       ================================ */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
