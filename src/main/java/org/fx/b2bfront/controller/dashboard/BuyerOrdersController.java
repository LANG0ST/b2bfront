package org.fx.b2bfront.controller.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.DashboardApi;
import org.fx.b2bfront.dto.CommandeDto;
import org.fx.b2bfront.dto.LigneCommandeDto;
import org.fx.b2bfront.store.AuthStore;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BuyerOrdersController implements Initializable {

    @FXML private VBox ordersContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBuyerOrders();
    }

    private void loadBuyerOrders() {

        Long buyerId = AuthStore.companyId;
        if (buyerId == null) return;

        new Thread(() -> {
            try {
                List<CommandeDto> list = DashboardApi.getBuyerOrders(buyerId);

                Platform.runLater(() -> {
                    ordersContainer.getChildren().clear();

                    for (CommandeDto c : list) {
                        ordersContainer.getChildren().add(buildOrderCard(c));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private VBox buildOrderCard(CommandeDto c) {

        VBox card = new VBox(8);
        card.getStyleClass().add("order-card");
        card.setPadding(new Insets(12));

        // Top row: ref + date + statut
        HBox header = new HBox(20);
        Label ref = new Label("#" + c.getRefCommande());
        ref.getStyleClass().add("order-ref");

        Label date = new Label(c.getDateCommande());
        date.getStyleClass().add("order-date");

        Label statut = new Label(c.getStatut());
        statut.getStyleClass().add("status-" + c.getStatut().toLowerCase());

        header.getChildren().addAll(ref, date, statut);

        // Items
        VBox itemsBox = new VBox(4);
        double total = 0;

        for (LigneCommandeDto l : c.getLignes()) {

            String lineText = "- " + l.getProduitName()
                    + " | Qte: " + l.getQuantite()
                    + " | PU: " + l.getPrixUnitaire() + " DH";

            Label itemLabel = new Label(lineText);
            itemLabel.getStyleClass().add("order-line");

            itemsBox.getChildren().add(itemLabel);

            total += l.getPrixUnitaire() * l.getQuantite();
        }

        // Total
        Label totalLabel = new Label("TOTAL: " + total + " DH");
        totalLabel.getStyleClass().add("order-total");

        VBox container = new VBox(10, header, itemsBox, totalLabel);
        return container;
    }
}
