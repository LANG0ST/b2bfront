package org.fx.b2bfront.model;

import java.time.LocalDateTime;

public class Payment {

    private Long id;
    private String orderId;
    private Double amount;
    private String method;
    private String reference;
    private String notes;
    private String transactionId;
    private LocalDateTime date;
    private LocalDateTime validationDate;
    private String history;
    private StatutPaiement status;
    private Long userId;
    private Long deliveryId;
    private Commande commande;

    public Payment() {
        super();
    }

    public Payment(Long id, String orderId, Double amount, String method, String reference, String notes, String transactionId,
                   LocalDateTime date, LocalDateTime validationDate, String history, StatutPaiement status, Long userId,
                   Long deliveryId, Commande commande) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.reference = reference;
        this.notes = notes;
        this.transactionId = transactionId;
        this.date = date;
        this.validationDate = validationDate;
        this.history = history;
        this.status = status;
        this.userId = userId;
        this.deliveryId = deliveryId;
        this.commande = commande;
    }

    public Payment(String orderId, Double amount, String method, String reference, String notes) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.reference = reference;
        this.notes = notes;
        this.date = LocalDateTime.now();
        this.status = StatutPaiement.EN_ATTENTE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(LocalDateTime validationDate) {
        this.validationDate = validationDate;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public StatutPaiement getStatus() {
        return status;
    }

    public void setStatus(StatutPaiement status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", method='" + method + '\'' +
                ", reference='" + reference + '\'' +
                ", notes='" + notes + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", date=" + date +
                ", validationDate=" + validationDate +
                ", history='" + history + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                ", deliveryId=" + deliveryId +
                ", commande=" + commande +
                '}';
    }
}
