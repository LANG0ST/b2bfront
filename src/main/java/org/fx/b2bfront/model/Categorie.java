package org.fx.b2bfront.model;

import java.util.List;

public class Categorie {

    private Integer idCat;
    private String description;
    private String name;
    private List<Produit> produits;

    public Categorie() {
        super();
    }

    public Categorie(Integer idCat, String name) {
        this.idCat = idCat;
        this.name = name;
    }

    public Categorie(Integer idCat, String description, String name, List<Produit> produits) {
        this.idCat = idCat;
        this.description = description;
        this.name = name;
        this.produits = produits;
    }

    public Integer getIdCat() {
        return idCat;
    }

    public void setIdCat(Integer idCat) {
        this.idCat = idCat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "idCat=" + idCat +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", produits=" + produits +
                '}';
    }
}