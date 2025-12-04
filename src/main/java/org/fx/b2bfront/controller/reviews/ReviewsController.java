package org.fx.b2bfront.controller.reviews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.dto.ReviewDto;
import org.fx.b2bfront.store.ReviewStore;
import org.fx.b2bfront.store.ProductStore;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class    ReviewsController implements Initializable {

    @FXML private VBox reviewsContainer;
    @FXML private Label productTitle;
    @FXML private ComboBox<Integer> ratingCombo;
    @FXML private TextArea reviewText;
    @FXML private Button btnSubmitReview;

    private long productId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productId = ProductStore.getSelectedProductId();

        productTitle.setText("Avis sur le produit #" + productId);

        ratingCombo.getItems().addAll(1, 2, 3, 4, 5);
        ratingCombo.getSelectionModel().select(4);

        loadReviews();

        btnSubmitReview.setOnAction(e -> submitReview());
    }

    private void loadReviews() {
        reviewsContainer.getChildren().clear();

        for (ReviewDto review : ReviewStore.getReviews(productId)) {
            reviewsContainer.getChildren().add(buildReviewCard(review));
        }
    }

    private VBox buildReviewCard(ReviewDto r) {
        VBox card = new VBox(5);
        card.getStyleClass().add("review-card");
        card.setPadding(new Insets(15));

        Label author = new Label(r.getAuthor() + " — " + r.getDate());
        author.getStyleClass().add("review-author");

        Label rating = new Label("⭐".repeat(r.getRating()));
        rating.getStyleClass().add("review-stars");

        Label content = new Label(r.getContent());
        content.setWrapText(true);
        content.getStyleClass().add("review-content");

        card.getChildren().addAll(author, rating, content);
        return card;
    }

    private void submitReview() {
        if (reviewText.getText().isBlank()) return;

        ReviewDto newReview = new ReviewDto(
                "Utilisateur",
                ratingCombo.getValue(),
                reviewText.getText(),
                LocalDate.now().toString()
        );

        ReviewStore.addReview(productId, newReview);

        reviewText.clear();
        loadReviews();
    }
}
