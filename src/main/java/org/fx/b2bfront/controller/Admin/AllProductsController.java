package org.fx.b2bfront.controller.Admin;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.api.StatsApi;
import org.fx.b2bfront.model.ProductStats;
import org.fx.b2bfront.utils.AppNavigator;

public class AllProductsController {

    @FXML private VBox rankingContainer;

    private final StatsApi statsApi = new StatsApi();

    @FXML
    public void initialize() {

        int rank = 1;

        for (ProductStats s : statsApi.bestProducts()) {
            HBox card = new HBox();
            if(rank == 1) {
                card.getStyleClass().add("ranking-card-1");
            } else if (rank == 2) {
                card.getStyleClass().add("ranking-card-2");
            } else if (rank == 3) {
                card.getStyleClass().add("ranking-card-3");
            }else{
                card.getStyleClass().add("ranking-card");
            }

            Label rankLabel;
            if(rank == 1) {
                rankLabel = new Label("\uD83E\uDD47");

            } else if (rank == 2) {
                rankLabel = new Label("\uD83E\uDD48");

            } else if (rank == 3) {
                rankLabel = new Label("\uD83E\uDD49");

            }else{
                rankLabel = new Label("#" + rank);

            }
            rankLabel.getStyleClass().add("rank-number");
            rankLabel.setAlignment(Pos.CENTER_LEFT);


            Label name = new Label(s.getProductName());
            name.getStyleClass().add("rank-name");
            name.setAlignment(Pos.CENTER);


            Label value = new Label(String.valueOf(s.getTotal()) + " DH ");
            value.getStyleClass().add("rank-value");
            value.setAlignment(Pos.CENTER_RIGHT);

            Pane spacer1 = new Pane();
            HBox.setHgrow(spacer1, Priority.ALWAYS);

            Pane spacer2 = new Pane();
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            card.getChildren().addAll(rankLabel,spacer1, name,spacer2,value);
            rankingContainer.getChildren().add(card);

            rank++;
        }
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
