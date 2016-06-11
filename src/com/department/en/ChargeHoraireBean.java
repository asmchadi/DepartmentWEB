package com.department.en;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.DefaultStreamedContent;

import com.department.ejb.*;
import com.department.entities.*;
import com.department.utils.*;

@ManagedBean(name = "chargeHoraire")
public class ChargeHoraireBean {

	@EJB
	ChargeHoraireService srvChargeHoraire;
	@EJB
	ChargeModuleService srvChargeModule;
	@EJB
	AnneeUniversitaireService srvAnneeUniversitaire;
	@EJB
	BonusParticipantService srvBonusParticipant;
	@EJB
	BonusService srvBonus;

	ChargeHoraire charge;
	List<ChargeModule> modules;
	List<BonusParticipant> listBonus;
	List<BonusParticipant> listPFE;
	Status status;
	Boolean needsModifications;

	public ChargeHoraireBean() {
		
	}

	@PostConstruct
	public void init() {
		
		charge  = srvChargeHoraire.getCurrentChargeHoraire();
		
		if (charge == null) {
			charge = new ChargeHoraire();
			AnneeUniversitaire annee = new AnneeUniversitaire("2015/2016");
			
			charge.setAnneeUniversitaire(annee.getIntitule());
			charge.setStatus(Status.PENDING);

			try {
				srvChargeHoraire.create(charge);
				if (srvAnneeUniversitaire.findById("2015/2016") != null) {
					srvAnneeUniversitaire.create(annee);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.modules = srvChargeModule.findAll("id");
		
		this.listBonus = srvBonusParticipant.findByWhere(
				"CHARGEHORAIRE_ID = 1 and bonus_id not like '"
						+ srvBonus.getPFEBonusId() + "'", "id");

		this.listPFE = srvBonusParticipant.findByWhere(
				"CHARGEHORAIRE_ID = 1 and bonus_id ="
						+ srvBonus.getPFEBonusId(), "id");


	}

	public String deleteChargeModule(Long id) {

		try {
			srvChargeModule.delete(srvChargeModule.findById(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		return "index.xhtml?faces-redirect=true";
	}

	public String deleteBonus(Long id) {
		try {
			srvBonusParticipant.delete(srvBonusParticipant.findById(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		return "index.xhtml?faces-redirect=true";
	}

	public String total() {

		Integer total = 0;

		for (ChargeModule charge : modules) {
			total += charge.getVhCour() * charge.getNbCour();
			total += charge.getVhTD() * charge.getNbTD();
			total += charge.getVhTP() * charge.getNbTP();
		}

		for (BonusParticipant bonus : listBonus) {
			total += bonus.getBonus().getVolumeHoraire();
		}

//		for (BonusParticipant bonus : listPFE) {
//			total += bonus.getVolumeHoraire() * bonus.getNbSection();
//		}

		return String.valueOf(total);
	}
	
	
	
	
	public Status getBonusStatus(Long id){
		return srvBonusParticipant.findById(id).getStatus();
	}

	public ChargeHoraire getCharge() {
		return charge;
	}

	public void setCharge(ChargeHoraire charge) {
		this.charge = charge;
	}

	public List<ChargeModule> getModules() {
		return modules;
	}

	public void setModules(List<ChargeModule> modules) {
		this.modules = modules;
	}

	public List<BonusParticipant> getListBonus() {
		return listBonus;
	}

	public void setListBonus(List<BonusParticipant> listBonus) {
		this.listBonus = listBonus;
	}

	public List<BonusParticipant> getListPFE() {
		return listPFE;
	}

	public void setListPFE(List<BonusParticipant> listPFE) {
		this.listPFE = listPFE;
	}

	public Status getStatus() {
		return srvChargeHoraire.getCurrentChargeHoraire().getStatus();
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean getNeedsModifications() {
		if (status == Status.REGECTED) {
			return true;
		}
		return false;

	}

}
