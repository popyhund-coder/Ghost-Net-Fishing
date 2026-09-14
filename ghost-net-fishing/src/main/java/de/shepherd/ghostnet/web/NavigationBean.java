package de.shepherd.ghostnet.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class NavigationBean {
    public String home() { return "/index.xhtml?faces-redirect=true"; }
    public String report() { return "/report.xhtml?faces-redirect=true"; }
    public String rescue() { return "/rescue.xhtml?faces-redirect=true"; }
    public String recover() { return "/recover.xhtml?faces-redirect=true"; }
    public String lost() { return "/lost.xhtml?faces-redirect=true"; }
}
