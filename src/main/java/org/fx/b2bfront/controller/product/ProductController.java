package org.fx.b2bfront.controller.product;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.ProductStore;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    // IMAGE AREA
    @FXML private Pane imagePane;

    // PRODUCT INFO
    @FXML private Label productNameLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productDescriptionLabel;

    // SPECS (for now we fake / derive them)
    @FXML private Label specType;
    @FXML private Label specPower;
    @FXML private Label specWeight;
    @FXML private Label specFuel;

    // QUANTITY
    @FXML private Label quantityLabel;
    @FXML private Button btnMinus;
    @FXML private Button btnPlus;

    // ACTION BUTTONS
    @FXML private Button btnAddToCart;
    @FXML private Button btnBuyNow;

    // REVIEWS
    @FXML private HBox reviewsContainer;

    private int quantity = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupQuantityButtons();

        Long productId = ProductStore.getSelectedProductId();
        System.out.println("Loaded productId: " + productId);  // add this
        if (productId != null) {
            loadProductFromBackend(productId);
            ProductDto dto = ProductsApi.getById(productId);
            System.out.println("👉 Image URL received: " + dto.getImageUrl());

        } else {
            // fallback: demo data if nothing selected
            loadFallbackDemo();
        }

        loadReviewsDemo();  // still fake for now
    }

    // ============================================================
    // LOAD PRODUCT FROM BACKEND
    // ============================================================
    private void loadProductFromBackend(long productId) {
        try {
            ProductDto dto = ProductsApi.getById(productId);
            if (dto == null) {
                loadFallbackDemo();
                return;
            }

            // Basic fields
            productNameLabel.setText(dto.getName());
            productDescriptionLabel.setText(dto.getDescription() != null ? dto.getDescription() : "");

            // Price display (you can improve formatting later)
            productPriceLabel.setText(String.format("%.2f MAD", dto.getPrice()));

            // Fake specs for now (you can later extend ProductDto / backend)
            specType.setText(dto.getFilterTag() != null ? dto.getFilterTag() : "Matériel industriel");
            specPower.setText("—");  // to be mapped when you have real fields
            specWeight.setText("—");
            specFuel.setText("—");

            // Image
            if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
                loadImageFromUrl(dto.getImageUrl());
            } else {
                loadPlaceholderImage();
            }

        } catch (Exception e) {
            // If error (backend down, etc.) → show fallback
            e.printStackTrace();
            loadFallbackDemo();
        }
    }

    // ============================================================
    // IMAGE HANDLING
    // ============================================================
    private void loadImageFromUrl(String url) {

        try {
            // No URL provided → use placeholder
            if (url == null || url.isBlank()) {
                loadPlaceholderImage();
                return;
            }

            // Case 1: The backend gives only filename → use placeholder
            if (!url.startsWith("http://") && !url.startsWith("https://") &&
                    !url.startsWith("/")) {
                System.out.println("⚠ Not a real URL, using placeholder instead: " + url);
                loadPlaceholderImage();
                return;
            }

            // Case 2: Backend gives "/uploads/x.jpg"
            if (url.startsWith("/")) {
                url = "http://localhost:8082" + url;
            }

            System.out.println("📷 Final resolved URL = " + url);

            Image img = new Image(url, true);

            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);
            iv.setFitWidth(500);
            iv.setFitHeight(400);

            imagePane.getChildren().clear();
            imagePane.getChildren().add(iv);

        } catch (Exception e) {
            e.printStackTrace();
            loadPlaceholderImage();
        }
    }

    private void loadPlaceholderImage() {
        try {
            Image img = new Image(
                    getClass().getResource("/images/placeholder.png").toExternalForm()
            );

            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);
            iv.setFitWidth(500);
            iv.setFitHeight(400);

            imagePane.getChildren().clear();
            imagePane.getChildren().add(iv);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Placeholder image missing! Add /assets/placeholder.png");
        }
    }

    // ============================================================
    // QUANTITY BUTTONS
    // ============================================================
    private void setupQuantityButtons() {
        btnMinus.setOnAction(e -> decreaseQuantity());
        btnPlus.setOnAction(e -> increaseQuantity());
    }

    @FXML
    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            quantityLabel.setText(String.valueOf(quantity));
        }
    }

    @FXML
    private void increaseQuantity() {
        quantity++;
        quantityLabel.setText(String.valueOf(quantity));
    }

    // ============================================================
    // FALLBACK DEMO (if backend fails / no ID)
    // ============================================================
    private void loadFallbackDemo() {
        productNameLabel.setText("Pelleteuse Caterpillar 320D");
        productPriceLabel.setText("350 000 MAD");
        productDescriptionLabel.setText(
                "Une machine robuste idéale pour chantiers lourds, " +
                        "conçue pour offrir stabilité, puissance et précision."
        );

        specType.setText("Engin de terrassement");
        specPower.setText("162 kW");
        specWeight.setText("21 tonnes");
        specFuel.setText("Diesel");

        loadPlaceholderImage();
    }

    // ============================================================
    // REVIEWS (still fake for now)
    // ============================================================
    private void loadReviewsDemo() {
        reviewsContainer.getChildren().clear();

        for (int i = 0; i < 3; i++) {
            VBox card = createReviewCard(
                    "Client " + (i + 1),
                    "Très satisfait de la qualité, livraison rapide."
            );
            reviewsContainer.getChildren().add(card);
        }
    }

    private VBox createReviewCard(String author, String text) {
        VBox root = new VBox(5);
        root.getStyleClass().add("review-card");
        root.setPadding(new Insets(10));
        root.setPrefWidth(260);

        Label name = new Label(author);
        name.getStyleClass().add("review-author");

        Label content = new Label(text);
        content.setWrapText(true);
        content.getStyleClass().add("review-text");

        root.getChildren().addAll(name, content);
        return root;
    }
}
