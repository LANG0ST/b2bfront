package org.fx.b2bfront.controller.components;

import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.fx.b2bfront.utils.AppNavigator;

public class NavbarController {

    @FXML private Button btnCategories;
    @FXML private Button btnNotifications;

    private ContextMenu categoriesMenu;

    @FXML
    public void initialize() {
        setupCategoriesMenu();

        // Notifications button stays visible but no dropdown anymore
        btnNotifications.setOnAction(e -> {
            System.out.println("Notifications clicked!");
            AppNavigator.navigateTo("categories.fxml");
        });
    }

    private void setupCategoriesMenu() {
        categoriesMenu = new ContextMenu();
        categoriesMenu.getItems().addAll(
                createCategoryItem("Electronics"),
                createCategoryItem("Machinery"),
                createCategoryItem("Clothing"),
                createCategoryItem("Food & Beverage"),
                createCategoryItem("Office Supplies")
        );

        btnCategories.setOnAction(event -> {
            if (categoriesMenu.isShowing()) {
                categoriesMenu.hide();
            } else {
                categoriesMenu.show(btnCategories, Side.BOTTOM, 0, 0);
            }
        });
    }

    private CustomMenuItem createCategoryItem(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("dropdown-text");

        VBox wrapper = new VBox(lbl);
        wrapper.getStyleClass().add("dropdown-item");
        wrapper.setPrefWidth(200); // ensures consistent width

        CustomMenuItem item = new CustomMenuItem(wrapper, false);
        item.setHideOnClick(false);   // keeps menu open unless clicked
        return item;
    }

}
