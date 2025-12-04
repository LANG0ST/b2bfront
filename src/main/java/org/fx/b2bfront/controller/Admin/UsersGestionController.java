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


    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> cityFilter;
    @FXML private Button clearFiltersBtn;

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

    @FXML private Button bloqueAllBtn;
    @FXML private Button debloqueAllBtn;
    @FXML private Button deleteAllBtn;

    private final ObservableList<CompanyDto> users = FXCollections.observableArrayList();
    private FilteredList<CompanyDto> filteredUsers;

    private final ObservableList<CompanyDto> selectedUsers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {


        idCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getId()));
        nameCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getName()));
        phoneCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPhone()));
        emailCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getEmail()));
        cityCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCity()));
        enabledCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().isEnabled()));
        createdAtCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCreatedAt()));


        selectCol.setCellFactory(col -> new TableCell<>() {

            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, oldV, newV) -> {
                    CompanyDto user = getTableRow().getItem();
                    if (user == null) return;

                    if (newV) {
                        if (!selectedUsers.contains(user))
                            selectedUsers.add(user);
                    } else {
                        selectedUsers.remove(user);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    CompanyDto user = getTableRow().getItem();
                    checkBox.setSelected(selectedUsers.contains(user));
                    setGraphic(checkBox);
                }
            }
        });

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
                    CompanyApi.disable(u.getId());
                    refreshTable();
                });

                unblockBtn.setOnAction(e -> {
                    CompanyDto u = getTableView().getItems().get(getIndex());
                    CompanyApi.enable(u.getId());
                    refreshTable();
                });

                deleteBtn.setOnAction(e -> {
                    CompanyDto u = getTableView().getItems().get(getIndex());
                    CompanyApi.delete(u.getId());
                    refreshTable();
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

        users.addAll(CompanyApi.findAll());

        filteredUsers = new FilteredList<>(users, u -> true);
        usersTable.setItems(filteredUsers);


        statusFilter.getItems().addAll("Tous", "Activés", "Désactivés");
        statusFilter.getSelectionModel().select("Tous");

        cityFilter.getItems().add("Toutes");
        cityFilter.getItems().addAll(
                users.stream().map(CompanyDto::getCity).distinct().collect(Collectors.toList())
        );
        cityFilter.getSelectionModel().select("Toutes");

        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        statusFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        cityFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        clearFiltersBtn.setOnAction(e -> resetFilters());

        bloqueAllBtn.setOnAction(e -> blockSelected());
        debloqueAllBtn.setOnAction(e -> unblockSelected());
        deleteAllBtn.setOnAction(e -> deleteSelected());

        usersTable.setRowFactory(tv -> {
            TableRow<CompanyDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openUserDetails(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML private StackPane mainContent;

    private void openUserDetails(CompanyDto user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/UserDetails.fxml"));
            Parent detailsRoot = loader.load();

            UserDetailsController controller = loader.getController();
            controller.setCompany(user);

            mainContent.getChildren().setAll(detailsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFilters() {

        String search = searchField.getText().toLowerCase();
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

    private void blockSelected() {
        if (selectedUsers.isEmpty()) return;

        for (CompanyDto u : selectedUsers)
            CompanyApi.disable(u.getId());

        showInfo("Les comptes sélectionnés ont été bloqués.");
        refreshTable();
    }

    private void unblockSelected() {
        if (selectedUsers.isEmpty()) return;

        for (CompanyDto u : selectedUsers)
            CompanyApi.enable(u.getId());

        showInfo("Les comptes sélectionnés ont été débloqués.");
        refreshTable();
    }

    private void deleteSelected() {
        if (selectedUsers.isEmpty()) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer tous les comptes sélectionnés ?",
                ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES)
            return;

        for (CompanyDto u : selectedUsers)
            CompanyApi.delete(u.getId());

        showInfo("Les comptes sélectionnés ont été supprimés.");
        refreshTable();
    }

    private void refreshTable() {
        selectedUsers.clear();
        users.clear();
        users.addAll(CompanyApi.findAll());
        applyFilters();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

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
