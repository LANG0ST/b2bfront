package org.fx.b2bfront.model;

public class LignePanier {

    private Long idLignePanier;
    private Panier panier;
    private Product product;
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

    public double getSousTotal() {
        if (product != null && product.getPrice() != null) {
            return quantite * product.getPrice().doubleValue();
        }
        return 0.0;
    }
}
