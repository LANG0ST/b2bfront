package org.fx.b2bfront.controller.categories;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class CategoriesController {

    @FXML private VBox filtersBox;
    @FXML private TilePane productsTile;
    @FXML private Label currentCategoryLabel;
    @FXML private Label productsTitle;

    // For now: hard-coded current category (later: set via navigator)
    private String currentCategory = "Engins & Matériel Lourd";

    @FXML
    public void initialize() {
        loadCategory(currentCategory);
    }

    // =========================
    //  LOAD CATEGORY (FAKE DATA)
    // =========================
    private void loadCategory(String categoryName) {
        currentCategoryLabel.setText(categoryName);
        productsTitle.setText("Produits — " + categoryName);

        filtersBox.getChildren().clear();
        productsTile.getChildren().clear();

        List<FilterGroup> filterGroups = getFiltersForCategory(categoryName);
        List<Product> products = getProductsForCategory(categoryName);

        // Build filter UI
        for (FilterGroup group : filterGroups) {
            Label groupLabel = new Label(group.name());
            groupLabel.getStyleClass().add("filter-group-title");
            filtersBox.getChildren().add(groupLabel);

            for (String option : group.options()) {
                CheckBox cb = new CheckBox(option);
                cb.getStyleClass().add("filter-checkbox");
                filtersBox.getChildren().add(cb);
            }
        }

        // Build product cards
        for (Product p : products) {
            VBox card = createProductCard(p);
            productsTile.getChildren().add(card);
        }
    }

    // =========================
    //  PRODUCT CARD UI
    // =========================
    private VBox createProductCard(Product p) {
        VBox card = new VBox(8);
        card.getStyleClass().add("product-card");

        Pane image = new Pane();
        image.setPrefSize(260, 180);
        image.getStyleClass().add("product-image");

        Label name = new Label(p.name());
        name.getStyleClass().add("product-name");

        HBox bottomRow = new HBox(10);
        bottomRow.getStyleClass().add("product-bottom-row");

        Label price = new Label(String.format("%.0f MAD", p.price()));
        price.getStyleClass().add("product-price");

        Label tag = new Label(p.tag());
        tag.getStyleClass().add("product-tag");

        bottomRow.getChildren().addAll(price, tag);

        card.getChildren().addAll(image, name, bottomRow);
        return card;
    }

    // =========================
    //  FAKE FILTER DATA
    // =========================
    private List<FilterGroup> getFiltersForCategory(String cat) {
        List<FilterGroup> list = new ArrayList<>();

        switch (cat) {
            case "Engins & Matériel Lourd" -> {
                list.add(new FilterGroup("Type d'engin",
                        List.of("Pelleteuse", "Bulldozer", "Chargeuse", "Nacelle", "Compacteur")));
                list.add(new FilterGroup("Marque",
                        List.of("Caterpillar", "Komatsu", "Volvo", "JCB", "Bobcat")));
                list.add(new FilterGroup("Carburant",
                        List.of("Diesel", "Électrique", "Hybride")));
            }
            case "Matériaux de Construction" -> {
                list.add(new FilterGroup("Type",
                        List.of("Ciment", "Béton prêt", "Sable", "Gravier", "Acier")));
                list.add(new FilterGroup("Certification",
                        List.of("CE", "ISO", "Agréé projet public")));
            }
            case "Électricité & Câblage" -> {
                list.add(new FilterGroup("Tension",
                        List.of("220V", "380V")));
                list.add(new FilterGroup("Section",
                        List.of("1,5 mm²", "2,5 mm²", "16 mm²", "25 mm²")));
                list.add(new FilterGroup("Indice de protection",
                        List.of("IP44", "IP65", "IP67")));
            }
            default -> {
                list.add(new FilterGroup("Marque",
                        List.of("Bosch", "Makita", "Hilti", "Milwaukee")));
                list.add(new FilterGroup("Usage",
                        List.of("Chantier", "Atelier", "Maintenance")));
            }
        }

        return list;
    }

    // =========================
    //  FAKE PRODUCTS DATA
    // =========================
    private List<Product> getProductsForCategory(String cat) {
        List<Product> list = new ArrayList<>();

        switch (cat) {
            case "Engins & Matériel Lourd" -> {
                list.add(new Product("Pelleteuse Caterpillar 320D", 350_000, "Neuf"));
                list.add(new Product("Bulldozer Komatsu D65", 720_000, "Occasion"));
                list.add(new Product("Nacelle élévatrice Genie GS-3246", 98_000, "Neuf"));
            }
            case "Matériaux de Construction" -> {
                list.add(new Product("Ciment Lafarge 50kg", 89, "Palette"));
                list.add(new Product("Béton prêt à l'emploi", 570, "m³"));
                list.add(new Product("Gravier roulé 10/20", 260, "Tonne"));
            }
            case "Électricité & Câblage" -> {
                list.add(new Product("Câble cuivre 16mm²", 49, "Par mètre"));
                list.add(new Product("Projecteur LED 300W IP65", 790, "Extérieur"));
                list.add(new Product("Disjoncteur Schneider C32", 89, "Tableau"));
            }
            default -> {
                list.add(new Product("Marteau-piqueur Bosch GSH 27", 7_500, "Pro"));
                list.add(new Product("Perceuse Makita 710W", 490, "Atelier"));
                list.add(new Product("Casque de sécurité 3M", 49, "EPI"));
            }
        }

        return list;
    }

    // =========================
    //  Simple record types
    // =========================
    private record FilterGroup(String name, List<String> options) {}
    private record Product(String name, double price, String tag) {}
}
