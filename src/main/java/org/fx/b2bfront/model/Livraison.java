package org.fx.b2bfront.model;

import java.time.LocalDate;

public class Livraison {

    private Long idLivraison;
    private String adresse;
    private String ville;
    private String codePostal;
    private String telephone;
    private String transporteur;
    private double fraisLivraison;
    private LocalDate dateEnvoi;
    private LocalDate dateEstimee;
    private Commande commande;

    public Livraison() {
        super();
    }

    public Livraison(Long idLivraison, String adresse, String ville, String codePostal, String telephone, String transporteur, double fraisLivraison, LocalDate dateEnvoi, LocalDate dateEstimee, Commande commande) {
        this.idLivraison = idLivraison;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.telephone = telephone;
        this.transporteur = transporteur;
        this.fraisLivraison = fraisLivraison;
        this.dateEnvoi = dateEnvoi;
        this.dateEstimee = dateEstimee;
        this.commande = commande;
    }

    public Long getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(Long idLivraison) {
        this.idLivraison = idLivraison;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTransporteur() {
        return transporteur;
    }

    public void setTransporteur(String transporteur) {
        this.transporteur = transporteur;
    }

    public double getFraisLivraison() {
        return fraisLivraison;
    }

    public void setFraisLivraison(double fraisLivraison) {
        this.fraisLivraison = fraisLivraison;
    }

    public LocalDate getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDate dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public LocalDate getDateEstimee() {
        return dateEstimee;
    }

    public void setDateEstimee(LocalDate dateEstimee) {
        this.dateEstimee = dateEstimee;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public String toString() {
        return "Livraison{" +
                "idLivraison=" + idLivraison +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", telephone='" + telephone + '\'' +
                ", transporteur='" + transporteur + '\'' +
                ", fraisLivraison=" + fraisLivraison +
                ", dateEnvoi=" + dateEnvoi +
                ", dateEstimee=" + dateEstimee +
                ", commande=" + commande +
                '}';
    }
}
