package org.fx.b2bfront.model;

public class ProductStats {

    private Long productId;
    private String productName;
    private double total;

    public ProductStats() {
        super();
    }

    public ProductStats(Long productId, String productName, double total) {
        this.productId = productId;
        this.productName = productName;
        this.total = total;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ProductStats{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", total=" + total +
                '}';
    }
}
