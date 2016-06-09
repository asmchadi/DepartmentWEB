package com.department.en;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import com.department.ejb.*;
import com.department.entities.*;
import com.department.utils.*;


@ManagedBean(name="bonusParticipant")
public class BonusParticipantBean {
	
	@EJB
	private BonusService srvBonus;
	@EJB
	ChargeHoraireService srvChargeHoraire;
	@EJB
	BonusParticipantService srvBonusParticipant;
	
	
	private BonusParticipant participant;
	private List<Bonus> listBonus;
	private Long bonus;
	
	
	public BonusParticipantBean(){
		participant = new BonusParticipant();
	}
	
	@PostConstruct
	public void init(){
		listBonus = srvBonus.findByWhere("intitule not like '%PFE%'", "id");
	}
	
	
	public String addPFE(){
		ChargeHoraire ch = srvChargeHoraire.findById(new Long(1));
		participant.setChargeHoraire(ch);
		participant.setBonus(srvBonus.getPFEBonus());
		participant.setStatus(Status.PENDING);
		
		try {
			srvBonusParticipant.create(participant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index.xhtml?faces-redirect=true";
	}
	
	public String addBonus(){
		ChargeHoraire ch = srvChargeHoraire.findById(new Long(1));
		participant.setStatus(Status.PENDING);
	
		participant.setChargeHoraire(ch);
		participant.setBonus(srvBonus.findById(bonus));
		
		try {
			srvBonusParticipant.create(participant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index.xhtml?faces-redirect=true";
	}
	
	

	public List<Bonus> getListBonus() {
		return listBonus;
	}

	public void setListBonus(List<Bonus> listBonus) {
		this.listBonus = listBonus;
	}

	public Long getBonus() {
		return bonus;
	}

	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}

	public BonusParticipant getParticipant() {
		return participant;
	}

	public void setParticipant(BonusParticipant participant) {
		this.participant = participant;
	}

}
