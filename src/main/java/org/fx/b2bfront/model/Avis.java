package org.fx.b2bfront.model;

import java.time.LocalDate;

public class Avis {

    private Long idAvis;
    private String feedback;
    private int note;
    private LocalDate dateCreation;
    private Company company;
    private Product product;
    private EtatAvis etat;

    public Avis() {
        super();
    }

    public Avis(Long idAvis, String feedback, int note, LocalDate dateCreation, Company company, Product product, EtatAvis etat) {
        this.idAvis = idAvis;
        this.feedback = feedback;
        this.note = note;
        this.dateCreation = dateCreation;
        this.company = company;
        this.product = product;
        this.etat = etat;
    }

    public Long getIdAvis() {
        return idAvis;
    }

    public void setIdAvis(Long idAvis) {
        this.idAvis = idAvis;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Product getProduit() {
        return product;
    }

    public void setProduit(Product product) {
        this.product = product;
    }

    public EtatAvis getEtat() {
        return etat;
    }

    public void setEtat(EtatAvis etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "idAvis=" + idAvis +
                ", feedback='" + feedback + '\'' +
                ", note=" + note +
                ", dateCreation=" + dateCreation +
                ", company=" + company +
                ", produit=" + product +
                ", etat=" + etat +
                '}';
    }
}
