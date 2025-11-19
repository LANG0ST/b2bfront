package org.fx.b2bfront.model;

public class LignePanier {

    private Long idLignePanier;
    private Panier panier;
    private Produit produit;
    private int quantite;


    public Long getIdLignePanier() {
        return idLignePanier;
    }

    public void setIdLignePanier(Long idLignePanier) {
        this.idLignePanier = idLignePanier;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getSousTotal() {
        if (produit != null && produit.getPrice() != null) {
            return quantite * produit.getPrice().doubleValue();
        }
        return 0.0;
    }
}
