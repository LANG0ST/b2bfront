package org.fx.b2bfront.model;

import java.math.BigDecimal;
import java.util.List;

public class Produit {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Company company;
    private Categorie categorie;
    private List<Avis> avis;
    private List<LigneCommande> lignesCommande;
    private List<LignePanier> lignesPanier;

    public Produit() {
        super();
    }

    public Produit(Long id, String name, String description, BigDecimal price, Integer stock, Company company, Categorie categorie, List<Avis> avis, List<LigneCommande> lignesCommande, List<LignePanier> lignesPanier) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.company = company;
        this.categorie = categorie;
        this.avis = avis;
        this.lignesCommande = lignesCommande;
        this.lignesPanier = lignesPanier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<Avis> getAvis() {
        return avis;
    }

    public void setAvis(List<Avis> avis) {
        this.avis = avis;
    }

    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    public List<LignePanier> getLignesPanier() {
        return lignesPanier;
    }

    public void setLignesPanier(List<LignePanier> lignesPanier) {
        this.lignesPanier = lignesPanier;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", company=" + company +
                ", categorie=" + categorie +
                ", avis=" + avis +
                ", lignesCommande=" + lignesCommande +
                ", lignesPanier=" + lignesPanier +
                '}';
    }
}
