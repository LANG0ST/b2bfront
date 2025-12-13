package org.fx.b2bfront.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fx.b2bfront.controller.components.NavbarController;

import java.util.Map;

public class AppNavigator {

    private static Stage mainStage;
    private static NavbarController navbarController;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void navigateTo(String fxml) {
        navigateToWithParams(fxml, null);
    }

    public static void navigateToWithParams(String fxml, Map<String, Object> params) {
        try {

            // ===== FIXED PATH =====
            FXMLLoader loader = new FXMLLoader(
                    AppNavigator.class.getResource("/fxml/" + fxml)
            );

            Parent root = loader.load();

            // ===== PASS PARAMETERS TO CONTROLLER =====
            if (params != null) {
                Object controller = loader.getController();
                if (controller instanceof ParamReceiver paramReceiver) {
                    paramReceiver.receiveParams(params);
                }
            }

            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + fxml);
        }
    }

    public static void setNavbarController(NavbarController controller) {
        navbarController = controller;
    }

    public static NavbarController getNavbarController() {
        return navbarController;
    }

}
