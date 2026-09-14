package de.shepherd.ghostnet.entity;

public enum GhostNetStatus {
    REPORTED("Gemeldet"),
    RESCUE_PENDING("Bergung bevorstehend"),
    RECOVERED("Geborgen"),
    LOST("Verschollen");

    private final String label;

    GhostNetStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
