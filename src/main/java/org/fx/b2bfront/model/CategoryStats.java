package org.fx.b2bfront.model;

public class CategoryStats {

    private Integer categoryId;
    private String categoryName;
    private double total;

    public CategoryStats() {
        super();
    }

    public CategoryStats(Integer categoryId, String categoryName, double total) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.total = total;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CategoryStats{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", total=" + total +
                '}';
    }
}
