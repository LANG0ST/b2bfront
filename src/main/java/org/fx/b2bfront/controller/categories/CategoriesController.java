package org.fx.b2bfront.controller.categories;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.CategoryApi;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.ProductStore;
import org.fx.b2bfront.utils.AppNavigator;
import org.fx.b2bfront.utils.ParamReceiver;
import org.fx.b2bfront.utils.ProductCardFactory;

import java.util.List;
import java.util.Map;

public class CategoriesController implements ParamReceiver {

    @FXML private Label currentCategoryLabel;
    @FXML private VBox filtersBox;
    @FXML private TilePane productsTile;
    @FXML private Label productsTitle;

    private int categoryId;
    private String categoryName;


    // ============================================================
    // RECEIVE PARAMS FROM HOMEPAGE
    // ============================================================
    @Override
    public void receiveParams(Map<String, Object> params) {
        this.categoryId = (int) params.get("categoryId");
        this.categoryName = (String) params.get("categoryName");

        currentCategoryLabel.setText(categoryName);
        productsTitle.setText("Produits — " + categoryName);

        loadFilters();
        loadProducts();
    }


    // ============================================================
    // LOAD FILTER GROUPS
    // ============================================================
    private void loadFilters() {

        new Thread(() -> {
            try {
                Map<String, List<String>> filterGroups = CategoryApi.getFilters(categoryId);

                Platform.runLater(() -> {
                    filtersBox.getChildren().clear();

                    for (String groupName : filterGroups.keySet()) {

                        // Group Title
                        Label title = new Label(groupName.toUpperCase());
                        title.getStyleClass().add("filter-group-title");
                        filtersBox.getChildren().add(title);

                        // Checkboxes
                        for (String value : filterGroups.get(groupName)) {

                            CheckBox cb = new CheckBox(value);
                            cb.getStyleClass().add("filter-checkbox");

                            cb.setOnAction(e -> {
                                if (cb.isSelected()) {
                                    applyFilter(groupName, value);
                                } else {
                                    loadProducts();
                                }
                            });

                            filtersBox.getChildren().add(cb);
                        }

                        filtersBox.getChildren().add(new Separator());
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    // ============================================================
    // APPLY FILTER
    // ============================================================
    private void applyFilter(String group, String value) {

        new Thread(() -> {
            try {
                List<ProductDto> products = ProductsApi.filter(categoryId, group, value);

                Platform.runLater(() -> {
                    productsTile.getChildren().clear();
                    addProductsToTile(products);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    // ============================================================
    // LOAD ALL PRODUCTS IN CATEGORY
    // ============================================================
    private void loadProducts() {
        new Thread(() -> {
            try {
                List<ProductDto> products = ProductsApi.getByCategory(categoryId);

                Platform.runLater(() -> {
                    productsTile.getChildren().clear();
                    addProductsToTile(products);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    // ============================================================
    // ADD PRODUCTS TO TILE WITH CLICK EVENT
    // ============================================================
    private void addProductsToTile(List<ProductDto> products) {

        for (ProductDto p : products) {

            VBox card = ProductCardFactory.build(p);

            // Make card clickable → open product page
            card.setOnMouseClicked(e -> {
                ProductStore.setSelectedProductId(p.getId());
                AppNavigator.navigateTo("product.fxml");
            });

            productsTile.getChildren().add(card);
        }
    }


    // ============================================================
    // CLEAR FILTERS
    // ============================================================
    @FXML
    private void clearFilters() {
        loadFilters();
        loadProducts();
    }
}
