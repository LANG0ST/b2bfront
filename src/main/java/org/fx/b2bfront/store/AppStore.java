package org.fx.b2bfront.store;

public class AppStore {

    private static boolean hasUnreadNotifications = false;
    private static String searchQuery = null;

    public static boolean hasNotifications() {
        return hasUnreadNotifications;
    }

    public static void setHasNotifications(boolean value) {
        hasUnreadNotifications = value;
    }

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
        hasUnreadNotifications = false;
        searchQuery = null;
    }
}
