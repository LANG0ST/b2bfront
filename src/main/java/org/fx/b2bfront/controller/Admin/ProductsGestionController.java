package org.fx.b2bfront.controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.fx.b2bfront.model.*;
import org.fx.b2bfront.utils.AppNavigator;
import java.math.BigDecimal;

public class ProductsGestionController {

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Boolean> selectCol;
    @FXML private TableColumn<Product, Long> idCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, String> descriptionCol;
    @FXML private TableColumn<Product, BigDecimal> priceCol;
    @FXML private TableColumn<Product, Integer> stockCol;
    @FXML private TableColumn<Product, String> categorieCol;
    @FXML private TableColumn<Product, String> companyCol;
    @FXML private TableColumn<Product, Void> actionsCol;

    // Filtres
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCategorie;
    @FXML private ComboBox<String> filterCompany;
    @FXML private Button clearFiltersBtn;

    // Data
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private FilteredList<Product> filteredProducts;

    @FXML
    public void initialize() {

        // MAPPINGS
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        categorieCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getCategorie() != null ? cell.getValue().getCategorie().getName() : ""
                ));

        companyCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getCompany() != null ? cell.getValue().getCompany().getName() : ""
                ));

        // CHECKBOX
        selectCol.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(checkBox);
            }
        });

        // ACTIONS (Modifier / Supprimer)
        actionsCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {

                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        deleteBtn.getStyleClass().add("action-btn-red");

                        // DELETE action
                        deleteBtn.setOnAction(e -> {
                            Product p = getTableView().getItems().get(getIndex());
                            products.remove(p);   // IMPORTANT : supprimer dans la vraie liste
                            applyFilters();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) setGraphic(null);
                        else {
                            HBox box = new HBox(10, deleteBtn);
                            box.getStyleClass().add("action-buttons-box");
                            setGraphic(box);
                        }
                    }
                };
            }
        });

        // TEST DATA
        loadTestProducts();

        // Filtres dynamiques
        filteredProducts = new FilteredList<>(products, p -> true);
        productsTable.setItems(filteredProducts);

        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        filterCategorie.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        filterCompany.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        clearFiltersBtn.setOnAction(e -> resetFilters());


        productsTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Product selectedProduct = row.getItem();
                    openProductDetails(selectedProduct);
                }
            });

            return row;
        });
    }

    private void resetFilters() {
        searchField.clear();
        filterCategorie.getSelectionModel().select("Toutes");
        filterCompany.getSelectionModel().select("Toutes");
        applyFilters();
    }

    @FXML
    private StackPane mainContent;

    private void openProductDetails(Product prod) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/ProductDetails.fxml"));
            Parent detailsRoot = loader.load();

            // Récupérer le controller de la nouvelle page
            ProductDetailsController controller = loader.getController();
            controller.setProd(prod);

            // Remplacer le contenu du center
            mainContent.getChildren().setAll(detailsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FILTRAGE LOGIQUE
    private void applyFilters() {
        filteredProducts.setPredicate(p -> {

            // Search text
            String search = searchField.getText() != null ? searchField.getText().toLowerCase() : "";
            if (!search.isEmpty() &&
                    !(p.getName().toLowerCase().contains(search)
                            || p.getDescription().toLowerCase().contains(search)))
                return false;

            // Category filter
            if (filterCategorie.getValue() != null &&
                    !filterCategorie.getValue().equals("Toutes")) {

                if (p.getCategorie() == null ||
                        !p.getCategorie().getName().equalsIgnoreCase(filterCategorie.getValue()))
                    return false;
            }

            // Company filter
            if (filterCompany.getValue() != null &&
                    !filterCompany.getValue().equals("Toutes")) {

                if (p.getCompany() == null ||
                        !p.getCompany().getName().equalsIgnoreCase(filterCompany.getValue()))
                    return false;
            }

            return true;
        });
    }

    // TEST DATA
    private void loadTestProducts() {

        Category cat1 = new Category(1, "Gravats","",null);
        Category cat2 = new Category(2, "Câbles","",null);
        Category cat3 = new Category(3, "Matériaux","",null);

        Company c1 = new Company(1L, null, "Safia corp", "adr", "Marrakech",
                "066666", "website", "email", "pass", true);

        Company c2 = new Company(2L, null, "Taha industries", "adr2", "Casa",
                "077777", "website2", "mail2", "pass", true);

        products.addAll(
                new Product(1L, "Câble 380V", "Cable haute tension", new BigDecimal("149.99"), 120, c1, cat2, null, null, null),
                new Product(2L, "Sable 25kg", "Qualité premium", new BigDecimal("59.99"), 300, c1, cat3, null, null, null),
                new Product(3L, "Gravier 50kg", "Pour chantiers lourds", new BigDecimal("79.99"), 80, c2, cat1, null, null, null),
                new Product(1L,  "Câble 380V","Câble haute tension industriel",new BigDecimal("149.99"), 120, c1, cat2, null, null, null),
                new Product(2L,  "Sable 25kg","Sable lavé qualité premium",new BigDecimal("59.99"), 300, c1, cat3, null, null, null),
                new Product(3L,  "Gravier 50kg",            "Granulométrie certifiée pour chantiers",     new BigDecimal("79.99"), 80,  c2, cat1, null, null, null),

                new Product(4L,  "Plaque acier 8mm","Acier renforcé norme EN10025",new BigDecimal("299.90"), 45,  c2, cat1, null, null, null),
                new Product(5L,  "Tube PVC 50mm","Tube résistant haute pression",new BigDecimal("24.50"),  400, c2, cat3, null, null, null),
                new Product(6L,  "Béton prêt 30kg","Béton de construction CAT Industrial",new BigDecimal("44.95"),  200, c1, cat3, null, null, null),
                new Product(7L,  "Tôle galvanisée","Anti-corrosion, usage extérieur",new BigDecimal("129.99"), 67,  c1, cat1, null, null, null),
                new Product(8L,  "Fer à béton 12mm", "Barre acier nervuré",new BigDecimal("13.99"),  800, c1, cat1, null, null, null),
                new Product(9L,  "Peinture industrielle","Résistance extrême UV et chaleur",new BigDecimal("89.99"),  56,  c2, cat1, null, null, null),
                new Product(10L, "Huile hydraulique 20L",  "Spéciale engins de chantier",new BigDecimal("249.99"), 30,  c1, cat2, null, null, null),
                new Product(11L, "Câble souterrain","Isolation triple couche",                     new BigDecimal("189.99"), 140, c2, cat2, null, null, null),
                new Product(12L, "Mortier 25kg","Haute adhérence",                             new BigDecimal("39.99"),  250, c1, cat3, null, null, null),
                new Product(14L, "Filtre moteur CAT","Compatible machines industrielles",           new BigDecimal("99.99"),  60,  c2, cat3, null, null, null),
                new Product(15L, "Galet convoyeur","Pour ligne de production",                    new BigDecimal("79.90"),  100, c1, cat2, null, null, null),
                new Product(16L, "Béton fibré 30kg",        "Renforcé fibres métalliques",                 new BigDecimal("54.99"),  180, c1, cat3, null, null, null),
                new Product(17L, "Peinture époxy",          "Usage industriel haute résistance",           new BigDecimal("129.90"), 75,  c2, cat1, null, null, null),
                new Product(19L, "Tube acier 30mm",         "Tube rond haute résistance",                  new BigDecimal("59.99"),  210, c2, cat1, null, null, null),
                new Product(20L, "Ciment 50kg",             "Qualité premium S2",                          new BigDecimal("99.99"),  150, c1, cat3, null, null, null),
                new Product(21L, "Gravier noir 25kg",       "Drainage et fondations",                      new BigDecimal("64.99"),  90,  c1, cat3, null, null, null),
                new Product(23L, "Roue industrielle",       "Roulement renforcé 300kg",                    new BigDecimal("119.90"), 70,  c1, cat3, null, null, null),
                new Product(24L, "Câble blindé",            "Protection EMI/EMF",                          new BigDecimal("199.99"), 50,  c2, cat2, null, null, null),
                new Product(26L, "Compresseur portable",    "Pression 12 bars",                            new BigDecimal("899.99"), 12,  c2, cat1, null, null, null),
                new Product(27L, "Clé dynamométrique",      "Couple réglable 70-350 Nm",                    new BigDecimal("349.99"), 28,  c1, cat2, null, null, null),
                new Product(28L, "Bobine cuivre 50m",       "Pour installation électrique",                new BigDecimal("599.90"), 25,  c2, cat2, null, null, null),
                new Product(30L, "Béton autonivelant",      "Usage sol industriel",                        new BigDecimal("129.99"), 90,  c1, cat3, null, null, null),
                new Product(36L, "Câble 220V renforcé",     "Isolation épaisse",                           new BigDecimal("89.99"),  140, c2, cat2, null, null, null),
                new Product(41L, "Sable rouge 30kg",        "Finition décorative",                         new BigDecimal("69.99"),  120, c1, cat3, null, null, null),
                new Product(42L, "Câble inox 8mm",          "Résistance traction 4 tonnes",                new BigDecimal("159.99"), 50,  c2, cat2, null, null, null),
                new Product(43L, "Fer plat 20mm",           "Acier laminé à chaud",                        new BigDecimal("74.99"),  180, c1, cat1, null, null, null)
        );

        // Charger les filtres
        filterCategorie.setItems(FXCollections.observableArrayList("Toutes", "Gravats", "Câbles", "Matériaux"));
        filterCategorie.setValue("Toutes");

        filterCompany.setItems(FXCollections.observableArrayList("Toutes", "Safia corp", "Taha industries"));
        filterCompany.setValue("Toutes");
    }

    @FXML
    public void openDashboard(MouseEvent event) {
        AppNavigator.navigateTo("Admin/AdminDashboard.fxml");
    }

    @FXML
    public void openUsersModule(MouseEvent event) {
        AppNavigator.navigateTo("Admin/GestionUsers.fxml");
    }

    @FXML
    public void openProductsModule(MouseEvent event) {AppNavigator.navigateTo("Admin/GestionProducts.fxml");}

    @FXML
    private void openAuthpage(MouseEvent event) {
        AppNavigator.navigateTo("auth.fxml");
    }

}
