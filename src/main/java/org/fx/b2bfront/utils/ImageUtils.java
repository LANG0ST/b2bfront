package org.fx.b2bfront.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageUtils {

    public static void loadImage(ImageView view, String url, double w, double h) {
        try {
            Image img = new Image(url, false);
            view.setImage(img);
            view.setFitWidth(w);
            view.setFitHeight(h);
            view.setPreserveRatio(true);
            view.setSmooth(true);
            view.setCache(true);
        } catch (Exception e) {
            System.out.println("Image load error: " + e.getMessage());
        }
    }
}