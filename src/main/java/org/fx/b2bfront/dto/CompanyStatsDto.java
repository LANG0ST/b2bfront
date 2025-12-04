package org.fx.b2bfront.dto;

public class CompanyStatsDto {

    private Long companyId;
    private String companyName;
    private double total;

    public CompanyStatsDto() {}

    public CompanyStatsDto(Long companyId, String companyName, double total) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.total = total;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getTotal() {
        return total;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
