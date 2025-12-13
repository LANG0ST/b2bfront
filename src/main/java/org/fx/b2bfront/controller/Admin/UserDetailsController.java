package org.fx.b2bfront.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.fx.b2bfront.dto.CompanyDto;
import org.fx.b2bfront.utils.AppNavigator;

public class UserDetailsController {

    @FXML private Label title;
    @FXML private Label nameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label cityLabel;
    @FXML private Label statusLabel;
    @FXML private Label createdAtLabel;

    private CompanyDto company;

    public void setCompany(CompanyDto company) {
        this.company = company;

        title.setText("Détails de " + company.getName());
        nameLabel.setText(company.getName());
        phoneLabel.setText(company.getPhone());
        emailLabel.setText(company.getEmail());
        cityLabel.setText(company.getCity());
        statusLabel.setText(company.isEnabled() ? "Activé" : "Désactivé");
        createdAtLabel.setText(company.getCreatedAt());
    }

    @FXML
    private void goBack() {
        AppNavigator.navigateTo("Admin/GestionUsers.fxml");
    }


}
