package org.fx.b2bfront.store;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class AppStore {

    private static final BooleanProperty hasUnreadNotifications =
            new SimpleBooleanProperty(false);

    private static String searchQuery = null;

    // =========================
    // NOTIFICATIONS
    // =========================
    public static boolean hasNotifications() {
        return hasUnreadNotifications.get();
    }

    public static void setHasNotifications(boolean value) {
        hasUnreadNotifications.set(value);
    }

    public static BooleanProperty hasNotificationsProperty() {
        return hasUnreadNotifications;
    }

    // =========================
    // SEARCH
    // =========================
    public static void setSearchQuery(String q) {
        searchQuery = q;
    }

    public static String getSearchQuery() {
        return searchQuery;
    }

    // =========================
    // LOGOUT SUPPORT
    // =========================
    public static void clear() {
        hasUnreadNotifications.set(false);
        searchQuery = null;
    }
}
