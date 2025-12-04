package org.fx.b2bfront.dto;

public class CategoryStatsDto {

    private Integer categoryId;
    private String categoryName;
    private double total;

    public CategoryStatsDto() {}

    public CategoryStatsDto(Integer categoryId, String categoryName, double total) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.total = total;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getTotal() {
        return total;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
