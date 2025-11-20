package org.fx.b2bfront;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fx.b2bfront.utils.AppNavigator;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        AppNavigator.setStage(stage); // IMPORTANT

        Parent root = FXMLLoader.load(
                getClass().getResource("/fxml/auth.fxml")
        );

        Scene scene = new Scene(root, 1600, 900);
        stage.setTitle("B2B Platform");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}
