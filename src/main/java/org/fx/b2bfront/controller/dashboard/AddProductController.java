package org.fx.b2bfront.controller.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.utils.AppNavigator;
import org.fx.b2bfront.utils.CategoryAttributes;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductController {

    // ==========================
    // FORM FIELDS
    // ==========================
    @FXML private TextField nameField;
    @FXML private TextArea descField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private ComboBox<String> categoryBox;

    // Dynamic attributes
    @FXML private VBox attributesBox;

    // Image
    @FXML private ImageView imagePreview;

    // Buttons
    @FXML private Button btnSubmit;
    @FXML private Button btnChooseImage;
    @FXML private Button btnBack;

    private File selectedImageFile = null;
    private final Map<String, TextField> attributeInputs = new HashMap<>();

    // ===========================================================
    // INITIALIZE
    // ===========================================================
    public void initialize() {

        categoryBox.getItems().addAll(
                "1 - Large industrial equipment",
                "2 - Building supplies and aggregates",
                "3 - Industrial electrical components",
                "4 - Protection and PPE equipment",
                "5 - Professional tools",
                "6 - Heating, ventilation, and plumbing"
        );

        btnBack.setOnAction(e -> {
            AppNavigator.navigateToWithParams(
                    "dashboard/Dashboard.fxml",
                    Map.of("section", "products")
            );
        });


        btnChooseImage.setOnAction(e -> chooseImage());
        btnSubmit.setOnAction(e -> submit());

        // Build attribute fields when category changes
        categoryBox.setOnAction(e -> buildAttributeFields());
    }

    // ===========================================================
    // DYNAMIC ATTRIBUTE FIELDS
    // ===========================================================
    private void buildAttributeFields() {

        attributesBox.getChildren().clear();
        attributeInputs.clear();

        if (categoryBox.getValue() == null) return;

        int categoryId = Integer.parseInt(categoryBox.getValue().split(" ")[0]);
        List<String> attrs = CategoryAttributes.get(categoryId);

        for (String attr : attrs) {
            Label label = new Label(attr.toUpperCase());
            TextField field = new TextField();
            field.setPromptText("Enter " + attr);

            attributesBox.getChildren().addAll(label, field);
            attributeInputs.put(attr, field);
        }
    }

    // ===========================================================
    // IMAGE ASPECT RATIO VALIDATION
    // ===========================================================
    private boolean isValidAspectRatio(Image img) {

        double w = img.getWidth();
        double h = img.getHeight();
        if (w <= 0 || h <= 0) return false;

        double ratio = w / h;

        // Accept only rectangular images (not square / vertical)
        return ratio >= 1.3 && ratio <= 2.2;
    }

    // ===========================================================
    // IMAGE PICKER
    // ===========================================================
    private void chooseImage() {

        FileChooser fc = new FileChooser();
        fc.setTitle("Choisir une image");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fc.showOpenDialog(null);
        if (file == null) return;

        Image img = new Image(file.toURI().toString());

        if (!isValidAspectRatio(img)) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Format d'image invalide");
            a.setContentText("""
                    L’image doit être rectangulaire (plus large que haute).

                    ✔ 1200×700  (OK)
                    ❌ 800×800  (trop carré)
                    ❌ 700×1100 (trop verticale)
                    """);
            a.show();
            return;
        }

        selectedImageFile = file;
        imagePreview.setImage(img);
    }

    // ===========================================================
    // SUBMIT PRODUCT
    // ===========================================================
    private void submit() {

        try {
            // -------- Basic validation --------
            if (nameField.getText().isBlank()) {
                alert("Le nom est obligatoire.");
                return;
            }

            if (!priceField.getText().matches("\\d+(\\.\\d+)?")) {
                alert("Prix invalide.");
                return;
            }

            if (!stockField.getText().matches("\\d+")) {
                alert("Stock invalide.");
                return;
            }

            if (categoryBox.getValue() == null) {
                alert("Veuillez choisir une catégorie.");
                return;
            }

            // -------- Build DTO --------
            ProductDto dto = new ProductDto();
            dto.setName(nameField.getText());
            dto.setDescription(descField.getText());
            dto.setPrice(Double.parseDouble(priceField.getText()));
            dto.setStock(Integer.parseInt(stockField.getText()));
            dto.setCompanyId(AuthStore.companyId);

            // -------- Build structured filterTag --------
            StringBuilder tag = new StringBuilder();

            for (var entry : attributeInputs.entrySet()) {
                String value = entry.getValue().getText().trim();
                if (value.isEmpty()) {
                    alert("Veuillez remplir le champ : " + entry.getKey());
                    return;
                }
                tag.append(entry.getKey())
                        .append("=")
                        .append(value)
                        .append(";");
            }

            dto.setFilterTag(tag.substring(0, tag.length() - 1));

            // -------- Category --------
            int catId = Integer.parseInt(categoryBox.getValue().split(" ")[0]);
            dto.setCategoryId(catId);

            // -------- Image upload --------
            if (selectedImageFile != null) {
                String url = ProductsApi.uploadImage(selectedImageFile);
                dto.setImageUrl(url);
            }

            // -------- Create product --------
            ProductsApi.createProduct(dto);
            AppNavigator.navigateTo("dashboard/Dashboard.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Erreur : " + e.getMessage());
        }
    }

    // ===========================================================
    // ALERT UTIL
    // ===========================================================
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }
}
