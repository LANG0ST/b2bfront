package org.fx.b2bfront.model;

import java.time.LocalDate;

public class Avis {

    private Long idAvis;
    private String feedback;
    private int note;
    private LocalDate dateCreation;
    private Company company;
    private Produit produit;
    private EtatAvis etat;

    public Avis() {
        super();
    }

    public Avis(Long idAvis, String feedback, int note, LocalDate dateCreation, Company company, Produit produit, EtatAvis etat) {
        this.idAvis = idAvis;
        this.feedback = feedback;
        this.note = note;
        this.dateCreation = dateCreation;
        this.company = company;
        this.produit = produit;
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

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
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
                ", produit=" + produit +
                ", etat=" + etat +
                '}';
    }
}
