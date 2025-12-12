package org.fx.b2bfront.controller.search;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.store.AppStore;
import org.fx.b2bfront.store.ProductStore;
import org.fx.b2bfront.utils.AppNavigator;
import org.fx.b2bfront.utils.ProductCardFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchResultsController implements Initializable {

    @FXML private Label resultTitle;
    @FXML private Label resultCount;
    @FXML private TilePane resultsTile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String query = AppStore.getSearchQuery();

        if (query == null || query.isBlank()) {
            resultTitle.setText("No search query");
            resultCount.setText("");
            return;
        }

        resultTitle.setText("Results for: \"" + query + "\"");

        loadSearch(query);
    }

    private void loadSearch(String query) {

        new Thread(() -> {
            try {
                List<ProductDto> all = ProductsApi.findAll();

                List<ProductDto> filtered = all.stream()
                        .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                        .toList();

                Platform.runLater(() -> {

                    resultsTile.getChildren().clear();

                    resultCount.setText(filtered.size() + " products found");

                    if (filtered.isEmpty()) {
                        Label no = new Label("No products found.");
                        no.setStyle("-fx-font-size: 16; -fx-text-fill: #777;");
                        resultsTile.getChildren().add(no);
                        return;
                    }

                    for (ProductDto p : filtered) {
                        VBox card = ProductCardFactory.buildLarge(p);

                        // CLICK → product page
                        card.setOnMouseClicked(ev -> {
                            ProductStore.setSelectedProductId(p.getId());
                            AppNavigator.navigateTo("product.fxml");
                        });

                        resultsTile.getChildren().add(card);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
