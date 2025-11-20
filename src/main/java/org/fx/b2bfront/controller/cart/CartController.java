package org.fx.b2bfront.controller.cart;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CartController {

    @FXML private VBox cartItemsContainer;

    @FXML private Label subtotalLabel;
    @FXML private Label totalLabel;

    private double subtotal = 0;

    @FXML
    public void initialize() {
        addCartItem("Pelleteuse Caterpillar 320D", "L", 1, 99000);
        addCartItem("Génératrice Industrielle CAT GX3500", "XL", 1, 15000);
        updateTotals();
    }

    private void addCartItem(String name, String size, int qty, double price) {

        HBox root = new HBox(20);
        root.getStyleClass().add("cart-item");
        root.setAlignment(Pos.TOP_LEFT);

        VBox imageBox = new VBox();
        imageBox.getStyleClass().add("item-image");

        VBox details = new VBox(4);
        details.getStyleClass().add("item-details");

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("item-title");

        Label sizeLabel = new Label("Size: " + size);
        Label qtyLabel = new Label("Quantity: " + qty);

        Label priceLabel = new Label(price + " MAD");
        priceLabel.getStyleClass().add("item-price");

        Button removeBtn = new Button("Remove");
        removeBtn.getStyleClass().add("remove-button");

        removeBtn.setOnAction(e -> {
            cartItemsContainer.getChildren().remove(root);
            subtotal -= price;
            updateTotals();
        });

        details.getChildren().addAll(nameLabel, sizeLabel, qtyLabel, priceLabel, removeBtn);

        root.getChildren().addAll(imageBox, details);

        subtotal += price;

        cartItemsContainer.getChildren().add(root);
    }

    private void updateTotals() {
        subtotalLabel.setText(subtotal + " MAD");
        totalLabel.setText(subtotal + " MAD");
    }
}
