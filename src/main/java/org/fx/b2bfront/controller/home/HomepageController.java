package org.fx.b2bfront.controller.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.CategoryApi;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.CategoryDto;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.ProductStore;
import org.fx.b2bfront.utils.AppNavigator;

import java.util.List;
import java.util.Map;

public class HomepageController {

    @FXML private VBox sidebarCategories;
    @FXML private FlowPane productsContainer;
    @FXML private HBox featuredContainer;
    @FXML private Label trendingLabel;


    // --------------------------------------------------
    // INITIALIZATION
    // --------------------------------------------------
    public void initialize() {
        loadCategories();
        loadTrendingProducts();
    }


    // ==================================================
    // 1) CATEGORIES SIDEBAR
    // ==================================================
    private void loadCategories() {
        new Thread(() -> {
            try {
                List<CategoryDto> categories = CategoryApi.getAll();

                Platform.runLater(() -> {
                    sidebarCategories.getChildren().clear();

                    Label title = new Label("Categories");
                    title.getStyleClass().add("sidebar-title");
                    sidebarCategories.getChildren().add(title);

                    for (CategoryDto cat : categories) {
                        Button btn = new Button(cat.getName());
                        btn.getStyleClass().add("category-btn");

                        btn.setOnAction(evt -> {
                            AppNavigator.navigateToWithParams(
                                    "Categories.fxml",
                                    Map.of(
                                            "categoryId", cat.getId(),
                                            "categoryName", cat.getName()
                                    )
                            );
                        });

                        sidebarCategories.getChildren().add(btn);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    // ==================================================
    // 2) TRENDING PRODUCTS (LIMIT 4)
    // ==================================================
    private void loadTrendingProducts() {
        new Thread(() -> {
            try {
                List<ProductDto> trending = ProductsApi.getTrending();

                List<ProductDto> limited = trending.stream()
                        .limit(4)
                        .toList();

                Platform.runLater(() -> {
                    productsContainer.getChildren().clear();
                    limited.forEach(this::addProductCard);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    // ==================================================
    // 3) OPTIONAL: LOAD PRODUCTS BY CATEGORY
    // ==================================================
    private void loadProductsByCategory(int categoryId) {
        new Thread(() -> {
            try {
                List<ProductDto> list = ProductsApi.getByCategory(categoryId);

                Platform.runLater(() -> {
                    productsContainer.getChildren().clear();
                    list.forEach(this::addProductCard);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    // ==================================================
    // 4) BUILD PRODUCT CARD
    // ==================================================
    private void addProductCard(ProductDto product) {

        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setSpacing(10);
        card.setPrefWidth(250);

        // ---------------------------------------
        // IMAGE (placeholder for now)
        // ---------------------------------------
        VBox img = new VBox();
        img.getStyleClass().add("product-image");
        img.setPrefHeight(160);

        // ---------------------------------------
        // NAME
        // ---------------------------------------
        Label nameLabel = new Label(product.getName());
        nameLabel.getStyleClass().add("product-name");

        // ---------------------------------------
        // PRICE
        // ---------------------------------------
        Label priceLabel = new Label(product.getPrice() + " DH");
        priceLabel.getStyleClass().add("product-price");

        // ---------------------------------------
        // STOCK
        // ---------------------------------------
        Label stockLabel = new Label("Stock: " + product.getStock());
        stockLabel.getStyleClass().add("product-stock");

        card.getChildren().addAll(img, nameLabel, priceLabel, stockLabel);

        // ---------------------------------------
        // CLICK → OPEN PRODUCT PAGE ✔ FIXED
        // ---------------------------------------
        card.setOnMouseClicked(event -> openProductPage(product));

        productsContainer.getChildren().add(card);
    }


    // ==================================================
    // 5) NAVIGATE TO PRODUCT PAGE
    // ==================================================
    private void openProductPage(ProductDto product) {
        ProductStore.setSelectedProductId(product.getId());
        AppNavigator.navigateTo("product.fxml");
    }
}
