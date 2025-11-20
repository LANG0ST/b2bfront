package org.fx.b2bfront.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commande {

    private Long id;
    private String refCommande;
    private LocalDate dateCommande;
    private Company company;
    private List<LigneCommande> lignes = new ArrayList<>();
    private StatutCommande statut;
    private Livraison livraison;
    private List<Payment> paiements = new ArrayList<>();

    public Commande() {
        super();
    }

    public Commande(Long id, String refCommande, LocalDate dateCommande, Company company, List<LigneCommande> lignes, StatutCommande statut, Livraison livraison, List<Payment> paiements) {
        this.id = id;
        this.refCommande = refCommande;
        this.dateCommande = dateCommande;
        this.company = company;
        this.lignes = lignes;
        this.statut = statut;
        this.livraison = livraison;
        this.paiements = paiements;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefCommande() {
        return refCommande;
    }

    public void setRefCommande(String refCommande) {
        this.refCommande = refCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public List<Payment> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Payment> paiements) {
        this.paiements = paiements;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", refCommande='" + refCommande + '\'' +
                ", dateCommande=" + dateCommande +
                ", company=" + company +
                ", lignes=" + lignes +
                ", statut=" + statut +
                ", livraison=" + livraison +
                ", paiements=" + paiements +
                '}';
    }
}
