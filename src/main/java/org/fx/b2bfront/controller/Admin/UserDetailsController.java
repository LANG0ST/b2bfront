package org.fx.b2bfront.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.fx.b2bfront.model.Company;
import org.fx.b2bfront.utils.AppNavigator;

public class UserDetailsController {

    @FXML private Label title;
    @FXML private Label nameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label cityLabel;
    @FXML private Label createdAtLabel;
    @FXML private Label statusLabel;

    private Company user;

    public void setUser(Company user) {
        this.user = user;

        title.setText("Détails de " + user.getName());
        nameLabel.setText(user.getName());
        phoneLabel.setText(user.getPhone());
        emailLabel.setText(user.getEmail());
        cityLabel.setText(user.getCity());
        createdAtLabel.setText(user.getCreatedAt().toString());
        statusLabel.setText(user.isEnabled() ? "Activé" : "Désactivé");
    }

    @FXML
    private void goBack() {
        AppNavigator.navigateTo("Admin/GestionUsers.fxml");
    }


}
