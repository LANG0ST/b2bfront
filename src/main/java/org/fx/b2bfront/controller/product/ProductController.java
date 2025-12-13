package org.fx.b2bfront.controller.product;

import javafx.application.Platform;
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
import javafx.scene.shape.Rectangle;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.api.ReviewsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.dto.ReviewDto;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.store.ProductStore;
import org.fx.b2bfront.utils.AppNavigator;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    // IMAGE
    @FXML private Pane imagePane;

    // INFO
    @FXML private Label productNameLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productDescriptionLabel;

    // SPECS
    @FXML private Label specType;
    @FXML private Label specPower;
    @FXML private Label specWeight;
    @FXML private Label specFuel;

    // QUANTITY
    @FXML private Label quantityLabel;
    @FXML private Button btnMinus;
    @FXML private Button btnPlus;

    // ACTIONS
    @FXML private Button btnAddToCart;
    @FXML private Button btnBuyNow;
    @FXML private Button btnSeeAllReviews;

    // REVIEWS
    @FXML private HBox reviewsContainer;

    private int quantity = 1;
    private ProductDto currentProduct;

    // =========================================================
    // INITIALIZE
    // =========================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Long productId = ProductStore.getSelectedProductId();
        if (productId != null) {
            loadProduct(productId);
            loadLatestReviews(productId);
        }

        btnAddToCart.setOnAction(e -> {
            if (currentProduct == null) return;
            CartStore.add(currentProduct.getId(), quantity);
            ProductsApi.addToCart(AuthStore.companyId, currentProduct.getId(), quantity);
        });

        btnBuyNow.setOnAction(e -> {
            if (currentProduct == null) return;
            CartStore.add(currentProduct.getId(), quantity);
            AppNavigator.navigateTo("cart.fxml");
        });

        btnSeeAllReviews.setOnAction(e -> {
            if (currentProduct == null) return;
            ProductStore.setSelectedProductId(currentProduct.getId());
            AppNavigator.navigateTo("reviews.fxml");
        });
    }

    // =========================================================
    // QUANTITY (FXML ACTIONS — THIS FIXES YOUR ERROR)
    // =========================================================
    @FXML
    public void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            quantityLabel.setText(String.valueOf(quantity));
        }
    }

    @FXML
    public void increaseQuantity() {
        quantity++;
        quantityLabel.setText(String.valueOf(quantity));
    }

    // =========================================================
    // PRODUCT
    // =========================================================
    private void loadProduct(long productId) {
        currentProduct = ProductsApi.getById(productId);

        if (currentProduct == null) return;

        productNameLabel.setText(currentProduct.getName());
        productPriceLabel.setText(String.format("%.2f MAD", currentProduct.getPrice()));
        productDescriptionLabel.setText(
                currentProduct.getDescription() != null ? currentProduct.getDescription() : ""
        );

        applySpecs(currentProduct.getFilterTag());
        loadImage(currentProduct.getImageUrl());
    }

    // =========================================================
    // SPECS FROM filterTag
    // =========================================================
    private void applySpecs(String filterTag) {
        Map<String, String> specs = parseFilterTag(filterTag);

        specType.setText(specs.getOrDefault("brand", "—"));
        specPower.setText(specs.getOrDefault("power", "—"));
        specWeight.setText(specs.getOrDefault("weight", "—"));
        specFuel.setText(specs.getOrDefault("fuel", "—"));
    }

    private Map<String, String> parseFilterTag(String tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == null || tag.isBlank()) return map;

        for (String pair : tag.split(";")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(kv[0].toLowerCase().trim(), kv[1].trim());
            }
        }
        return map;
    }

    // =========================================================
    // IMAGE
    // =========================================================
    private void loadImage(String url) {
        if (url == null || url.isBlank()) {
            loadPlaceholder();
            return;
        }

        if (url.startsWith("/")) {
            url = "http://localhost:8082" + url;
        }

        ImageView iv = new ImageView(new Image(url, true));
        applyCover(iv);
        imagePane.getChildren().setAll(iv);
    }

    private void loadPlaceholder() {
        ImageView iv = new ImageView(
                new Image(getClass().getResource("/images/placeholder.png").toExternalForm())
        );
        applyCover(iv);
        imagePane.getChildren().setAll(iv);
    }

    private void applyCover(ImageView iv) {
        iv.setPreserveRatio(false);
        iv.fitWidthProperty().bind(imagePane.widthProperty());
        iv.fitHeightProperty().bind(imagePane.heightProperty());

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(imagePane.widthProperty());
        clip.heightProperty().bind(imagePane.heightProperty());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        iv.setClip(clip);
    }

    // =========================================================
    // REVIEWS (BACKEND – 3 LATEST)
    // =========================================================
    private void loadLatestReviews(long productId) {
        reviewsContainer.getChildren().clear();

        new Thread(() -> {
            try {
                List<ReviewDto> reviews =
                        ReviewsApi.getLatestForProduct(productId, 3);

                Platform.runLater(() -> {

                    if (reviews == null || reviews.isEmpty()) {
                        reviewsContainer.getChildren().add(
                                new Label("Aucun avis pour ce produit.")
                        );
                        return;
                    }

                    for (ReviewDto r : reviews) {
                        reviewsContainer.getChildren().add(buildReviewCard(r));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private VBox buildReviewCard(ReviewDto r) {

        VBox card = new VBox(6);
        card.getStyleClass().add("review-card");
        card.setPadding(new Insets(12));
        card.setPrefWidth(260);

        Label author = new Label(
                r.getAuthor() != null ? r.getAuthor() : "Client"
        );
        author.getStyleClass().add("review-author");

        Label stars = new Label("★".repeat(Math.max(1, r.getRating())));
        stars.getStyleClass().add("review-stars");

        Label content = new Label(
                r.getContent() != null ? r.getContent() : ""
        );
        content.setWrapText(true);
        content.getStyleClass().add("review-text");

        card.getChildren().addAll(author, stars, content);
        return card;
    }
}
