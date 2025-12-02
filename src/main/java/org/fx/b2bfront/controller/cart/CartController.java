package org.fx.b2bfront.controller.cart;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.fx.b2bfront.dto.CartItem;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.utils.AppNavigator;

public class CartController {

    @FXML private VBox cartItemsContainer;

    @FXML private Label subtotalLabel;
    @FXML private Label totalLabel;

    @FXML private TextField couponField;
    @FXML private Button btnCheckout;

    @FXML
    public void initialize() {
        refresh();

        btnCheckout.setOnAction(e ->
                AppNavigator.navigateTo("checkout.fxml")
        );
    }


    /* ---------------------------------------------------------
       LOAD ALL ITEMS
       --------------------------------------------------------- */
    private void loadCartItems() {
        cartItemsContainer.getChildren().clear();

        for (CartItem item : CartStore.getItems()) {
            cartItemsContainer.getChildren().add(buildCartItem(item));
        }
    }


    /* ---------------------------------------------------------
       BUILD A SINGLE CART ITEM (card)
       --------------------------------------------------------- */
    private HBox buildCartItem(CartItem item) {

        HBox root = new HBox(20);
        root.getStyleClass().add("cart-item-card");
        root.setAlignment(Pos.CENTER_LEFT);

        /* =====================================
           IMAGE
        ===================================== */
        ImageView img = new ImageView();
        img.setFitWidth(140);
        img.setFitHeight(120);
        img.setPreserveRatio(true);

        try {
            if (item.getImageUrl() != null && !item.getImageUrl().isBlank()) {
                img.setImage(new Image("http://localhost:8082/" + item.getImageUrl(), true));
            } else {
                img.setImage(new Image(getClass().getResource("/images/placeholder.png").toExternalForm()));
            }
        } catch (Exception ex) {
            img.setImage(new Image(getClass().getResource("/images/placeholder.png").toExternalForm()));
        }

        VBox imageBox = new VBox(img);
        imageBox.getStyleClass().add("cart-item-image");


        /* =====================================
           NAME + QUANTITY
        ===================================== */
        VBox info = new VBox(10);

        Label name = new Label(item.getName());
        name.getStyleClass().add("cart-item-title");

        HBox qtyBox = new HBox(8);
        qtyBox.setAlignment(Pos.CENTER_LEFT);
        qtyBox.getStyleClass().add("qty-box");

        Button minus = new Button("-");
        minus.getStyleClass().add("qty-button");
        minus.setOnAction(e -> changeQty(item, item.getQuantity() - 1));

        Label qtyValue = new Label(String.valueOf(item.getQuantity()));
        qtyValue.getStyleClass().add("qty-value");

        Button plus = new Button("+");
        plus.getStyleClass().add("qty-button");
        plus.setOnAction(e -> changeQty(item, item.getQuantity() + 1));

        qtyBox.getChildren().addAll(minus, qtyValue, plus);

        info.getChildren().addAll(name, qtyBox);


        /* =====================================
           PRICE
        ===================================== */
        Label price = new Label(
                String.format("%.2f MAD", item.getUnitPrice() * item.getQuantity())
        );
        price.getStyleClass().add("cart-item-price");


        /* =====================================
           REMOVE BUTTON
        ===================================== */
        Button remove = new Button("✖");
        remove.getStyleClass().add("remove-button");
        remove.setOnAction(e -> {
            CartStore.remove(item.getProductId());
            refresh();
        });


        /* =====================================
           ASSEMBLE CARD
        ===================================== */
        root.getChildren().addAll(imageBox, info, price, remove);
        return root;
    }


    /* ---------------------------------------------------------
       UPDATE QTY
       --------------------------------------------------------- */
    private void changeQty(CartItem item, int newQty) {
        if (newQty <= 0) {
            CartStore.remove(item.getProductId());
        } else {
            CartStore.updateQuantity(item.getProductId(), newQty);
        }
        refresh();
    }


    /* ---------------------------------------------------------
       REFRESH UI
       --------------------------------------------------------- */
    private void refresh() {
        loadCartItems();
        subtotalLabel.setText(String.format("%.2f MAD", CartStore.getSubtotal()));
        totalLabel.setText(String.format("%.2f MAD", CartStore.getSubtotal()));
    }
}
