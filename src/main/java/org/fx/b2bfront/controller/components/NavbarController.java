package org.fx.b2bfront.controller.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.fx.b2bfront.utils.AppNavigator;

public class NavbarController {

    @FXML private TextField searchBar;
    @FXML private Button btnHome;
    @FXML private Button btnCategories;
    @FXML private Button btnNotifications;
    @FXML private Button btnCart;
    @FXML private Button btnDashboard;
    @FXML private Button btnAccount;

    @FXML
    public void initialize() {

        // HOME
        btnHome.setOnAction(e ->
                AppNavigator.navigateTo("homepage.fxml")
        );


        // NOTIFICATIONS (simple click)
        btnNotifications.setOnAction(e ->
                System.out.println("Notifications clicked!")
        );

        // CART / PANIER
        btnCart.setOnAction(e ->
                AppNavigator.navigateTo("cart.fxml")
        );

        // DASHBOARD
        btnDashboard.setOnAction(e ->
                AppNavigator.navigateTo("Admin/AdminDashboard.fxml")
        );

        // ACCOUNT
        btnAccount.setOnAction(e ->
                AppNavigator.navigateTo("Account.fxml")
        );

        // SEARCH (optional)
        searchBar.setOnAction(e -> {
            String query = searchBar.getText().trim();
            System.out.println("Searching: " + query);
            // AppNavigator.navigateToWithParams("search.fxml", Map.of("q", query));
        });
    }
}
