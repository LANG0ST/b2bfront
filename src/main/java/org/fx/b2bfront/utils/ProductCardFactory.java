package org.fx.b2bfront.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.fx.b2bfront.dto.ProductDto;

public class ProductCardFactory {

    // ----------------------------------------
    // SMALL CARD → HOMEPAGE
    // ----------------------------------------
    public static VBox buildSmall(ProductDto product) {
        return build(product, 230, 130, 260);
    }

    // ----------------------------------------
    // LARGE CARD → CATEGORIES PAGE
    // ----------------------------------------
    public static VBox buildLarge(ProductDto product) {
        return build(product, 275, 160, 300);
    }


    // ----------------------------------------
    // INTERNAL UNIVERSAL BUILDER
    // ----------------------------------------
    private static VBox build(ProductDto product, double imgW, double imgH, double cardWidth) {

        VBox card = new VBox(8);
        card.getStyleClass().add("product-card");
        card.setPadding(new Insets(10));
        card.setCursor(Cursor.HAND);
        card.setAlignment(Pos.TOP_LEFT);

        // FIX CARD WIDTH (height is auto)
        card.setPrefWidth(cardWidth);
        card.setMinWidth(cardWidth);
        card.setMaxWidth(cardWidth);

        // =======================
        // IMAGE WRAPPER
        // =======================
        Pane imageWrapper = new Pane();
        imageWrapper.getStyleClass().add("product-image");

        imageWrapper.setPrefSize(imgW, imgH);
        imageWrapper.setMinSize(imgW, imgH);
        imageWrapper.setMaxSize(imgW, imgH);

        // load image
        ImageView iv = loadImage(product);
        applyCover(iv, imgW, imgH);

        iv.setLayoutX(0);
        iv.setLayoutY(0);
        imageWrapper.getChildren().add(iv);

        // =======================
        // NAME
        // =======================
        Label name = new Label(product.getName());
        name.getStyleClass().add("product-name");

        // =======================
        // PRICE
        // =======================
        Label price = new Label(product.getPrice() + " DH");
        price.getStyleClass().add("product-price");

        // =======================
        // STOCK
        // =======================
        Label stock = new Label("Stock: " + product.getStock());
        stock.getStyleClass().add("product-stock");

        card.getChildren().addAll(imageWrapper, name, price, stock);

        return card;
    }


    // ----------------------------------------
    // LOAD IMAGE (Cloudinary or placeholder)
    // ----------------------------------------
    private static ImageView loadImage(ProductDto product) {

        String url = product.getImageUrl();

        Image img;

        if (url == null || url.isBlank() ||
                !(url.startsWith("http://") || url.startsWith("https://"))) {

            img = new Image(
                    ProductCardFactory.class.getResource("/images/placeholder.png")
                            .toExternalForm()
            );

        } else {
            img = new Image(url, true);
        }

        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(false);
        iv.setSmooth(true);

        return iv;
    }



    // ----------------------------------------
    // COVER MODE
    // ----------------------------------------
    private static void applyCover(ImageView iv, double w, double h) {

        if (iv.getImage() == null) {
            iv.setFitWidth(w);
            iv.setFitHeight(h);
            return;
        }

        double imgW = iv.getImage().getWidth();
        double imgH = iv.getImage().getHeight();

        if (imgW <= 0 || imgH <= 0) {
            iv.setFitWidth(w);
            iv.setFitHeight(h);
            return;
        }

        double scale = Math.max(w / imgW, h / imgH);

        iv.setFitWidth(imgW * scale);
        iv.setFitHeight(imgH * scale);

        Rectangle clip = new Rectangle(w, h);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        iv.setClip(clip);
    }
}
