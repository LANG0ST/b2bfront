package org.fx.b2bfront.model;

public class ShippingAddress {

    private String street;
    private String city;
    private String phoneNumber;

    public ShippingAddress() {}

    public ShippingAddress(String street, String city, String phoneNumber) {
        this.street = street;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ShippingAddress{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
