package org.fx.b2bfront.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import org.fx.b2bfront.dto.ProductDto;

public class ProductCardFactory {

    private static ImageView loadCardImage(ProductDto product) {

        String url = product.getImageUrl();

        // Always use placeholder if:
        // - null
        // - blank
        // - just a filename
        // - not http/https
        boolean invalid =
                (url == null || url.isBlank()
                        || !(url.startsWith("http://") || url.startsWith("https://")));

        if (invalid) {
            Image placeholder = new Image(
                    ProductCardFactory.class.getResource("/images/placeholder.png").toExternalForm()
            );

            ImageView iv = new ImageView(placeholder);
            iv.setFitWidth(180);
            iv.setFitHeight(130);
            iv.setPreserveRatio(true);

            return iv;
        }

        // Future: real backend images
        Image networkImage = new Image(url, true);
        ImageView iv = new ImageView(networkImage);
        iv.setFitWidth(180);
        iv.setFitHeight(130);
        iv.setPreserveRatio(true);

        return iv;
    }

    public static VBox build(ProductDto product) {

        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setSpacing(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.TOP_LEFT);
        card.setCursor(Cursor.HAND);

        // =====================================================
        //               PRODUCT IMAGE
        // =====================================================
        Pane imageWrapper = new Pane();
        imageWrapper.getStyleClass().add("product-image");
        imageWrapper.setPrefHeight(150);

        ImageView imgView = loadCardImage(product);
        imgView.setFitHeight(150);
        imgView.setPreserveRatio(true);

        // ensure it sits at (0,0)
        imgView.setLayoutX(0);
        imgView.setLayoutY(0);

        imageWrapper.getChildren().add(imgView);

        // =====================================================
        //               PRODUCT NAME
        // =====================================================
        Label nameLabel = new Label(product.getName());
        nameLabel.getStyleClass().add("product-name");

        // =====================================================
        //               PRODUCT PRICE
        // =====================================================
        Label priceLabel = new Label(product.getPrice() + " DH");
        priceLabel.getStyleClass().add("product-price");

        // =====================================================
        //               COMPOSE CARD
        // =====================================================
        card.getChildren().addAll(imageWrapper, nameLabel, priceLabel);

        return card;
    }
}
