package org.fx.b2bfront.controller.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ReviewsController {

    @FXML private VBox reviewsContainer;

    @FXML
    public void initialize() {
        // Later: load reviews from API
        // Example: ProductReviewService.getReviews(productId)
    }
    @FXML
    private Label productTitleLabel;

    public void setProductTitle(String title) {
        productTitleLabel.setText(title);
    }

    @FXML
    private void handleGoBack() {
        // replace this with your real navigation logic
        System.out.println("Going back...");
    }

}
