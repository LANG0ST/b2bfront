package org.fx.b2bfront.store;

public class CheckoutStore {

    // Address step
    public static String contactName;
    public static String address;
    public static String city;
    public static String zipcode;

    // Shipping step
    public static String shippingMethod;
    public static double shippingCost = 0.0;

    // Payment step
    public static String cardNumber;
    public static String expiration;
    public static String cvv;

    public static void reset() {
        contactName = null;
        address = null;
        city = null;
        zipcode = null;
        shippingMethod = null;
        shippingCost = 0.0;

        cardNumber = null;
        expiration = null;
        cvv = null;
    }
}
