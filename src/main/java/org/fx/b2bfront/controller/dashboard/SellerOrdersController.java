package org.fx.b2bfront.controller.dashboard;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.fx.b2bfront.api.DashboardApi;
import org.fx.b2bfront.dto.CommandeDto;
import org.fx.b2bfront.store.AuthStore;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SellerOrdersController implements Initializable {

    @FXML private TableView<CommandeDto> ordersTable;
    @FXML private TableColumn<CommandeDto, String> colRef;
    @FXML private TableColumn<CommandeDto, String> colBuyer;
    @FXML private TableColumn<CommandeDto, String> colDate;
    @FXML private TableColumn<CommandeDto, String> colStatut;
    @FXML private TableColumn<CommandeDto, Void> colActions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadOrders();
    }

    private void setupTable() {

        // -------- REF --------
        colRef.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getRefCommande())
        );

        // -------- BUYER NAME (via company) --------
        colBuyer.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getCompany() != null ?
                                c.getValue().getCompany().getName() :
                                "—"
                )
        );

        // -------- DATE --------
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateCommande())
        );

        // -------- STATUS --------
        colStatut.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatut())
        );

        // -------- ACTION BUTTONS --------
        colActions.setCellFactory(col -> new TableCell<>() {

            private final Button acceptBtn = new Button("Accepter");
            private final Button rejectBtn = new Button("Refuser");

            private final HBox box = new HBox(10, acceptBtn, rejectBtn);

            {
                box.setAlignment(Pos.CENTER);

                acceptBtn.getStyleClass().add("action-btn-green");
                rejectBtn.getStyleClass().add("action-btn-red");

                acceptBtn.setOnAction(e -> updateStatus("VALIDEE"));
                rejectBtn.setOnAction(e -> updateStatus("REFUSEE"));
            }

            private void updateStatus(String decision) {
                CommandeDto row = getTableView().getItems().get(getIndex());
                Long orderId = row.getId();

                new Thread(() -> {
                    try {
                        DashboardApi.updateOrderStatus(orderId, decision);
                        loadOrders();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void loadOrders() {

        Long sellerId = AuthStore.companyId;
        if (sellerId == null) return;

        new Thread(() -> {
            try {
                List<CommandeDto> data = DashboardApi.getSellerOrders(sellerId);

                Platform.runLater(() ->
                        ordersTable.getItems().setAll(data)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
