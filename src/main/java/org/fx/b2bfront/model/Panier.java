package org.fx.b2bfront.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Panier {

    private Long id;
    private Company company;
    private List<LignePanier> lignes = new ArrayList<>();
    private LocalDate dateCreation;
    private Double total;

    public Panier() {
        this.dateCreation = LocalDate.now();
    }

    public Panier(Company company) {
        this.company = company;
        this.dateCreation = LocalDate.now();
    }

    public Panier(Long id, Company company, List<LignePanier> lignes, LocalDate dateCreation, Double total) {
        this.id = id;
        this.company = company;
        this.lignes = lignes;
        this.dateCreation = dateCreation;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<LignePanier> getLignes() {
        return lignes;
    }

    public void setLignes(List<LignePanier> lignes) {
        this.lignes = lignes;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id=" + id +
                ", company=" + company +
                ", lignes=" + lignes +
                ", dateCreation=" + dateCreation +
                ", total=" + total +
                '}';
    }
}
