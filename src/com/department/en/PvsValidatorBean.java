package com.department.en;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import com.department.ejb.PvValidatorsService;
import com.department.entities.PvValidators;;


@ManagedBean(name = "e_pvsvalidator")
public class PvsValidatorBean {
	private PvValidators pvValidator;
	private UploadedFile file;
	private List<PvValidators> pvValidators;
	@EJB
	private PvValidatorsService pvValidatorsService;

	public PvsValidatorBean() {
		this.pvValidator = new PvValidators(); 
	}

	public List<PvValidators> getPvValidators() {
		pvValidators = pvValidatorsService.findAll("DATE desc");
		return pvValidators;
	}

	public PvValidators getPvValidator() {
		return pvValidator;
	}
	
	public String showPvValidator(String index) {
		System.out.println(index);
		this.pvValidator = pvValidators.get(Integer.parseInt(index));
		System.out.println(pvValidator);
		return "show.xhtml";
	}

	public String newPvValidator() {
		this.pvValidator = new PvValidators();
		return "show.xhtml";
	}

	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public DefaultStreamedContent streamContent(byte[] file,String name,String type)
			throws Exception {
		ByteArrayInputStream array = new ByteArrayInputStream(file);
		DefaultStreamedContent content = new DefaultStreamedContent(array,type);
		content.setName(name);
		return content;
	}

	public void addPvValidator(PvValidators pvValidator) {
		FacesMessage msg = new FacesMessage();
		try {
			pvValidatorsService.create(pvValidator);
			
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("PvValidator enregistrer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("PvValidator enregistrement échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String dropPvValidator() {
		System.out.println("drop " + pvValidator.getId());
		try { 
			pvValidatorsService.delete(pvValidator);
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"PvValidator suppression échouée", e.getMessage()));
		}
		return "";
	}
}