package org.fx.b2bfront.dto;

import java.util.List;

public class CommandeDto {

    private Long id;
    private String refCommande;
    private String dateCommande;
    private String statut;

    // same shape as backend
    private CompanyDto company;             // buyer company
    private List<LigneCommandeDto> lignes;  // order lines

    // ====== GETTERS ======
    public Long getId() { return id; }
    public String getRefCommande() { return refCommande; }
    public String getDateCommande() { return dateCommande; }
    public String getStatut() { return statut; }
    public CompanyDto getCompany() { return company; }
    public List<LigneCommandeDto> getLignes() { return lignes; }

    // Convenience for SellerOrdersController
    public String getBuyerName() {
        return (company != null && company.getName() != null)
                ? company.getName()
                : "";
    }

    // ====== SETTERS ======
    public void setId(Long id) { this.id = id; }
    public void setRefCommande(String refCommande) { this.refCommande = refCommande; }
    public void setDateCommande(String dateCommande) { this.dateCommande = dateCommande; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setCompany(CompanyDto company) { this.company = company; }
    public void setLignes(List<LigneCommandeDto> lignes) { this.lignes = lignes; }
}
