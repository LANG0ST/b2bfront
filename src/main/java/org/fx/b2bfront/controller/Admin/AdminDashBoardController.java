package org.fx.b2bfront.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.StatsApi;
import org.fx.b2bfront.utils.AppNavigator;

public class AdminDashBoardController {

    // ===== TOP NUMBERS =====
    @FXML
    private Label usersCount, productsCount, ordersCount;

    // ===== TOP 3 SELLERS =====
    @FXML private VBox sellerBox1, sellerBox2, sellerBox3;

    // ===== TOP 3 BUYERS =====
    @FXML private VBox buyerBox1, buyerBox2, buyerBox3;

    // ===== TOP 3 PRODUCTS =====
    @FXML private VBox productBox1, productBox2, productBox3;

    // ===== TOP 3 CATEGORIES =====
    @FXML private VBox catBox1, catBox2, catBox3;

    @FXML
    private BorderPane rootPane;

    private final StatsApi statsApi = new StatsApi();

    @FXML
    public void initialize() {
        loadNumbers();
        loadTop3Sellers();
        loadTop3Buyers();
        loadTop3Products();
        loadTop3Categories();
    }

    // ============================================
    //                NUMBERS
    // ============================================
    private void loadNumbers() {
        usersCount.setText(String.valueOf(StatsApi.getCompaniesActives() != null ? StatsApi.getCompaniesActives() : 0));
        productsCount.setText(String.valueOf(statsApi.getProductsCount() != null ? statsApi.getProductsCount() : 0));
        ordersCount.setText(String.valueOf(statsApi.getOrdersCount() != null ? statsApi.getOrdersCount() : 0));
    }

    // ============================================
    //                TOP 3 SELLERS
    // ============================================
    private void loadTop3Sellers() {
        var sellers = statsApi.top3CompaniesVendeuses();

        fillMiniCard(sellerBox1, sellers.get(0).getCompanyName(), sellers.get(0).getTotal() + " MAD","compa");
        fillMiniCard(sellerBox2, sellers.get(1).getCompanyName(), sellers.get(1).getTotal() + " MAD","compa");
        fillMiniCard(sellerBox3, sellers.get(2).getCompanyName(), sellers.get(2).getTotal() + " MAD","compa");
    }

    // ============================================
    //                TOP 3 BUYERS
    // ============================================
    private void loadTop3Buyers() {
        var buyers = statsApi.top3CompaniesAcheteuses();

        fillMiniCard(buyerBox1, buyers.get(0).getCompanyName(), buyers.get(0).getTotal() + " MAD","compa");
        fillMiniCard(buyerBox2, buyers.get(1).getCompanyName(), buyers.get(1).getTotal() + " MAD","compa");
        fillMiniCard(buyerBox3, buyers.get(2).getCompanyName(), buyers.get(2).getTotal() + " MAD","compa");
    }

    // ============================================
    //                TOP 3 PRODUCTS
    // ============================================
    private void loadTop3Products() {
        var products = statsApi.top3Products();

        fillMiniCard(productBox1, products.get(0).getProductName(), products.get(0).getTotal() + " MAD","prod");
        fillMiniCard(productBox2, products.get(1).getProductName(), products.get(1).getTotal() + " MAD","prod");
        fillMiniCard(productBox3, products.get(2).getProductName(), products.get(2).getTotal() + " MAD","prod");
    }

    // ============================================
    //                TOP 3 CATEGORIES
    // ============================================
    private void loadTop3Categories() {
        var categories = statsApi.top3Categories();

        fillMiniCard(catBox1, categories.get(0).getCategoryName(), categories.get(0).getTotal() + " MAD","cat");
        fillMiniCard(catBox2, categories.get(1).getCategoryName(), categories.get(1).getTotal() + " MAD","cat");
        fillMiniCard(catBox3, categories.get(2).getCategoryName(), categories.get(2).getTotal() + " MAD","cat");
    }

    // ============================================
    //                MINI CARD BUILDER
    // ============================================
    private void fillMiniCard(VBox card, String title, String value , String type) {
        card.getChildren().clear();
        Label icon ;
        if (type == "compa"){
            icon = new Label("\uD83C\uDFE2");
        } else if (type == "prod") {
            icon = new Label("\uD83D\uDCE6");
        }else {
            icon = new Label("\uD83D\uDDC2");

        }
        Label name = new Label(title);
        Label val  = new Label(value);
        icon.getStyleClass().add("stats-icon-d");
        name.getStyleClass().add("stats-title-d");
        val.getStyleClass().add("stats-value-d");

        card.getChildren().addAll(icon , name, val);
    }

    // ============================================
    //                NAVIGATION (More →)
    // ============================================
    @FXML private void openAllSellers() {
        AppNavigator.navigateTo("Admin/AllSellers.fxml");
    }

    @FXML private void openAllBuyers() {
        AppNavigator.navigateTo("Admin/AllBuyers.fxml");
    }

    @FXML private void openAllProducts() {
        AppNavigator.navigateTo("Admin/AllProducts.fxml");
    }

    @FXML private void openAllCategories() {
        AppNavigator.navigateTo("Admin/AllCategories.fxml");
    }

    // ============================================
    //                NAVIGATION (Sidebar)
    // ============================================
    @FXML
    private void openDashboard(MouseEvent event) {
        AppNavigator.navigateTo("Admin/AdminDashBoard.fxml");
    }

    @FXML
    private void openUsersModule(MouseEvent event) {
        AppNavigator.navigateTo("Admin/GestionUsers.fxml");
    }

    @FXML
    private void openProductsModule(MouseEvent event) {
        AppNavigator.navigateTo("Admin/GestionProducts.fxml");
    }

    @FXML
    private void openAuthpage(MouseEvent event) {
        AppNavigator.navigateTo("auth.fxml");
    }
}
