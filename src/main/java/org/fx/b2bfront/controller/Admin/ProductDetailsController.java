package org.fx.b2bfront.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.fx.b2bfront.model.Produit;
import org.fx.b2bfront.utils.AppNavigator;

import java.math.BigDecimal;


public class ProductDetailsController {
    @FXML
    private Label title;
    @FXML private Label nameLabel;
    @FXML private Label description;
    @FXML private Label categorieLable;
    @FXML private Label price;
    @FXML private Label stock;
    @FXML private Label company;


    private Produit prod;

    public void setProd(Produit prod) {
        this.prod = prod;

        title.setText("Détails de " + prod.getName());
        nameLabel.setText(prod.getName());
        description.setText(prod.getDescription());
        categorieLable.setText(prod.getCategorie().getName());
        price.setText(String.valueOf(prod.getPrice()));
        stock.setText(String.valueOf(prod.getStock()));
        company.setText(prod.getCompany().getName());
    }

    @FXML
    private void goBack() {
        AppNavigator.navigateTo("Admin/GestionUsers.fxml");
    }


}

