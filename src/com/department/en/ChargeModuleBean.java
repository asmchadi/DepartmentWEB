package com.department.en;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import com.department.ejb.*;
import com.department.entities.*;
import com.department.utils.Status;


@ManagedBean(name="chargeModule")
public class ChargeModuleBean  {
	
	
	
	
	@EJB
	ModuleService srvModule;
	@EJB
	ChargeHoraireService srvChargeHoraire;
	@EJB
	ChargeModuleService srvChargeModule;
	
	
	private ChargeModule chargeModule;
	private List<Module> modules;

	
	
	public ChargeModuleBean(){
		
		chargeModule = new ChargeModule();		
		
	}
	
	
	@PostConstruct
	public void init(){
		modules = srvModule.findAll("id");
	}
	
	
	public String add(){
		
		ChargeHoraire chargeHoraire = srvChargeHoraire.getCurrentChargeHoraire();
		
		chargeModule.setChargeHoraire(chargeHoraire);

		chargeModule.setStatus(Status.PENDING);
		
		try {
			srvChargeModule.create(chargeModule);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return "index.xhtml?faces-redirect=true";
	}
	

	
	
	
	public String getBonus(){
		return "rgrte";
	}



	public ChargeModule getCharge() {
		return chargeModule;
	}



	public void setCharge(ChargeModule charge) {
		this.chargeModule = charge;
	}



	public List<Module> getModules() {
		return modules;
	}



	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	

}
