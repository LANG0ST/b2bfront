package org.fx.b2bfront.dto;

public class ProductStatsDto {

    private long productId;
    private String productName;
    private double total;

    public ProductStatsDto() {}

    public ProductStatsDto(long productId, String productName, double total) {
        this.productId = productId;
        this.productName = productName;
        this.total = total;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getTotal() {
        return total;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
