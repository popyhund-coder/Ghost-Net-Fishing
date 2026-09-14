package de.shepherd.ghostnet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ghost_net")
public class GhostNet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "estimated_size_m2", nullable = false)
    private double estimatedSizeM2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GhostNetStatus status = GhostNetStatus.REPORTED;

    @Column(name = "reporter_name", nullable = false, length = 100)
    private String reporterName;

    @Column(name = "reporter_phone", length = 30)
    private String reporterPhone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rescuer_id")
    private Rescuer rescuer;

    public Long getId() { return id; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public double getEstimatedSizeM2() { return estimatedSizeM2; }
    public void setEstimatedSizeM2(double estimatedSizeM2) { this.estimatedSizeM2 = estimatedSizeM2; }
    public GhostNetStatus getStatus() { return status; }
    public void setStatus(GhostNetStatus status) { this.status = status; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public String getReporterPhone() { return reporterPhone; }
    public void setReporterPhone(String reporterPhone) { this.reporterPhone = reporterPhone; }
    public Rescuer getRescuer() { return rescuer; }
    public void setRescuer(Rescuer rescuer) { this.rescuer = rescuer; }
}
