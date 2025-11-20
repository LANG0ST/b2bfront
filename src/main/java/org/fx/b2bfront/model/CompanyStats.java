package org.fx.b2bfront.model;

public class CompanyStats {

    private Long companyId;
    private String companyName;
    private double total;

    public CompanyStats() {
        super();
    }

    public CompanyStats(Long companyId, String companyName, double total) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.total = total;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CompanyStats{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", total=" + total +
                '}';
    }
}
