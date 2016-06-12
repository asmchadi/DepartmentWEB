package com.department.cd;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.department.ejb.PvDefaultService;
import com.department.ejb.PvService;
import com.department.ejb.PvValidatorsService;
import com.department.entities.Pv;
import com.department.entities.PvDefault;
import com.department.entities.PvValidators;
import com.department.utils.Status;

@ManagedBean(name = "cd_pvs")
@SessionScoped
public class PvBean {

	@EJB
	private PvService pvService;
	@EJB
	private PvValidatorsService validatorsService;
	@EJB
	private PvDefaultService pvmService;
	
	private List<PvDefault> pvmodels;
	private List<Pv> pvs;
	private List<PvValidators> validators;
	private Pv pv;
	private PvValidators validator;
	private String model;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public PvValidators getValidator() {
		try {
			if (validator == null){
				validator = validators.get(0);
			System.out.println("____________");
			String input = pv.getText();
			String test  = validator.getContent();
			validator.setContent(htmlCorrection(input, test, "red"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return validator;
	}

	public void selected() {
		System.out.println("selected<<<<" + validator.getId());
		// <span style="color:#FF0000">dfsdfs</span>
		validator = validatorsService.findById(validator.getId());
		String input = pv.getText();
		String test  = validator.getContent();
		validator.setContent(htmlCorrection(input, test, "red"));
		System.out.println("selected<<<<" + validator.getContent());
	}

	public void selectedModel() {
		pv.setText(model);
	}

	public String htmlCorrection(String input, String test, String color) {
		test = test.replace("<", " <");
		test = test.replace(">", "> ");
		input = input.replace("<", " <");
		input = input.replace(">", "> ");
		String[] words = test.split(" ");
		for (int j = 0 ;j < words.length;j++) {
				if (!input.contains(words[j].trim())) {
					words[j] = words[j].replace(words[j].trim(), "<span style=\"color:"+ color +"\">" + words[j].trim() + "</span>");
					continue;
				}
			} 
		test = String.join(" ", words);
		return test;
	}
	
	public static void main(String[] args) {
		String input = "<p>abdelhofid el kadiiri</p>";
		String test = "<p>abdelhafid al kadiri</p>";
		System.out.println();
		System.out.println(new PvBean().htmlCorrection(input, test, "red"));
	}

	public void setValidator(PvValidators validator) {
		this.validator = validator;
	}

	public PvBean() {
		pv = new Pv();
	}

	public List<Pv> getPvs() throws Exception {
		// pv = new Pv();
		// pv.setText("testtest");
		// pvService.create(pv);
		this.pvs = pvService.findAll("date desc");
		this.pv = getCurrentPV();
		return pvs;
	}

	public List<PvValidators> getValidators() {
		setValidators(validatorsService.findByField("pv_id", this.pv.getId(),
				"date desc"));
		return validators;
	}
	
	public List<PvDefault> getPvmodels() {
		setPvmodels(pvmService.findAll(null));
		return pvmodels;
	}

	public void setPvmodels(List<PvDefault> pvmodels) {
		this.pvmodels = pvmodels;
	}

	public void setValidators(List<PvValidators> validators) {
		this.validators = validators;
	}

	public Pv getPv() {
		return pv;
	}

	public void setPv(Pv pv) {
		this.pv = pv;
	}

	public String showPv(String index) {
		this.pv = pvs.get(Integer.parseInt(index));
		getValidators();
		return "show.xhtml?faces-redirect=true";
	}

	public String currentPv() {
		if (this.pv == null) {
			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Pas de PV courant.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}
		getValidators();
		return "show.xhtml?faces-redirect=true";
	}

	public Pv getCurrentPV() {
		try {
			return pvs.stream().filter(new Predicate<Pv>() {
				@Override
				public boolean test(Pv t) {
					// TODO Auto-generated method stub
					return t.getStatus() == Status.NONVALID;
				}
			}).findFirst().get();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public String newPv() {
		if (this.pv != null) {
			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("valider le PV courant.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}
		this.pv = new Pv();
		return "new.xhtml";
	}

	public void addPv() {
		FacesMessage msg = new FacesMessage();
		try {
			System.out.println(pv.getText());
			pv.setDate(new Date());
			pv.setStatus(Status.NONVALID);
			pvService.create(pv);
			pv = new Pv();
			msg.setSummary("Pv publier avec succée");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Pv publication échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String savePv() {
		FacesMessage msg = new FacesMessage();
		try {
			System.out.println("_____" + pv.getId() + "---");
			pv.setStatus(Status.NONVALID);
			pvService.update(pv);
			msg.setSummary("Pv enregistrer avec succée");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Pv publication échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "";
	}

	public String validerPv() {
		FacesMessage msg = new FacesMessage();
		try {
			System.out.println("_____" + pv.getId() + "---");
			pv.setStatus(Status.VALID);
			pvService.update(pv);
			msg.setSummary("Pv valider avec succée");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Pv validation échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "";
	}

	public String dropPv(String index) {
		try {
			pvService.delete(pvs.get(Integer.valueOf(index)));
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Pv suppression échouée", e.getMessage()));
		}
		return "";
	}

}
