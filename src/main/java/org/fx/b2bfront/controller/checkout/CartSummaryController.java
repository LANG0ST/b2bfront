package org.fx.b2bfront.controller.checkout;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import org.fx.b2bfront.dto.CartItem;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.store.CheckoutStore;

public class CartSummaryController {

    @FXML private VBox cartItemsContainer;
    @FXML private TextField couponField;
    @FXML private Label subtotalLabel;
    @FXML private Label shippingLabel;
    @FXML private Label totalLabel;

    @FXML
    public void initialize() {
        loadItems();
        updateTotals();
    }

    /* ============================================================
       LOAD ITEMS
       ============================================================ */
    private void loadItems() {
        cartItemsContainer.getChildren().clear();

        for (CartItem item : CartStore.getItems()) {
            cartItemsContainer.getChildren().add(buildItemRow(item));
        }
    }

    /* ============================================================
       BUILD A ROW FOR THE SUMMARY
       Image is rendered in true COVER mode
       ============================================================ */
    private HBox buildItemRow(CartItem item) {

        HBox root = new HBox(12);
        root.getStyleClass().add("cart-item-box");
        root.setAlignment(Pos.CENTER_LEFT);

        /* ---------------------------------------------------------
           IMAGE WRAPPER — 60×60 preview with rounded corners
        --------------------------------------------------------- */
        Pane imgWrapper = new Pane();
        imgWrapper.setPrefSize(60, 60);
        imgWrapper.setMinSize(60, 60);
        imgWrapper.setMaxSize(60, 60);
        imgWrapper.getStyleClass().add("summary-image");

        ImageView iv = new ImageView();
        iv.setPreserveRatio(false);
        iv.setSmooth(true);

        try {
            String url = item.getImageUrl();

            Image img;
            if (url != null && !url.isBlank()) {
                img = new Image(url, true);
            } else {
                img = new Image(
                        getClass().getResource("/images/placeholder.png").toExternalForm()
                );
            }
            iv.setImage(img);

        } catch (Exception e) {
            iv.setImage(new Image(
                    getClass().getResource("/images/placeholder.png").toExternalForm()
            ));
        }

        // COVER MODE — scale image beyond bounds then clip
        iv.fitWidthProperty().bind(imgWrapper.widthProperty().multiply(1.4));
        iv.fitHeightProperty().bind(imgWrapper.heightProperty().multiply(1.4));

        Rectangle clip = new Rectangle(60, 60);
        clip.arcWidthProperty().set(10);
        clip.arcHeightProperty().set(10);

        clip.widthProperty().bind(imgWrapper.widthProperty());
        clip.heightProperty().bind(imgWrapper.heightProperty());

        iv.setClip(clip);

        imgWrapper.getChildren().add(iv);

        /* ---------------------------------------------------------
           TEXTS
        --------------------------------------------------------- */
        VBox text = new VBox(2);

        Label name = new Label(item.getName());
        name.getStyleClass().add("cart-item-name");

        Label qty = new Label("x" + item.getQuantity());
        qty.getStyleClass().add("cart-item-qty");

        Label price = new Label(String.format("%.1f MAD", item.getUnitPrice()));
        price.getStyleClass().add("cart-item-price");

        text.getChildren().addAll(name, qty, price);

        /* ---------------------------------------------------------
           ASSEMBLE ROW
        --------------------------------------------------------- */
        root.getChildren().addAll(imgWrapper, text);

        return root;
    }

    /* ============================================================
       TOTALS
       ============================================================ */
    public void updateTotals() {
        double subtotal = CartStore.getSubtotal();
        double shipping = CheckoutStore.shippingCost;
        double total = subtotal + shipping;

        subtotalLabel.setText(String.format("%.1f MAD", subtotal));
        shippingLabel.setText(String.format("%.1f MAD", shipping));
        totalLabel.setText(String.format("%.1f MAD", total));
    }
}
