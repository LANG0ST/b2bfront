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
import javafx.scene.shape.Rectangle;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.store.ProductStore;
import org.fx.b2bfront.utils.AppNavigator;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    // IMAGE AREA
    @FXML private Pane imagePane;

    // PRODUCT INFO
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

    // ACTION BUTTONS
    @FXML private Button btnAddToCart;
    @FXML private Button btnBuyNow;
    @FXML private Button btnSeeAllReviews;

    // REVIEWS
    @FXML private HBox reviewsContainer;

    private int quantity = 1;

    private ProductDto currentProduct;
    private long currentProductId;
    private long companyId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupQuantityButtons();

        Long productId = ProductStore.getSelectedProductId();
        System.out.println("Loaded productId: " + productId);

        if (productId != null) {
            loadProductFromBackend(productId);
        } else {
            loadFallbackDemo();
        }

        loadReviewsDemo();

        // ==============================
        //  GET COMPANY ID SAFELY
        // ==============================
        Long companyIdObj = AuthStore.companyId;   // <---- use AuthStore
        long companyId = (companyIdObj != null) ? companyIdObj : -1;

        // =====================================================
        //  ADD TO CART
        // =====================================================
        btnAddToCart.setOnAction(e -> {
            if (currentProduct != null) {

                CartStore.add(productId, quantity);            // local store
                ProductsApi.addToCart(companyId, productId, quantity);   // backend sync

                System.out.println("→ Added to cart: " + currentProduct.getName() +
                        " x" + quantity);
            }
        });

        // =====================================================
        //  BUY NOW → add then navigate
        // =====================================================
        btnBuyNow.setOnAction(e -> {
            if (currentProduct != null) {
                CartStore.add(currentProduct.getId(), quantity);
                AppNavigator.navigateTo("cart.fxml");
            }
        });

        btnSeeAllReviews.setOnAction(e -> {
            ProductStore.setSelectedProductId(currentProduct.getId());
            AppNavigator.navigateTo("reviews.fxml");
        });

    }


    // ============================================================
    // LOAD PRODUCT FROM BACKEND
    // ============================================================
    private void loadProductFromBackend(long productId) {
        try {
            currentProduct = ProductsApi.getById(productId);

            if (currentProduct == null) {
                loadFallbackDemo();
                return;
            }

            productNameLabel.setText(currentProduct.getName());
            productDescriptionLabel.setText(
                    currentProduct.getDescription() != null
                            ? currentProduct.getDescription()
                            : ""
            );

            productPriceLabel.setText(String.format("%.2f MAD", currentProduct.getPrice()));

            // Fake specs
            specType.setText(currentProduct.getFilterTag() != null
                    ? currentProduct.getFilterTag()
                    : "Matériel industriel");
            specPower.setText("—");
            specWeight.setText("—");
            specFuel.setText("—");

            if (currentProduct.getImageUrl() != null &&
                    !currentProduct.getImageUrl().isBlank()) {

                loadImageFromUrl(currentProduct.getImageUrl());
            } else {
                loadPlaceholderImage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            loadFallbackDemo();
        }
    }


    // ============================================================
// IMAGE HANDLING (COVER MODE)
// ============================================================
    private void loadImageFromUrl(String url) {

        try {
            if (url == null || url.isBlank()) {
                loadPlaceholderImage();
                return;
            }

            if (!url.startsWith("http://") &&
                    !url.startsWith("https://") &&
                    !url.startsWith("/")) {

                System.out.println("⚠ Not a valid URL → using placeholder: " + url);
                loadPlaceholderImage();
                return;
            }

            if (url.startsWith("/")) {
                url = "http://localhost:8082" + url;
            }

            Image img = new Image(url, true);
            ImageView iv = new ImageView(img);

            applyCover(iv);

            imagePane.getChildren().setAll(iv);

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
            applyCover(iv);

            imagePane.getChildren().setAll(iv);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Placeholder image missing in /images/placeholder.png");
        }
    }


    // ============================================================
    // QUANTITY
    // ============================================================
    private void setupQuantityButtons() {
        btnMinus.setOnAction(e -> decreaseQuantity());
        btnPlus.setOnAction(e -> increaseQuantity());
    }

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


    // ============================================================
    // FALLBACK
    // ============================================================
    private void loadFallbackDemo() {

        currentProduct = new ProductDto();
        currentProduct.setId(-1L);
        currentProduct.setName("Pelleteuse Caterpillar 320D");
        currentProduct.setPrice(350000);

        productNameLabel.setText(currentProduct.getName());
        productPriceLabel.setText("350 000 MAD");
        productDescriptionLabel.setText(
                "Une machine robuste idéale pour chantiers lourds."
        );

        specType.setText("Engin de terrassement");
        specPower.setText("162 kW");
        specWeight.setText("21 tonnes");
        specFuel.setText("Diesel");

        loadPlaceholderImage();
    }


    // ============================================================
    // REVIEWS (FAKE)
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

    /**
     * Makes the ImageView behave like CSS object-fit: cover.
     * It fills the pane completely, cropping overflow.
     */
    private void applyCover(ImageView iv) {

        iv.setPreserveRatio(false);   // allow stretching
        iv.setSmooth(true);

        // Let ImageView expand with the pane
        iv.fitWidthProperty().bind(imagePane.widthProperty());
        iv.fitHeightProperty().bind(imagePane.heightProperty());

        // Rounded clipping mask
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(imagePane.widthProperty());
        clip.heightProperty().bind(imagePane.heightProperty());
        clip.setArcWidth(20);
        clip.setArcHeight(20);

        iv.setClip(clip);
    }

}