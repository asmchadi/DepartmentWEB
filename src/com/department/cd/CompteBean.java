package com.department.cd;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.department.ejb.EnseignantService;
import com.department.entities.Enseignant;

@ManagedBean(name = "cd_comptes")
public class CompteBean {
	@EJB
	private EnseignantService ensService;
	private List<Enseignant> enseignants;
	private Enseignant enseignant = new Enseignant();
	public List<Enseignant> getComptes() {
		setComptes(ensService.findAll("nom,prenom"));
		return enseignants;
	}

	public List<Enseignant> getEnseignants() {
		return enseignants;
	}

	public void setEnseignants(List<Enseignant> enseignants) {
		this.enseignants = enseignants;
	}

	public Enseignant getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}

	public void setComptes(List<Enseignant> enseignants) {
		this.enseignants = enseignants;
	}

	public Enseignant getCompte() {
		return enseignant;
	}

	public void setCompte(Enseignant enseignant) {
		this.enseignant = enseignant;
	}

	public String showCompte(String index) {
		System.out.println(index);
		this.enseignant = enseignants.get(Integer.parseInt(index));
		System.out.println(enseignant);
		return "show.xhtml";
	}

	public String newCompte() {
		return "new.xhtml";
	} 
	
	public void addCompte() {
		FacesMessage msg = new FacesMessage();
		try { 
			System.out.println("_____"+enseignant);
			ensService.create(new Enseignant(enseignant.getNom(), enseignant.getPrenom()));
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Compte enregistrer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Compte enregistrement échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String resetCompte(String index) {
		System.out.println("reset " + enseignant.getId());
		try {
			enseignant = enseignants.get(Integer.valueOf(index));
			enseignant.setPassword(enseignant.getNom().toLowerCase());
			ensService.update(enseignant);
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Compte restauration échouée", e.getMessage()));
		}
		return "";
	}

	public String dropCompte(String index) {
		System.out.println("drop " + enseignant.getId());
		try {
			enseignant = enseignants.get(Integer.valueOf(index));
			ensService.delete(enseignant);
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Compte suppression échouée", e.getMessage()));
		}
		return "";
	} 

	
}
