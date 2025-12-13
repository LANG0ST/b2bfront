package org.fx.b2bfront.store;

public class AppStore {

    private static boolean hasUnreadNotifications = false;

    public static boolean hasNotifications() {
        return hasUnreadNotifications;
    }

    public static void setHasNotifications(boolean value) {
        hasUnreadNotifications = value;
    }
    private static String searchQuery = null;

    public static void setSearchQuery(String q) {
        searchQuery = q;
    }

    public static String getSearchQuery() {
        return searchQuery;
    }

}
