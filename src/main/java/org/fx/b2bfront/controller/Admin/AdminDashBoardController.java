package org.fx.b2bfront.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.fx.b2bfront.utils.AppNavigator;

public class AdminDashBoardController {

    // ========== STATISTICS LABELS ==========
    @FXML
    private Label usersCount;

    @FXML
    private Label productsCount;

    @FXML
    private Label commadeCount;

    // Called automatically when the FXML is loaded
    @FXML
    public void initialize() {
        System.out.println("Admin Dashboard initialized.");

        // Fake values for now — replace with real service calls
        usersCount.setText("0");
        productsCount.setText("0");
        commadeCount.setText("0");
    }


    // ============================================
    //                NAVIGATION
    // ============================================
    @FXML
    private void openDashboard(MouseEvent event) {
        System.out.println("Dashboard clicked");
        // Already here, so no navigation.
    }

    @FXML
    private void openUsersModule(MouseEvent event) {
        System.out.println("Opening Users Module...");
        AppNavigator.navigateTo("GestionUsers.fxml");
    }

    @FXML
    private void openProductsModule(MouseEvent event) {
        System.out.println("Opening Products Module...");
        AppNavigator.navigateTo("GestionProducts.fxml");
    }

    @FXML
    private void openAuthpage(MouseEvent event) {
        System.out.println("Back to authentification page.");
        AppNavigator.navigateTo("auth.fxml");
    }



    // ============================================
    //                HELPER METHOD
    // ============================================
    private void navigate(String fxmlPath) {
        try {
            org.fx.b2bfront.utils.AppNavigator.navigateTo(fxmlPath);
        } catch (Exception e) {
            System.err.println("❌ Navigation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
