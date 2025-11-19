package org.fx.b2bfront.model;

import java.time.LocalDateTime;
import java.util.List;

public class Company {

    private Long id;
    private LocalDateTime createdAt;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String website;
    private String email;
    private String password;
    private boolean mustChangePassword;
    private String fullName;
    private Role role;
    private boolean enabled;
    private List<Produit> produits;

    public Company() {
        super();
    }

    public Company(Long id, LocalDateTime createdAt, String name, String address, String city, String phone, String website, String email, String password, boolean enabled) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.website = website;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public Company(Long id, LocalDateTime createdAt, String name, String address, String city, String phone, String website, String email, String password, boolean mustChangePassword, String fullName, Role role, boolean enabled, List<Produit> produits) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.website = website;
        this.email = email;
        this.password = password;
        this.mustChangePassword = mustChangePassword;
        this.fullName = fullName;
        this.role = role;
        this.enabled = enabled;
        this.produits = produits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mustChangePassword=" + mustChangePassword +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", enabled=" + enabled +
                ", produits=" + produits +
                '}';
    }
}
