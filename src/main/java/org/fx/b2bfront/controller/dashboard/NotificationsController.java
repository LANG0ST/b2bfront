package org.fx.b2bfront.controller.dashboard;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.application.Platform;
import org.fx.b2bfront.api.NotificationApi;
import org.fx.b2bfront.dto.NotificationDto;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.AppStore;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class NotificationsController implements Initializable {

    @FXML private ListView<NotificationDto> notificationsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notificationsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(NotificationDto n, boolean empty) {
                super.updateItem(n, empty);
                if (empty || n == null) {
                    setText(null);
                } else {
                    String date = n.getCreatedAt() != null
                            ? n.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM HH:mm"))
                            : "";

                    String prefix = n.isRead() ? "" : "• ";

                    setText(prefix + date + " - " + n.getMessage());
                }
            }
        });

        loadAndMarkRead();
    }

    private void loadAndMarkRead() {
        Long companyId = AuthStore.companyId;
        if (companyId == null) return;

        new Thread(() -> {
            try {
                // Load notifications
                List<NotificationDto> list = NotificationApi.getNotifications(companyId);

                Platform.runLater(() ->
                        notificationsList.setItems(FXCollections.observableArrayList(list))
                );

                // Mark unread notifications as read
                for (NotificationDto n : list) {
                    if (!n.isRead()) {
                        NotificationApi.markAsRead(n.getId());
                    }
                }

                // Update global store
                AppStore.setHasNotifications(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
