package org.fx.b2bfront.service;

import javafx.application.Platform;
import org.fx.b2bfront.api.NotificationApi;
import org.fx.b2bfront.store.AppStore;
import org.fx.b2bfront.store.AuthStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationPoller {

    private static ScheduledExecutorService scheduler;

    public static void start() {
        if (scheduler != null) return;

        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (AuthStore.companyId == null) return;

                var list = NotificationApi.getNotifications(AuthStore.companyId);
                boolean hasUnread = list.stream().anyMatch(n -> !n.isRead());

                Platform.runLater(() ->
                        AppStore.setHasNotifications(hasUnread)
                );

            } catch (Exception ignored) {}
        }, 0, 10, TimeUnit.SECONDS);
    }

    public static void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }
}
