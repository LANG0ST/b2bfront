package org.fx.b2bfront.controller.Admin;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.fx.b2bfront.model.Company;
import org.fx.b2bfront.utils.AppNavigator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class UsersController {

    // ======= FILTRES =======
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> cityFilter;
    @FXML private Button clearFiltersBtn;

    // ======= TABLE =======
    @FXML private TableView<Company> usersTable;
    @FXML private TableColumn<Company, Boolean> selectCol;
    @FXML private TableColumn<Company, Long> idCol;
    @FXML private TableColumn<Company, String> nameCol;
    @FXML private TableColumn<Company, String> phoneCol;
    @FXML private TableColumn<Company, String> emailCol;
    @FXML private TableColumn<Company, String> cityCol;
    @FXML private TableColumn<Company, String> createdAtCol;
    @FXML private TableColumn<Company, Boolean> enabledCol;
    @FXML private TableColumn<Company, Void> actionsCol;

    // ======= DATA SOURCE =======
    private ObservableList<Company> users = FXCollections.observableArrayList();
    private FilteredList<Company> filteredUsers;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {

        // ===============================
        //   TABLE COLUMN BINDINGS
        // ===============================

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        enabledCol.setCellValueFactory(new PropertyValueFactory<>("enabled"));

        createdAtCol.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(
                        cell.getValue().getCreatedAt() != null ?
                                cell.getValue().getCreatedAt().format(fmt) : ""
                )
        );

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
                    Company u = getTableView().getItems().get(getIndex());
                    u.setEnabled(false);
                    usersTable.refresh();
                });

                unblockBtn.setOnAction(e -> {
                    Company u = getTableView().getItems().get(getIndex());
                    u.setEnabled(true);
                    usersTable.refresh();
                });

                deleteBtn.setOnAction(e -> {
                    Company u = getTableView().getItems().get(getIndex());
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
        users.addAll(
                new Company(1L, LocalDateTime.now(), "Safia", "Hay Riad", "Marrakech", "06000001", "Safia.ma", "sa@ma.com", "pass", true),
                new Company(2L, LocalDateTime.now(), "Meriem", "Hay Sidi Youssef", "Marrakech", "06000002", "Meriem.ma", "me@ma.com", "pass", true),
                new Company(3L, LocalDateTime.now(), "Ziad", "Hay Charaf", "Marrakech", "06000003", "Ziad.ma", "z@ma.com", "pass", true),
                new Company(4L, LocalDateTime.now(), "Alaa", "Hay Sinko", "Settat", "06000004", "Alaa.ma", "a@ma.com", "pass", false),
                new Company(5L, LocalDateTime.now(), "Soufiane", "Al Massira 3", "Marrakech", "06000005", "Soufiane.ma", "so@ma.com", "pass", true),
                new Company(6L, LocalDateTime.now(), "Taha", "Hay Riad", "Marrakech", "06000006", "Taha.ma", "t@ma.com", "pass", true),
                new Company(7L, LocalDateTime.now(), "Rokaya", "Hay Riad", "Dakar", "06000007", "Rokaya.ma", "r@ma.com", "pass", true),
                new Company(8L, LocalDateTime.now(), "Maroua", "Al Massira", "Marrakech", "06000008", "Maroua.ma", "mar@ma.com", "pass", true),
                new Company(9L, LocalDateTime.now(), "Malak", "Hay Riad", "Marrakech", "06000009", "Malak.ma", "mal@ma.com", "pass", true)
        );

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
                users.stream().map(Company::getCity).distinct().collect(Collectors.toList())
        );
        cityFilter.getSelectionModel().select("Toutes");

        // Listeners
        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        statusFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        cityFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        clearFiltersBtn.setOnAction(e -> resetFilters());

        usersTable.setRowFactory(tv -> {
            TableRow<Company> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Company selectedUser = row.getItem();
                    openUserDetails(selectedUser);
                }
            });

            return row;
        });

    }

    @FXML
    private StackPane mainContent;

    private void openUserDetails(Company user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/UserDetails.fxml"));
            Parent detailsRoot = loader.load();

            // Récupérer le controller de la nouvelle page
            UserDetailsController controller = loader.getController();
            controller.setUser(user);

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
