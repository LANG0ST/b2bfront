package org.fx.b2bfront.store;

public class AuthStore {
    public static String jwt;
    public static String role;
    public static Long companyId;
    public static String email;

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public static boolean isCompany() {
        return "COMPANY".equalsIgnoreCase(role);
    }
}
