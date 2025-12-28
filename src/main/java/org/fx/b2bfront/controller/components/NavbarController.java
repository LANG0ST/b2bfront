package org.fx.b2bfront.controller.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.fx.b2bfront.service.NotificationPoller;
import org.fx.b2bfront.store.AppStore;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.utils.AppNavigator;

import java.util.Map;

public class NavbarController {

    @FXML private TextField searchBar;
    @FXML private Button btnHome;
    @FXML private Button btnNotifications;
    @FXML private Button btnCart;
    @FXML private Button btnDashboard;

    @FXML private ImageView notifIcon;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {

        btnHome.setOnAction(e -> AppNavigator.navigateTo("homepage.fxml"));
        btnDashboard.setOnAction(e -> AppNavigator.navigateTo("dashboard/Dashboard.fxml"));
        btnCart.setOnAction(e -> AppNavigator.navigateTo("cart.fxml"));
        btnLogout.setOnAction(e -> logout());

        btnNotifications.setOnAction(e -> {
            AppNavigator.navigateToWithParams(
                    "dashboard/Dashboard.fxml",
                    Map.of("section", "notif")
            );
        });


        AppStore.hasNotificationsProperty().addListener((obs, oldVal, newVal) -> {
            updateNotificationIcon();
        });
        updateNotificationIcon();

        searchBar.setOnAction(e -> {
            String q = searchBar.getText().trim();
            if (!q.isEmpty()) {
                AppStore.setSearchQuery(q);
                AppNavigator.navigateTo("search/SearchResults.fxml");
            }
        });

    }

    private void logout() {

        AuthStore.jwt = null;
        AuthStore.role = null;
        AuthStore.companyId = null;
        AuthStore.email = null;

        NotificationPoller.stop();
        AppStore.clear();
        CartStore.clear();

        AppNavigator.navigateTo("Auth.fxml");
    }


    private void updateNotificationIcon() {

        String iconPath;

        if (AppStore.hasNotifications()) {
            iconPath = "/images/notification.png";
        } else {
            iconPath = "/images/nonotification.png";
        }

        var res = getClass().getResource(iconPath);
        if (res == null) {
            System.out.println("Cannot load icon: " + iconPath);
            return;
        }

        notifIcon.setImage(new Image(res.toExternalForm()));
    }

    public void refreshNotificationIcon() {
        updateNotificationIcon();
    }


}
