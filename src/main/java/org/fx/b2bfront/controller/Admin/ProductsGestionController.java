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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.CategoryApi;
import org.fx.b2bfront.api.CompanyApi;
import org.fx.b2bfront.api.ProductsApi;
import org.fx.b2bfront.dto.CategoryDto;
import org.fx.b2bfront.dto.CompanyDto;
import org.fx.b2bfront.dto.ProductDto;
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

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCategorie;
    @FXML private ComboBox<String> filterCompany;
    @FXML private Button clearFiltersBtn;

    @FXML private Button deleteAllBtn;

    private final ObservableList<ProductDto> products = FXCollections.observableArrayList();
    private FilteredList<ProductDto> filteredProducts;

    private final List<ProductDto> selectedProducts = new ArrayList<>();

    @FXML
    public void initialize() {

        setupColumns();
        setupSelectionColumn();
        setupActionColumn();

        deleteAllBtn.setOnAction(e -> deleteSelected());

        loadTestProducts();

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
                    openProductDetails(row.getItem());
                }
            });

            return row;
        });
    }

    private void setupColumns() {

        idCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getId()));
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        descriptionCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));

        priceCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(BigDecimal.valueOf(cell.getValue().getPrice()))
        );

        stockCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getStock())
        );

        categorieCol.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getCategoryId() != null
                                ? CategoryApi.findById(cell.getValue().getCategoryId()).getName()
                                : ""
                )
        );

        companyCol.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getCompanyId() != null
                                ? CompanyApi.getById(cell.getValue().getCompanyId()).getName()
                                : ""
                )
        );
    }

    private void setupSelectionColumn() {

        selectCol.setCellFactory(col -> new TableCell<>() {

            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, oldV, newV) -> {
                    ProductDto product = getCurrentProduct();
                    if (product == null) return;

                    if (newV)
                        selectedProducts.add(product);
                    else
                        selectedProducts.remove(product);
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                ProductDto product = getCurrentProduct();

                if (product != null)
                    checkBox.setSelected(selectedProducts.contains(product));

                setGraphic(checkBox);
            }

            private ProductDto getCurrentProduct() {
                return getTableRow() != null ? getTableRow().getItem() : null;
            }
        });
    }

    private void setupActionColumn() {

        actionsCol.setCellFactory(col -> new TableCell<>() {

            private final Button deleteBtn = new Button("Supprimer");

            {
                deleteBtn.getStyleClass().add("action-btn-red");

                deleteBtn.setOnAction(e -> {
                    ProductDto p = getTableView().getItems().get(getIndex());
                    ProductsApi.delete(p.getId());
                    refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, deleteBtn));
            }
        });
    }


    private void refresh() {
        selectedProducts.clear();
        products.setAll(ProductsApi.findAll());
        applyFilters();
    }


    private void deleteSelected() {

        if (selectedProducts.isEmpty()) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer tous les produits sélectionnés ?",
                ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

            selectedProducts.forEach(p -> ProductsApi.delete(p.getId()));

            showInfo("Produits supprimés avec succès.");
            refresh();
        }
    }

    private void showInfo(String msg) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(null);
        dialog.setHeaderText(null);

        // Désactiver les boutons par défaut
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setText("OK");
        okBtn.getStyleClass().add("alert-ok-btn");

        // Contenu custom
        Label message = new Label(msg);
        message.getStyleClass().add("alert-message");

        VBox box = new VBox(message);
        box.setSpacing(20);
        box.setStyle("-fx-padding: 18;");
        box.getStyleClass().add("alert-container");

        dialog.getDialogPane().setContent(box);

        // Import du style CSS global
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/style/dashboard.css").toExternalForm()
        );

        dialog.showAndWait();
    }



    private void applyFilters() {

        filteredProducts.setPredicate(p -> {

            // search
            String search = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
            if (!search.isEmpty()
                    && !p.getName().toLowerCase().contains(search)
                    && !p.getDescription().toLowerCase().contains(search))
                return false;

            // category
            if (filterCategorie.getValue() != null
                    && !"Toutes".equals(filterCategorie.getValue())) {

                String catName = CategoryApi.findById(p.getCategoryId()).getName();
                if (!catName.equalsIgnoreCase(filterCategorie.getValue()))
                    return false;
            }

            // company
            if (filterCompany.getValue() != null
                    && !"Toutes".equals(filterCompany.getValue())) {

                String compName = CompanyApi.getById(p.getCompanyId()).getName();
                if (!compName.equalsIgnoreCase(filterCompany.getValue()))
                    return false;
            }

            return true;
        });
    }

    private void resetFilters() {
        searchField.clear();
        filterCategorie.getSelectionModel().select("Toutes");
        filterCompany.getSelectionModel().select("Toutes");
        applyFilters();
    }


    private void loadTestProducts() {

        List<CategoryDto> categories = CategoryApi.getAll();
        List<String> categoriesNames = categories.stream().map(CategoryDto::getName).toList();

        List<CompanyDto> companies = CompanyApi.findAll();
        List<String> companiesNames = companies.stream().map(CompanyDto::getName).toList();

        products.addAll(ProductsApi.findAll());

        filterCategorie.setItems(
                FXCollections.observableArrayList(
                        Stream.concat(Stream.of("Toutes"), categoriesNames.stream()).toList()
                )
        );
        filterCategorie.setValue("Toutes");

        filterCompany.setItems(
                FXCollections.observableArrayList(
                        Stream.concat(Stream.of("Toutes"), companiesNames.stream()).toList()
                )
        );
        filterCompany.setValue("Toutes");
    }


    @FXML
    private StackPane mainContent;

    private void openProductDetails(ProductDto prod) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/ProductDetails.fxml"));
            Parent detailsRoot = loader.load();

            ProductDetailsController controller = loader.getController();
            controller.setProd(prod);

            mainContent.getChildren().setAll(detailsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openDashboard(MouseEvent event) { AppNavigator.navigateTo("Admin/AdminDashboard.fxml"); }

    @FXML
    public void openUsersModule(MouseEvent event) { AppNavigator.navigateTo("Admin/GestionUsers.fxml"); }

    @FXML
    public void openProductsModule(MouseEvent event) { AppNavigator.navigateTo("Admin/GestionProducts.fxml"); }

    @FXML
    private void openAuthpage(MouseEvent event) { AppNavigator.navigateTo("auth.fxml"); }

}
