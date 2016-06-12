package com.department.cd;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.department.ejb.BonusService;
import com.department.entities.Bonus;

@ManagedBean(name = "cd_bonus")
public class BonusBean {

	@EJB
	private BonusService bonusService;
	private List<Bonus> bonuss;
	private Bonus bonus;

	public BonusBean() {
		// TODO Auto-generated constructor stub
		this.bonus = new Bonus();
	}

	@PostConstruct
	private void init() {
		// TODO Auto-generated method stub
		bonuss = bonusService.findAll(null);
	}

	public List<Bonus> getBonuss() {
		return bonuss;
	}

	public Bonus getBonus() {
		return bonus;
	}

	public String showBonus(String index) {
		System.out.println(index);
		this.bonus = bonuss.get(Integer.parseInt(index));
		System.out.println(bonus);
		return "show.xhtml";
	}

	public String newBonus() {
		this.bonus = new Bonus();
		return "show.xhtml";
	}

	public void saveBonus() {
		FacesMessage msg = new FacesMessage();
		try {
			if (bonus.getId() == null)
				bonusService.create(bonus);
			else {
				Bonus d = bonusService.findById(bonus.getId());
				d.setIntitule(bonus.getIntitule());
				d.setVolumeHoraire(bonus.getVolumeHoraire());
				bonusService.update(d);
			}
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Bonus enregistrer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Bonus enregistrement échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String dropBonus() {
		System.out.println("drop " + bonus.getId());
		try {
			bonusService.delete(bonus);// createQuery("delete from Bonus where id = "+bonus.getId()).executeUpdate();
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Bonus suppression échouée", e.getMessage()));
		}
		return "";
	}

}
