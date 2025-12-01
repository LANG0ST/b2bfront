package org.fx.b2bfront.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.fx.b2bfront.api.CategoryApi;
import org.fx.b2bfront.api.CompanyApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.model.Product;
import org.fx.b2bfront.utils.AppNavigator;


public class ProductDetailsController {
    @FXML
    private Label title;
    @FXML private Label nameLabel;
    @FXML private Label description;
    @FXML private Label categorieLable;
    @FXML private Label price;
    @FXML private Label stock;
    @FXML private Label company;


    private ProductDto prod;
    private  final CategoryApi categoryApi = new CategoryApi();
    private  final CompanyApi companyApi = new CompanyApi();

    public void setProd(ProductDto prod) {
        this.prod = prod;

        title.setText("Détails de " + prod.getName());
        nameLabel.setText(prod.getName());
        description.setText(prod.getDescription());
        categorieLable.setText(categoryApi.findById(prod.getCategoryId()).getName());
        price.setText(String.valueOf(prod.getPrice()));
        stock.setText(String.valueOf(prod.getStock()));
        company.setText(companyApi.getById(prod.getCompanyId()).getName());
    }

    @FXML
    private void goBack() {
        AppNavigator.navigateTo("Admin/GestionProducts.fxml");
    }


}

