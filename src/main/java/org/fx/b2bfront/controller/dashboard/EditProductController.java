package org.fx.b2bfront.controller.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.utils.AppNavigator;
import org.fx.b2bfront.utils.ParamReceiver;

import java.io.File;
import java.net.URL;
import java.util.Map;

public class EditProductController implements ParamReceiver {

    @FXML private ImageView productImage;
    @FXML private TextField nameField;
    @FXML private TextArea descField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnChooseImage;

    private ProductDto product;
    private File selectedImageFile = null;

    @FXML
    public void initialize() {
        btnCancel.setOnAction(e -> AppNavigator.navigateTo("dashboard/Dashboard.fxml"));
        btnChooseImage.setOnAction(e -> chooseImage());
        btnSave.setOnAction(e -> saveProduct());
    }

    // ============================================================
    // PARAM RECEIVER (called automatically by AppNavigator)
    // ============================================================
    @Override
    public void receiveParams(Map<String, Object> params) {
        if (params != null && params.containsKey("product")) {
            this.product = (ProductDto) params.get("product");
            loadProduct();
        }
    }

    // ============================================================
    private void loadProduct() {
        if (product == null) return;

        nameField.setText(product.getName());
        descField.setText(product.getDescription());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));

        // image loading
        String path = (product.getImageUrl() != null && !product.getImageUrl().isEmpty())
                ? "/images/" + product.getImageUrl()
                : "/images/placeholder.png";

        URL url = getClass().getResource(path);
        if (url != null) {
            productImage.setImage(new Image(url.toExternalForm()));
        } else {
            System.out.println("⚠ Image not found: " + path);
            productImage.setImage(new Image(getClass().getResource("/images/placeholder.png").toExternalForm()));
        }
    }

    // ============================================================
    private void chooseImage() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choisir une nouvelle image");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fc.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            productImage.setImage(new Image(file.toURI().toString()));
        }
    }

    // ============================================================
    private void saveProduct() {
        try {
            product.setName(nameField.getText());
            product.setDescription(descField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setStock(Integer.parseInt(stockField.getText()));

            // Upload image if user picked one
            if (selectedImageFile != null) {
                String fileName = ProductsApi.uploadImage(selectedImageFile);
                product.setImageUrl(fileName);
            }

            ProductsApi.update(product);

            AppNavigator.navigateTo("dashboard/Dashboard.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Erreur lors de la sauvegarde : " + e.getMessage()).show();
        }
    }
}
