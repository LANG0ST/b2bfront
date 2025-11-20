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

public class ProductsController {

    @FXML private TableView<Produit> productsTable;
    @FXML private TableColumn<Produit, Boolean> selectCol;
    @FXML private TableColumn<Produit, Long> idCol;
    @FXML private TableColumn<Produit, String> nameCol;
    @FXML private TableColumn<Produit, String> descriptionCol;
    @FXML private TableColumn<Produit, BigDecimal> priceCol;
    @FXML private TableColumn<Produit, Integer> stockCol;
    @FXML private TableColumn<Produit, String> categorieCol;
    @FXML private TableColumn<Produit, String> companyCol;
    @FXML private TableColumn<Produit, Void> actionsCol;

    // Filtres
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCategorie;
    @FXML private ComboBox<String> filterCompany;

    // Data
    private final ObservableList<Produit> products = FXCollections.observableArrayList();
    private FilteredList<Produit> filteredProducts;

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
            public TableCell<Produit, Void> call(TableColumn<Produit, Void> param) {
                return new TableCell<>() {

                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        deleteBtn.setStyle("-fx-background-color:#FF4747; -fx-text-fill:white;");

                        // DELETE action
                        deleteBtn.setOnAction(e -> {
                            Produit p = getTableView().getItems().get(getIndex());
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

        productsTable.setRowFactory(tv -> {
            TableRow<Produit> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Produit selectedProduct = row.getItem();
                    openProductDetails(selectedProduct);
                }
            });

            return row;
        });
    }

    @FXML
    private StackPane mainContent;

    private void openProductDetails(Produit prod) {
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

        Categorie cat1 = new Categorie(1, "Gravats");
        Categorie cat2 = new Categorie(2, "Câbles");
        Categorie cat3 = new Categorie(3, "Matériaux");

        Company c1 = new Company(1L, null, "Safia corp", "adr", "Marrakech",
                "066666", "website", "email", "pass", true);

        Company c2 = new Company(2L, null, "Taha industries", "adr2", "Casa",
                "077777", "website2", "mail2", "pass", true);

        products.addAll(
                new Produit(1L, "Câble 380V", "Cable haute tension", new BigDecimal("149.99"), 120, c1, cat2, null, null, null),
                new Produit(2L, "Sable 25kg", "Qualité premium", new BigDecimal("59.99"), 300, c1, cat3, null, null, null),
                new Produit(3L, "Gravier 50kg", "Pour chantiers lourds", new BigDecimal("79.99"), 80, c2, cat1, null, null, null)
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
