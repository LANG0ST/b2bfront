package org.fx.b2bfront.model;

public class LigneCommande {

    private Long idLigneCommande;
    private Commande commande;
    private Product product;
    private int quantite;
    private double prixUnitaire;
    private Double sellerLinePrix;

    public LigneCommande() {
        super();
    }

    public LigneCommande(Long idLigneCommande, Commande commande, Product product, int quantite, double prixUnitaire, Double sellerLinePrix) {
        this.idLigneCommande = idLigneCommande;
        this.commande = commande;
        this.product = product;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sellerLinePrix = sellerLinePrix;
    }

    public Long getIdLigneCommande() {
        return idLigneCommande;
    }

    public void setIdLigneCommande(Long idLigneCommande) {
        this.idLigneCommande = idLigneCommande;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Product getProduit() {
        return product;
    }

    public void setProduit(Product product) {
        this.product = product;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Double getSellerLinePrix() {
        return sellerLinePrix;
    }

    public void setSellerLinePrix(Double sellerLinePrix) {
        this.sellerLinePrix = sellerLinePrix;
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "idLigneCommande=" + idLigneCommande +
                ", commande=" + commande +
                ", produit=" + product +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", sellerLinePrix=" + sellerLinePrix +
                '}';
    }
}
