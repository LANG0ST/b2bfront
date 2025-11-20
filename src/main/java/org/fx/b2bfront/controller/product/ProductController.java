package org.fx.b2bfront.controller.product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductController {

    // Main product fields
    @FXML private Label productNameLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productDescriptionLabel;

    @FXML private Label specType;
    @FXML private Label specPower;
    @FXML private Label specWeight;
    @FXML private Label specFuel;

    // Quantity
    @FXML private Label quantityLabel;
    @FXML private Button btnMinus;
    @FXML private Button btnPlus;

    // Reviews container
    @FXML private HBox reviewsContainer;

    private int quantity = 1;

    @FXML
    public void initialize() {
        // For now: hard-coded product — later you inject data from backend
        loadSampleProduct();
        buildSampleReviews();
        updateQuantityLabel();
    }

    // =========================
    // SAMPLE DATA
    // =========================
    private void loadSampleProduct() {
        productNameLabel.setText("Pelleteuse Caterpillar 320D");
        productPriceLabel.setText("350 000 MAD");
        productDescriptionLabel.setText(
                "Pelleteuse hydraulique de chantier adaptée aux grands travaux " +
                        "de terrassement (stades, routes, plateformes logistiques). " +
                        "Faible consommation, cabine climatisée, entretien simplifié."
        );

        specType.setText("Engin de terrassement");
        specPower.setText("162 kW");
        specWeight.setText("21 tonnes");
        specFuel.setText("Diesel");
    }

    private void buildSampleReviews() {
        reviewsContainer.getChildren().clear();

        reviewsContainer.getChildren().add(createReviewCard(
                "Société Atlas BTP",
                "Très bonne machine, idéale pour les grands chantiers.",
                "★★★★★"
        ));

        reviewsContainer.getChildren().add(createReviewCard(
                "Entreprise Casa Infra",
                "Consommation correcte, entretien facile, service après-vente réactif.",
                "★★★★☆"
        ));

        reviewsContainer.getChildren().add(createReviewCard(
                "Groupe Nord Travaux",
                "Bon rendement sur sols difficiles, confort opérateur appréciable.",
                "★★★★★"
        ));
    }

    private VBox createReviewCard(String author, String body, String stars) {
        VBox card = new VBox(6);
        card.getStyleClass().add("review-card");

        Label starsLabel = new Label(stars);
        starsLabel.getStyleClass().add("review-stars");

        Label titleLabel = new Label("Avis client");
        titleLabel.getStyleClass().add("review-title");

        Label bodyLabel = new Label(body);
        bodyLabel.setWrapText(true);
        bodyLabel.getStyleClass().add("review-body");

        Label authorLabel = new Label(author);
        authorLabel.getStyleClass().add("review-author");

        card.getChildren().addAll(starsLabel, titleLabel, bodyLabel, authorLabel);
        return card;
    }

    // =========================
    // QUANTITY HANDLERS
    // =========================
    @FXML
    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            updateQuantityLabel();
        }
    }

    @FXML
    private void increaseQuantity() {
        quantity++;
        updateQuantityLabel();
    }

    private void updateQuantityLabel() {
        quantityLabel.setText(String.valueOf(quantity));
    }
}
