package org.fx.b2bfront.controller.reviews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.ReviewsApi;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ReviewDto;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.ProductStore;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewsController implements Initializable {

    @FXML private VBox reviewsContainer;
    @FXML private Label productTitle;
    @FXML private ComboBox<Integer> ratingCombo;
    @FXML private TextArea reviewText;
    @FXML private Button btnSubmitReview;

    private long productId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productId = ProductStore.getSelectedProductId();

        // ============================================
        // LOAD PRODUCT NAME FROM BACKEND
        // ============================================
        try {
            ProductDto product = ProductsApi.getById(productId);

            if (product != null && product.getName() != null) {
                productTitle.setText("Avis pour : " + product.getName());
            } else {
                productTitle.setText("Avis du produit #" + productId);
            }

        } catch (Exception e) {
            productTitle.setText("Avis du produit #" + productId);
            e.printStackTrace();
        }

        // Rating selector
        ratingCombo.getItems().addAll(1, 2, 3, 4, 5);
        ratingCombo.getSelectionModel().select(4);

        loadReviewsFromBackend();

        btnSubmitReview.setOnAction(e -> submitReview());
    }

    // ============================================================
    // LOAD REVIEWS FROM BACKEND
    // ============================================================
    private void loadReviewsFromBackend() {

        reviewsContainer.getChildren().clear();

        new Thread(() -> {
            try {
                List<ReviewDto> list = ReviewsApi.getAllForProduct(productId);

                javafx.application.Platform.runLater(() -> {
                    reviewsContainer.getChildren().clear();
                    for (ReviewDto r : list) {
                        reviewsContainer.getChildren().add(buildReviewCard(r));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ============================================================
    // BUILD ONE REVIEW CARD
    // ============================================================
    private VBox buildReviewCard(ReviewDto r) {
        VBox card = new VBox(5);
        card.getStyleClass().add("review-card");
        card.setPadding(new Insets(15));

        Label author = new Label(
                r.getAuthor() + " — " +
                        r.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        author.getStyleClass().add("review-author");

        Label stars = new Label("⭐".repeat(r.getRating()));
        stars.getStyleClass().add("review-stars");

        Label text = new Label(r.getContent());
        text.setWrapText(true);
        text.getStyleClass().add("review-content");

        card.getChildren().addAll(author, stars, text);
        return card;
    }

    // ============================================================
    // SUBMIT A REVIEW
    // ============================================================
    private void submitReview() {

        if (reviewText.getText().isBlank()) return;

        int rating = ratingCombo.getValue();
        String comment = reviewText.getText();

        new Thread(() -> {
            try {
                ReviewsApi.addReview(
                        productId,
                        AuthStore.companyId, // logged-in company ID
                        rating,
                        comment
                );

                javafx.application.Platform.runLater(() -> {
                    reviewText.clear();
                    loadReviewsFromBackend();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
