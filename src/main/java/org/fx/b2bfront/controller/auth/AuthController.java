package org.fx.b2bfront.controller.auth;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.fx.b2bfront.utils.AppNavigator;

public class AuthController {

    @FXML private Pane slider;
    @FXML private Label slideTitle;
    @FXML private Button slideButton;

    @FXML private Button loginButton; // <----- add this

    private boolean showingLogin = true;

    @FXML
    public void initialize() {

        // Toggle panel
        slideButton.setOnAction(e -> toggle());

        // SIGN IN -> Homepage
        loginButton.setOnAction(e -> {
            AppNavigator.navigateTo("homepage.fxml");
        });
    }

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
}
