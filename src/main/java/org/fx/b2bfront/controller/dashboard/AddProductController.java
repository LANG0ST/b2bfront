package org.fx.b2bfront.controller.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.utils.AppNavigator;

import java.io.File;

public class AddProductController {

    @FXML private TextField nameField;
    @FXML private TextArea descField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private ComboBox<String> categoryBox;
    @FXML private TextField filterTagField;

    @FXML private ImageView imagePreview;

    @FXML private Button btnSubmit;
    @FXML private Button btnChooseImage;
    @FXML private Button btnBack;

    private File selectedImageFile = null;

    //===========================================================
    // INITIALIZE
    //===========================================================
    public void initialize() {

        categoryBox.getItems().addAll(
                "1 - Large industrial equipment",
                "2 - Building supplies and aggregates",
                "3 - Industrial electrical components",
                "4 - Protection and PPE equipment",
                "5 - Professional tools",
                "6 - Heating, ventilation, and plumbing"
        );

        btnBack.setOnAction(e -> AppNavigator.navigateTo("dashboard/Dashboard.fxml"));
        btnChooseImage.setOnAction(e -> chooseImage());
        btnSubmit.setOnAction(e -> submit());
    }

    //===========================================================
    // ASPECT RATIO VALIDATION
    //===========================================================
    private boolean isValidAspectRatio(Image img) {
        double w = img.getWidth();
        double h = img.getHeight();
        if (w <= 0 || h <= 0) return false;

        double ratio = w / h;
        System.out.println("Image ratio = " + ratio);

        // ✔ Accept only rectangular images
        return (ratio >= 1.3 && ratio <= 2.2);
    }

    //===========================================================
    // IMAGE PICKER
    //===========================================================
    private void chooseImage() {

        FileChooser fc = new FileChooser();
        fc.setTitle("Choisir une image");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fc.showOpenDialog(null);
        if (file == null) return;

        Image img = new Image(file.toURI().toString());

        // --- Validate shape (rectangular only) ---
        if (!isValidAspectRatio(img)) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Format d'image invalide");
            a.setContentText("""
                    L’image doit être rectangulaire (plus large que haute).

                    Exemples :
                    ✔ 1200×700 (OK)
                    ❌ 800×800 (trop carré)
                    ❌ 700×1100 (trop verticale)
                    """);
            a.show();
            return;
        }

        selectedImageFile = file;
        imagePreview.setImage(img);
    }

    //===========================================================
    // SUBMIT PRODUCT
    //===========================================================
    private void submit() {
        try {

            if (nameField.getText().isBlank()) { alert("Le nom est obligatoire."); return; }
            if (!priceField.getText().matches("\\d+(\\.\\d+)?")) { alert("Prix invalide."); return; }
            if (!stockField.getText().matches("\\d+")) { alert("Stock invalide."); return; }
            if (categoryBox.getValue() == null) { alert("Veuillez choisir une catégorie."); return; }

            ProductDto dto = new ProductDto();
            dto.setName(nameField.getText());
            dto.setDescription(descField.getText());
            dto.setPrice(Double.parseDouble(priceField.getText()));
            dto.setStock(Integer.parseInt(stockField.getText()));
            dto.setFilterTag(filterTagField.getText());
            dto.setCompanyId(AuthStore.companyId);

            // Extract category ID → before " - "
            int catId = Integer.parseInt(categoryBox.getValue().split(" ")[0]);
            dto.setCategoryId(catId);

            // Upload image if provided
            if (selectedImageFile != null) {
                String url = ProductsApi.uploadImage(selectedImageFile);
                dto.setImageUrl(url);
            }

            ProductsApi.createProduct(dto);
            AppNavigator.navigateTo("dashboard/Dashboard.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Erreur : " + e.getMessage());
        }
    }

    //===========================================================
    // ALERT UTIL
    //===========================================================
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }
}
