package org.fx.b2bfront.controller.Admin;

import javafx.beans.property.SimpleObjectProperty;
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
import org.fx.b2bfront.api.CompanyApi;
import org.fx.b2bfront.dto.CompanyDto;
import org.fx.b2bfront.utils.AppNavigator;

import java.util.stream.Collectors;

public class UsersGestionController {

    // ======= FILTRES =======
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> cityFilter;
    @FXML private Button clearFiltersBtn;

    // ======= TABLE =======
    @FXML private TableView<CompanyDto> usersTable;
    @FXML private TableColumn<CompanyDto, Boolean> selectCol;
    @FXML private TableColumn<CompanyDto, Long> idCol;
    @FXML private TableColumn<CompanyDto, String> nameCol;
    @FXML private TableColumn<CompanyDto, String> phoneCol;
    @FXML private TableColumn<CompanyDto, String> emailCol;
    @FXML private TableColumn<CompanyDto, String> createdAtCol;
    @FXML private TableColumn<CompanyDto, String> cityCol;
    @FXML private TableColumn<CompanyDto, Boolean> enabledCol;
    @FXML private TableColumn<CompanyDto, Void> actionsCol;

    // ======= DATA SOURCE =======
    private ObservableList<CompanyDto> users = FXCollections.observableArrayList();
    private FilteredList<CompanyDto> filteredUsers;

    private  final CompanyApi companyApi = new CompanyApi();




    @FXML
    public void initialize() {

        // ===============================
        //   TABLE COLUMN BINDINGS
        // ===============================

        idCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getId()));

        nameCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getName()));

        phoneCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getPhone()));

        emailCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getEmail()));

        cityCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getCity()));

        enabledCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().isEnabled()));
        createdAtCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getCreatedAt()));





        // ===============================
        //   CHECKBOX SELECTION COLUMN
        // ===============================
        selectCol.setCellFactory(col -> new TableCell<>() {
            private final CheckBox cb = new CheckBox();
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cb);
            }
        });

        // ===============================
        //   ACTION BUTTONS PER ROW
        // ===============================
        actionsCol.setCellFactory(col -> new TableCell<>() {

            private final Button blockBtn = new Button("Bloquer");
            private final Button unblockBtn = new Button("Débloquer");
            private final Button deleteBtn = new Button("Supprimer");

            {
                blockBtn.getStyleClass().add("action-btn");
                unblockBtn.getStyleClass().add("action-btn-green");
                deleteBtn.getStyleClass().add("action-btn-red");

                blockBtn.setOnAction(e -> {
                    CompanyDto u = getTableView().getItems().get(getIndex());
                    u.setEnabled(false);
                    usersTable.refresh();
                });

                unblockBtn.setOnAction(e -> {
                    CompanyDto u = getTableView().getItems().get(getIndex());
                    u.setEnabled(true);
                    usersTable.refresh();
                });

                deleteBtn.setOnAction(e -> {
                    CompanyDto u = getTableView().getItems().get(getIndex());
                    users.remove(u);
                    applyFilters();
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, blockBtn, unblockBtn, deleteBtn);
                    box.getStyleClass().add("action-buttons-box");
                    setGraphic(box);
                }
            }
        });

        // ===============================
        //   TEST DATA
        // ===============================
        users.addAll(companyApi.findAll());

        // ===============================
        //   FILTER LOGIC
        // ===============================


        filteredUsers = new FilteredList<>(users, u -> true);
        usersTable.setItems(filteredUsers);



        // Status filter
        statusFilter.getItems().addAll("Tous", "Activés", "Désactivés");
        statusFilter.getSelectionModel().select("Tous");

        // City filter auto
        cityFilter.getItems().add("Toutes");
        cityFilter.getItems().addAll(
                users.stream().map(CompanyDto::getCity).distinct().collect(Collectors.toList())
        );
        cityFilter.getSelectionModel().select("Toutes");

        // Listeners
        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        statusFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        cityFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        clearFiltersBtn.setOnAction(e -> resetFilters());

        usersTable.setRowFactory(tv -> {
            TableRow<CompanyDto> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    CompanyDto selectedUser = row.getItem();
                    openUserDetails(selectedUser);
                }
            });

            return row;
        });

    }

    @FXML
    private StackPane mainContent;

    private void openUserDetails(CompanyDto user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/UserDetails.fxml"));
            Parent detailsRoot = loader.load();

            // Récupérer le controller de la nouvelle page
            UserDetailsController controller = loader.getController();
            controller.setCompany(user);

            // Remplacer le contenu du center
            mainContent.getChildren().setAll(detailsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    // ======================================================
    //    FILTER METHODS
    // ======================================================

    private void applyFilters() {

        String search = searchField.getText().trim().toLowerCase();
        String status = statusFilter.getValue();
        String city = cityFilter.getValue();

        filteredUsers.setPredicate(user -> {

            boolean matchSearch =
                    user.getName().toLowerCase().contains(search)
                            || user.getEmail().toLowerCase().contains(search)
                            || user.getPhone().toLowerCase().contains(search)
                            || user.getCity().toLowerCase().contains(search);

            boolean matchStatus =
                    status.equals("Tous")
                            || (status.equals("Activés") && user.isEnabled())
                            || (status.equals("Désactivés") && !user.isEnabled());

            boolean matchCity =
                    city.equals("Toutes") || user.getCity().equalsIgnoreCase(city);

            return matchSearch && matchStatus && matchCity;
        });
    }

    private void resetFilters() {
        searchField.clear();
        statusFilter.getSelectionModel().select("Tous");
        cityFilter.getSelectionModel().select("Toutes");
        applyFilters();
    }


    // ======================================================
    //    NAVIGATION
    // ======================================================

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
