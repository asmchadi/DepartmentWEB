package com.department.cd;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.department.ejb.CycleService;
import com.department.ejb.EnseignantService;
import com.department.ejb.FiliereService;
import com.department.ejb.FormationService;
import com.department.entities.Cycle;
import com.department.entities.Enseignant;
import com.department.entities.Filiere;
import com.department.entities.Formation;


@ManagedBean(name = "cd_filieres")
//@ViewScoped
public class FilieresBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private EnseignantService ensService;
	@EJB
	private FiliereService filService;
	@EJB
	private CycleService cycleService;
	@EJB
	private FormationService frmService;	

	private List<Filiere> filieres;
	private List<Enseignant> enseignants;
	private List<Formation> formations;
	private List<Cycle> cycles; 
	private Filiere filiere;
	

	public FilieresBean() {
		System.out.println("in the constructor");
		this.filiere = new Filiere();
		this.filiere.setCycle(new Cycle());
		this.filiere.setFormation(new Formation());
		this.filiere.setCoordonnateur(new Enseignant());
	}



	public List<Enseignant> getEnseignants() {
		setEnseignants(ensService.findAll(null));
		return enseignants;
	}

	public void setEnseignants(List<Enseignant> enseignants) {
		this.enseignants = enseignants;
	}

	public List<Formation> getFormations() {
		setFormations(frmService.findAll(null));
		return formations;
	}

	public void setFormations(List<Formation> formations) {
		this.formations = formations;
	}

	public List<Cycle> getCycles() {
		setCycles(cycleService.findAll(null));
		return cycles;
	}

	public void setCycles(List<Cycle> cycles) {
		this.cycles = cycles;
	}
	 
	public List<Filiere> getFilieres() {
		filieres = filService.findAll(null);
		return filieres;
	}

	public Filiere getFiliere() {
		return filiere;
	}
	
	public String showFiliere(String index) {
		System.out.println(index);
		this.filiere = filieres.get(Integer.parseInt(index));
		System.out.println(filiere);
		return "show.xhtml";
	}

	public String newFiliere() {
		this.filiere = new Filiere();
		return "show.xhtml";
	}
	
	public void saveFiliere() {
		System.out.println(filiere.getCycle().getIntitule()+filiere.getCycle().getId());
		FacesMessage msg = new FacesMessage();
		try {			
			System.out.println(filiere.getIntitule());
			Cycle cycle = filiere.getCycle();
			Formation formation = filiere.getFormation();
			if(filiere.getCycle().getId()==-1){
				cycleService.create(new Cycle(null,filiere.getCycle().getIntitule()));
				cycle = cycleService.findByField("intitule", filiere.getCycle().getIntitule(), null).get(0);
			}
			if(filiere.getFormation().getId()==-1){
				frmService.create(new Formation(null,filiere.getFormation().getIntitule()));
				formation = frmService.findByField("intitule", filiere.getFormation().getIntitule(), null).get(0);
			}
			Filiere f = new Filiere(filiere.getId(), filiere.getIntitule(), cycle, formation, filiere.getCoordonnateur());
			filService.update(f);
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Filiere enregistrer");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Filiere enregistrement echoue");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String dropFiliere(int index) {
		System.out.println("drop " + filiere.getId());
		try { 
			filService.delete(filieres.get(index));// createQuery("delete from Filiere where id = "+filiere.getId()).executeUpdate();
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Filiere suppression echoue", e.getMessage()));
		}
		return "";
	}

	
}

