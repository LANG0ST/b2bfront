package org.fx.b2bfront.controller.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.DashboardApi;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.api.NotificationApi;
import org.fx.b2bfront.controller.components.NavbarController;
import org.fx.b2bfront.dto.CommandeDto;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.dto.NotificationDto;
import org.fx.b2bfront.store.AppStore;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.utils.AppNavigator;
import org.fx.b2bfront.utils.ParamReceiver;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, ParamReceiver {

    // --- SECTIONS ---
    @FXML private VBox buyerSection;
    @FXML private VBox sellerSection;
    @FXML private VBox productsSection;
    @FXML private VBox notifSection;

    // --- CONTAINERS ---
    @FXML private VBox buyerOrdersContainer;
    @FXML private VBox sellerOrdersContainer;
    @FXML private VBox productsContainer;
    @FXML private VBox notificationsContainer;

    // --- SIDEBAR ---
    @FXML private Label btnBuyer;
    @FXML private Label btnSeller;
    @FXML private Label btnProducts;
    @FXML private Label btnNotif;
    @FXML
    private StackPane productsContentPane;

    @FXML private Button btnAddProduct;

    private Long companyId;

    // ======================================================
    // INITIALIZE
    // ======================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        companyId = AuthStore.companyId;

        // Default section
        showSection("buyer");

        // Sidebar switching
        btnBuyer.setOnMouseClicked(e -> showSection("buyer"));
        btnSeller.setOnMouseClicked(e -> showSection("seller"));
        btnProducts.setOnMouseClicked(e -> showSection("products"));
        btnNotif.setOnMouseClicked(e -> showSection("notif"));

        btnAddProduct.setOnAction(e -> loadAddProduct());



        // Load all dashboard data
        if (companyId != null) {
            loadBuyerOrders();
            loadSellerOrders();
            loadProducts();
            loadNotifications();
        }
    }

    // ======================================================
    // RECEIVE PARAMS FROM NAVBAR (notifications → notif)
    // ======================================================
    @Override
    public void receiveParams(Map<String, Object> params) {

        if (params == null) return;
        Object sec = params.get("section");
        if (sec == null) return;

        String section = sec.toString().toLowerCase();

        switch (section) {
            case "buyer"    -> showSection("buyer");
            case "seller"   -> showSection("seller");
            case "products" -> showSection("products");
            case "notif"    -> showSection("notif");
        }

    }

    // ======================================================
    // SECTION SWITCHING
    // ======================================================
    private void showSection(String section) {

        buyerSection.setVisible(section.equals("buyer"));
        buyerSection.setManaged(section.equals("buyer"));

        sellerSection.setVisible(section.equals("seller"));
        sellerSection.setManaged(section.equals("seller"));

        productsSection.setVisible(section.equals("products"));
        productsSection.setManaged(section.equals("products"));

        notifSection.setVisible(section.equals("notif"));
        notifSection.setManaged(section.equals("notif"));

        if (section.equals("notif")) {
            markNotificationsAsRead();
        }
    }

    // ======================================================
    // BUYER ORDERS
    // ======================================================
    private void loadBuyerOrders() {

        Platform.runLater(() -> buyerOrdersContainer.getChildren().clear());

        new Thread(() -> {
            try {
                List<CommandeDto> orders = DashboardApi.getBuyerOrders(companyId);
                Collections.reverse(orders);

                Platform.runLater(() -> {
                    if (orders == null || orders.isEmpty()) {
                        buyerOrdersContainer.getChildren().add(new Label("Aucune commande."));
                        return;
                    }

                    int idx = 0;
                    for (CommandeDto c : orders) {
                        buyerOrdersContainer.getChildren().add(buildOrderCard(c, idx++));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ======================================================
    // SELLER ORDERS
    // ======================================================
    private void loadSellerOrders() {

        Platform.runLater(() -> sellerOrdersContainer.getChildren().clear());

        new Thread(() -> {
            try {
                List<CommandeDto> orders = DashboardApi.getSellerOrders(companyId);
                Collections.reverse(orders);

                Platform.runLater(() -> {
                    if (orders == null || orders.isEmpty()) {
                        sellerOrdersContainer.getChildren().add(new Label("Aucune commande reçue."));
                        return;
                    }

                    for (CommandeDto c : orders) {
                        sellerOrdersContainer.getChildren().add(buildSellerOrderCard(c));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ======================================================
    // PRODUCTS
    // ======================================================
    private void loadProducts() {

        Platform.runLater(() -> productsContainer.getChildren().clear());

        new Thread(() -> {
            try {
                List<ProductDto> products = ProductsApi.findAll();

                Platform.runLater(() -> {
                    List<ProductDto> myProducts =
                            products.stream()
                                    .filter(p -> Objects.equals(p.getCompanyId(), companyId))
                                    .toList();

                    if (myProducts.isEmpty()) {
                        productsContainer.getChildren().add(new Label("Aucun produit ajouté."));
                        return;
                    }

                    for (ProductDto p : myProducts) {
                        productsContainer.getChildren().add(buildProductCard(p));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ======================================================
    // NOTIFICATIONS
    // ======================================================
    private void loadNotifications() {

        Platform.runLater(() -> notificationsContainer.getChildren().clear());

        new Thread(() -> {
            try {
                List<NotificationDto> list = NotificationApi.getNotifications(companyId);

                Platform.runLater(() -> {
                    if (list == null || list.isEmpty()) {
                        notificationsContainer.getChildren().add(new Label("Aucune notification."));
                        return;
                    }

                    for (NotificationDto n : list) {
                        notificationsContainer.getChildren().add(buildNotificationCard(n));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ======================================================
    // CARD BUILDERS (NO CHANGE TO YOUR UI)
    // ======================================================
    private VBox buildOrderCard(CommandeDto c, int index) {
        VBox card = new VBox(10);
        card.getStyleClass().add("order-card");
        card.setPadding(new Insets(15));

        card.setStyle(index % 2 == 0
                ? "-fx-background-color: #FFFFFF;"
                : "-fx-background-color: #FFF8EF;");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label ref = new Label("#" + c.getRefCommande());
        ref.getStyleClass().add("order-ref");

        Label date = new Label(c.getDateCommande());
        date.getStyleClass().add("order-date");

        Label status = new Label(c.getStatut());
        status.getStyleClass().add("status-" + c.getStatut().toLowerCase());

        header.getChildren().addAll(ref, date, status);

        VBox itemsBox = new VBox(6);
        itemsBox.setPadding(new Insets(10, 0, 5, 0));

        double total = 0;

        if (c.getLignes() != null) {
            for (var l : c.getLignes()) {
                String txt = "- " + l.getProduitName()
                        + " | Qte: " + l.getQuantite()
                        + " | PU: " + l.getPrixUnitaire() + " DH";

                Label lineLabel = new Label(txt);
                lineLabel.getStyleClass().add("order-line");

                itemsBox.getChildren().add(lineLabel);
                total += l.getPrixUnitaire() * l.getQuantite();
            }
        }

        Label totalLabel = new Label("TOTAL : " + total + " DH");
        totalLabel.getStyleClass().add("order-total");

        card.getChildren().addAll(header, itemsBox, totalLabel);
        return card;
    }

    private VBox buildSellerOrderCard(CommandeDto c) {
        VBox card = new VBox(10);
        card.getStyleClass().add("order-card");
        card.setPadding(new Insets(15));

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label ref = new Label("#" + c.getRefCommande());
        ref.getStyleClass().add("order-ref");

        Label buyer = new Label(
                "Client : " + (c.getCompany() != null ? c.getCompany().getName() : "Inconnu")
        );
        buyer.getStyleClass().add("order-buyer");

        Label date = new Label(c.getDateCommande());
        date.getStyleClass().add("order-date");

        Label status = new Label(c.getStatut());
        status.getStyleClass().add("status-" + c.getStatut().toLowerCase());

        header.getChildren().addAll(ref, buyer, date, status);

        VBox productsBox = new VBox(5);
        productsBox.setPadding(new Insets(10, 0, 10, 0));

        if (c.getLignes() != null && !c.getLignes().isEmpty()) {
            for (var l : c.getLignes()) {
                String txt = "- " + l.getProduitName()
                        + "  | Qte: " + l.getQuantite()
                        + "  | PU: " + l.getPrixUnitaire() + " DH";

                Label lineLabel = new Label(txt);
                lineLabel.getStyleClass().add("order-line");
                productsBox.getChildren().add(lineLabel);
            }
        } else {
            productsBox.getChildren().add(new Label("Aucun produit dans cette commande."));
        }

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button accept = new Button("Accepter");
        Button reject = new Button("Refuser");

        accept.getStyleClass().add("action-btn-green");
        reject.getStyleClass().add("action-btn-red");

        boolean locked = c.getStatut().equals("VALIDEE") || c.getStatut().equals("REFUSEE");

        accept.setDisable(locked);
        reject.setDisable(locked);

        accept.setOnAction(e -> updateOrderStatus(c.getId(), "VALIDEE"));
        reject.setOnAction(e -> updateOrderStatus(c.getId(), "REFUSEE"));

        actions.getChildren().addAll(accept, reject);

        card.getChildren().addAll(header, productsBox, actions);
        return card;
    }

    private void updateOrderStatus(Long id, String newStatus) {

        new Thread(() -> {
            try {
                DashboardApi.updateOrderStatus(id, newStatus);

                Platform.runLater(this::loadSellerOrders);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private HBox buildProductCard(ProductDto p) {

        HBox card = new HBox(20);
        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));

        Image image;

        if (p.getImageUrl() != null && !p.getImageUrl().isBlank()) {
            image = new Image(
                    p.getImageUrl(),   // FULL URL
                    90,
                    90,
                    true,
                    true,
                    true               // background loading
            );
        } else {
            image = new Image(
                    getClass().getResource("/images/placeholder.png").toExternalForm(),
                    90,
                    90,
                    true,
                    true
            );
        }

        ImageView imgView = new ImageView(image);
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setPreserveRatio(true);

        Label name = new Label(p.getName());
        name.getStyleClass().add("product-name");

        Label price = new Label(String.format("%.2f DH", p.getPrice()));
        price.getStyleClass().add("product-price");

        Label stock = new Label("Stock : " + p.getStock());
        stock.getStyleClass().add("product-stock");

        VBox info = new VBox(4, name, price, stock);
        info.setAlignment(Pos.CENTER_LEFT);

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.getStyleClass().add("action-btn-red");

        Button editBtn = new Button("Modifier");
        editBtn.getStyleClass().add("action-btn-orange");

        deleteBtn.setOnAction(e -> deleteProduct(p.getId()));

        editBtn.setOnAction(e -> {
            Map<String, Object> params = new HashMap<>();
            params.put("product", p);
            AppNavigator.navigateToWithParams("dashboard/EditProduct.fxml", params);
        });

        card.getChildren().addAll(imgView, info, deleteBtn, editBtn);
        return card;
    }

    private void deleteProduct(Long id) {

        new Thread(() -> {
            try {
                ProductsApi.delete(id);

                Platform.runLater(() -> {
                    loadProducts();
                    Alert ok = new Alert(Alert.AlertType.INFORMATION);
                    ok.setHeaderText(null);
                    ok.setContentText("Produit supprimé !");
                    ok.show();
                });

            } catch (Exception e) {

                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Suppression impossible");
                    alert.setContentText(e.getMessage().replace("Exception:", ""));
                    alert.show();
                });
            }
        }).start();
    }

    // ======================================================
    // NOTIFICATION CARD
    // ======================================================
    private HBox buildNotificationCard(NotificationDto n) {

        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("notification-card");

        Label dot = new Label("●");
        dot.getStyleClass().add(n.isRead() ? "notif-dot-read" : "notif-dot-unread");

        Label msg = new Label(n.getMessage());
        msg.getStyleClass().add("notif-message");

        box.getChildren().addAll(dot, msg);
        return box;
    }

    private void markNotificationsAsRead() {
        new Thread(() -> {
            try {
                List<NotificationDto> list = NotificationApi.getNotifications(companyId);
                NotificationApi.markAllAsRead(list);

                // Update AppStore
                AppStore.setHasNotifications(false);

                // Update Navbar
                Platform.runLater(() -> {
                    NavbarController navbar = AppNavigator.getNavbarController();
                    if (navbar != null) navbar.refreshNotificationIcon();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void loadAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/dashboard/AddProduct.fxml")
            );

            Parent view = loader.load();

            productsContentPane.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
