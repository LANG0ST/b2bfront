package org.fx.b2bfront.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppNavigator {

    private static Stage mainStage;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void navigateTo(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    AppNavigator.class.getResource("/fxml/" + fxmlName)
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);

            mainStage.setScene(scene);
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
