package org.fx.b2bfront.controller.Admin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import org.fx.b2bfront.api.CategoryApi;
import org.fx.b2bfront.api.CompanyApi;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.CategoryDto;
import org.fx.b2bfront.dto.CompanyDto;
import org.fx.b2bfront.dto.ProductDto;
import org.fx.b2bfront.model.*;
import org.fx.b2bfront.utils.AppNavigator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProductsGestionController {

    @FXML private TableView<ProductDto> productsTable;
    @FXML private TableColumn<ProductDto, Boolean> selectCol;
    @FXML private TableColumn<ProductDto, Long> idCol;
    @FXML private TableColumn<ProductDto, String> nameCol;
    @FXML private TableColumn<ProductDto, String> descriptionCol;
    @FXML private TableColumn<ProductDto, BigDecimal> priceCol;
    @FXML private TableColumn<ProductDto, Integer> stockCol;
    @FXML private TableColumn<ProductDto, String> categorieCol;
    @FXML private TableColumn<ProductDto, String> companyCol;
    @FXML private TableColumn<ProductDto, Void> actionsCol;

    // Filtres
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCategorie;
    @FXML private ComboBox<String> filterCompany;
    @FXML private Button clearFiltersBtn;

    // Data
    private final ObservableList<ProductDto> products = FXCollections.observableArrayList();
    private FilteredList<ProductDto> filteredProducts;
    private  final CategoryApi categoryApi = new CategoryApi();
    private  final CompanyApi companyApi = new CompanyApi();
    private  final ProductsApi productsApi = new ProductsApi();

    @FXML
    public void initialize() {
        // MAPPINGS
        idCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getId()));

        nameCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getName()));

        descriptionCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDescription()));

        priceCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(BigDecimal.valueOf(cell.getValue().getPrice())));

        stockCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getStock()));


        categorieCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getCategoryId() != null ? categoryApi.findById(cell.getValue().getCategoryId()).getName() : ""
                ));

        companyCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getCompanyId() != null ? companyApi.getById(cell.getValue().getCompanyId()).getName() : ""
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
            public TableCell<ProductDto, Void> call(TableColumn<ProductDto, Void> param) {
                return new TableCell<>() {

                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        deleteBtn.getStyleClass().add("action-btn-red");

                        // DELETE action
                        deleteBtn.setOnAction(e -> {
                            ProductDto p = getTableView().getItems().get(getIndex());
                            productsApi.delete(p.getId());
                            products.clear();
                            products.addAll(productsApi.findAll());
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
            TableRow<ProductDto> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ProductDto selectedProduct = row.getItem();
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

    private void openProductDetails(ProductDto prod) {
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

                if (p.getCategoryId() == null ||
                        ! categoryApi.findById(p.getCategoryId()).getName().equalsIgnoreCase(filterCategorie.getValue()))
                    return false;
            }

            // Company filter
            if (filterCompany.getValue() != null &&
                    !filterCompany.getValue().equals("Toutes")) {

                if (p.getCompanyId() == null ||
                        !companyApi.getById(p.getCompanyId()).getName().equalsIgnoreCase(filterCompany.getValue()))
                    return false;
            }

            return true;
        });
    }

    // TEST DATA
    private void loadTestProducts() {

        List<CategoryDto> categories = categoryApi.getAll();

        List<String> categoriesNames = new ArrayList<>();
        for(CategoryDto category : categories ){
            categoriesNames.add(category.getName());
        }

        List<CompanyDto> companies = companyApi.findAll();

        List<String> compagniesNames = new ArrayList<>();
        for(CompanyDto company : companies ){
            compagniesNames.add(company.getName());
        }

        products.addAll(productsApi.findAll());


        // Charger les filtres
        filterCategorie.setItems(
                FXCollections.observableArrayList(
                        Stream.concat(Stream.of("Toutes"), categoriesNames.stream()).toList()
                )
        );
        filterCategorie.setValue("Toutes");

        filterCompany.setItems(
                FXCollections.observableArrayList(
                        Stream.concat(Stream.of("Toutes"), compagniesNames.stream()).toList()
                )
        );
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
