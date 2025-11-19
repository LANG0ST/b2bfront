package org.fx.b2bfront.model;

import java.time.LocalDate;

public class Delivery {

    private Long id;
    private ShippingAddress shippingAddress;
    private String carrier;
    private Double shippingCost;
    private LocalDate shippingDate;
    private LocalDate estimatedDeliveryDate;
    private String trackingNumber;
    private DeliveryStatus status;
    private AdminUser user;
    private Commande order;

    public Delivery() {
        super();
    }

    public Delivery(Long id, ShippingAddress shippingAddress, String carrier, Double shippingCost, LocalDate shippingDate, LocalDate estimatedDeliveryDate, String trackingNumber, DeliveryStatus status, AdminUser user, Commande order) {
        this.id = id;
        this.shippingAddress = shippingAddress;
        this.carrier = carrier;
        this.shippingCost = shippingCost;
        this.shippingDate = shippingDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.user = user;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public AdminUser getUser() {
        return user;
    }

    public void setUser(AdminUser user) {
        this.user = user;
    }

    public Commande getOrder() {
        return order;
    }

    public void setOrder(Commande order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", shippingAddress=" + shippingAddress +
                ", carrier='" + carrier + '\'' +
                ", shippingCost=" + shippingCost +
                ", shippingDate=" + shippingDate +
                ", estimatedDeliveryDate=" + estimatedDeliveryDate +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", status=" + status +
                ", user=" + user +
                ", order=" + order +
                '}';
    }
}
