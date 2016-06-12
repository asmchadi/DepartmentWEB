package com.department.cd;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.department.ejb.PvDefaultService;
import com.department.entities.PvDefault;

@ManagedBean(name = "cd_pvmod")
@SessionScoped
public class PvDefaultBean {

	@EJB
	private PvDefaultService pvmService;
	private List<PvDefault> pvmodels;
	private PvDefault pvmodel;

	public List<PvDefault> getPvmodels() {
		setPvmodels(pvmService.findAll(null));
		return pvmodels;
	}

	public void setPvmodels(List<PvDefault> pvmodels) {
		this.pvmodels = pvmodels;
	}

	public PvDefault getPvmodel() {
		return pvmodel;
	}

	public void setPvmodel(PvDefault pvmodel) {
		this.pvmodel = pvmodel;
	}
	
	public String show(String index) {
		this.pvmodel = pvmodels.get(Integer.parseInt(index)); 
		return "show.xhtml";
	}

	public String show() {
		this.pvmodel = new PvDefault();
		return "show.xhtml";
	}
	
	public void save() {
		FacesMessage msg = new FacesMessage();
		try {
			if (pvmodel.getId() == null)
				pvmService.create(pvmodel);
			else {
				PvDefault d = pvmService.findById(pvmodel.getId());
				d.setIntitule(pvmodel.getIntitule());
				d.setModel(pvmodel.getModel());
				pvmService.update(d);
			}
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("PvDefault enregistrer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("PvDefault enregistrement échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String delete() {
		System.out.println("drop " + pvmodel.getId());
		try {
			pvmService.delete(pvmodel);// createQuery("delete from PvDefault where id = "+pvmodel.getId()).executeUpdate();
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"PvDefault suppression échouée", e.getMessage()));
		}
		return "";
	}

}
