package org.fx.b2bfront.model;

public enum PaymentStatus {
    EN_ATTENTE("En attente"),
    VALIDE("Validé"),
    REFUSE("Refusé"),
    REMBOURSE("Remboursé");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
