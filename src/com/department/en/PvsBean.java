package com.department.en;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.department.ejb.EnseignantService;
import com.department.ejb.NotificationService;
import com.department.ejb.PvService;
import com.department.ejb.PvValidatorsService;
import com.department.entities.Enseignant;
import com.department.entities.Pv;
import com.department.entities.PvValidators;
import com.department.utils.Status;


@ManagedBean(name = "e_pvs")
public class PvsBean {
	private Pv pv;
	private List<Pv> pvs;
	@EJB
	private PvService pvService;
	@EJB
	private PvValidatorsService pvValidatorsService;
	@EJB
	private EnseignantService enseignantService; 
	@EJB
	private NotificationService notificationService;
	
	@PostConstruct
	public void init() {
		if(this.pv == null) {
			if(!pvService.findByField("state", Status.NONVALID.name(), "").isEmpty()) {
				this.pv = pvService.findByField("state", Status.NONVALID.name(), "").get(0);
			}
		}
	}

	public PvsBean() {
	}

	public List<Pv> getPvs() {
		pvs = pvService.findAll("DATE desc");
		return pvs;
	}

	public Pv getPv() {
		return pv;
	}

	public String showPv(Pv pv) {
		this.pv = pv;

		return "showPV.xhtml?faces-redirect=true";
	}

	public void validatePv(Pv pv) {
		FacesMessage msg = new FacesMessage();
		Enseignant enseignant = enseignantService.findById(new Long(1));
		PvValidators pvValidators = new PvValidators(pv.getIntitule(), pv, enseignant,
				Calendar.getInstance().getTime(), pv.getContent());
//		Notification notification = new Notification("the PV " + pv.getId() + " is " + Status.MODIFIED.name().toLowerCase(),
//				Calendar.getInstance().getTime(), StatusNotification.UNREAD.name(), "orange",
//				"icon-folder-close-alt", pv, new ArrayList<Enseignant>());
		pv.setState(Status.MODIFIED.name());

		try {
			pvService.update(pv);
			pvValidatorsService.create(pvValidators);
//			notificationService.create(notification);
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Pv enregistrer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Pv enregistrement échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	@SuppressWarnings("deprecation")
	public int validityPV() {
		Pv pv = pvService.findByField("state", Status.NONVALID.name(), "").get(0);
		Date today = Calendar.getInstance().getTime(); 
		int pvDate = pv.getDate().getDate() + pv.getDate().getMonth()*30 + pv.getDate().getYear()*365 + pv.getPeriod();
		int dateValidity = today.getDate() + today.getMonth()*30 + today.getYear()*365;

		if((pvDate - dateValidity) > 0) {
			return (pvDate - dateValidity);
		} else {
			validatePv(pv);
			return -1;
		}
	}
}