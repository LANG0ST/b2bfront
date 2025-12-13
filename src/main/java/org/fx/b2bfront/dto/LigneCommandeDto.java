package org.fx.b2bfront.dto;

public class LigneCommandeDto {

    private String produitName;
    private int quantite;
    private double prixUnitaire;

    public String getProduitName() { return produitName; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }

    public void setProduitName(String produitName) { this.produitName = produitName; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}
