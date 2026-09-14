package de.shepherd.ghostnet.web;

import de.shepherd.ghostnet.entity.GhostNet;
import de.shepherd.ghostnet.service.GhostNetService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class GhostNetBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private GhostNetService service;

    private GhostNet newNet = new GhostNet();
    private String reporterName;
    private String reporterPhone;
    private String contactName;
    private String contactPhone;
    private Long selectedNetId;

    public List<GhostNet> getAllNets() { return service.findAll(); }
    public List<GhostNet> getOpenNets() { return service.findOpen(); }
    public List<GhostNet> getAssignedOpenNets() { return service.findAssignedOpen(); }

    public String createReport() {
        newNet.setReporterName(isBlank(reporterName) ? "Anonym" : reporterName.trim());
        newNet.setReporterPhone(isBlank(reporterPhone) ? null : reporterPhone.trim());
        service.save(newNet);
        addMessage(FacesMessage.SEVERITY_INFO, "Geisternetz wurde erfolgreich gemeldet.");
        newNet = new GhostNet();
        reporterName = null;
        reporterPhone = null;
        return "/index.xhtml?faces-redirect=true";
    }

    public String claim() {
        if (!validContact()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Name und Telefonnummer der bergenden Person sind erforderlich.");
            return null;
        }
        boolean success = service.claim(selectedNetId, contactName.trim(), contactPhone.trim());
        addMessage(success ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR,
                success ? "Bergung wurde eingetragen." : "Das Geisternetz ist bereits zugeordnet oder nicht mehr verfügbar.");
        clearContact();
        return success ? "/rescue.xhtml?faces-redirect=true" : null;
    }

    public String recover() {
        if (!validContact()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Name und Telefonnummer sind erforderlich.");
            return null;
        }
        boolean success = service.markRecovered(selectedNetId, contactName.trim(), contactPhone.trim());
        addMessage(success ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR,
                success ? "Geisternetz wurde als geborgen erfasst." : "Die angegebenen Kontaktdaten passen nicht zur Bergung.");
        clearContact();
        return success ? "/recover.xhtml?faces-redirect=true" : null;
    }

    public String markLost() {
        if (!validContact()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Name und Telefonnummer sind erforderlich.");
            return null;
        }
        boolean success = service.markLost(selectedNetId, contactName.trim(), contactPhone.trim());
        addMessage(success ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR,
                success ? "Geisternetz wurde als verschollen gemeldet." : "Das Geisternetz kann nicht als verschollen markiert werden.");
        clearContact();
        return success ? "/lost.xhtml?faces-redirect=true" : null;
    }

    private boolean validContact() {
        return !isBlank(contactName) && !isBlank(contactPhone)
                && contactName.length() <= 100 && contactPhone.length() <= 30;
    }

    private boolean isBlank(String value) { return value == null || value.isBlank(); }

    private void clearContact() {
        contactName = null;
        contactPhone = null;
        selectedNetId = null;
    }

    private void addMessage(FacesMessage.Severity severity, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
    }

    public GhostNet getNewNet() { return newNet; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public String getReporterPhone() { return reporterPhone; }
    public void setReporterPhone(String reporterPhone) { this.reporterPhone = reporterPhone; }
    public String getRescuerName() { return contactName; }
    public void setRescuerName(String contactName) { this.contactName = contactName; }
    public String getRescuerPhone() { return contactPhone; }
    public void setRescuerPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Long getSelectedNetId() { return selectedNetId; }
    public void setSelectedNetId(Long selectedNetId) { this.selectedNetId = selectedNetId; }
}
