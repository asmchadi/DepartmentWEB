package com.department.cd;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.department.ejb.AnneeService;
import com.department.ejb.BonusParticipantService;
import com.department.ejb.ChargeHoraireService;
import com.department.ejb.ChargeModuleService;
import com.department.entities.AnneeUniversitaire;
import com.department.entities.BonusParticipant;
import com.department.entities.ChargeHoraire;
import com.department.entities.ChargeModule;
import com.department.utils.Status;
import com.department.utils._TableNames;

@SessionScoped
@ManagedBean(name = "cd_cha")
public class ChargeBean {

	private ChargeHoraire cha = new ChargeHoraire();
	@EJB
	private ChargeHoraireService srvCHA;
	@EJB
	private ChargeModuleService srvCHM;
	@EJB
	private BonusParticipantService srvCHB;
	@EJB
	private AnneeService srvANN;
	
	public List<AnneeUniversitaire> getAnnees(){
		return srvANN.findAll("intitule");
	}
	
	private AnneeUniversitaire annee;

	public AnneeUniversitaire getAnnee() {
		return annee;
	}
	public void setAnnee(AnneeUniversitaire annee) {
		this.annee = annee;
	}

	private List<ChargeHoraire> chas;
	private Integer index;
	private String msg = "";
	
	public Integer countNV(){
		return getChas().size();
	}

//	private List<BonusParticipant> pfe; 
//	public List<BonusParticipant> getPfe() {
//		 return cha.getBonus().stream().filter(new Predicate<BonusParticipant>() {
//			@Override
//			public boolean test(BonusParticipant t) {
//				// TODO Auto-generated method stub
//				return t.getStatus() == Status.NONVALID;
//			}
//		}).map(null);
//	}
//	public void setPfe(List<BonusParticipant> pfe) {
//		this.pfe = pfe;
//	}
	
	@PostConstruct
	private void init() {
		// TODO Auto-generated method stub
		this.annee = new AnneeUniversitaire();
	}
	
	public String test() {
		System.out.println("message de test " + msg);
		FacesMessage msgg = new FacesMessage();
		msgg.setSeverity(FacesMessage.SEVERITY_INFO);
		msgg.setSummary("Operation reussite");
		FacesContext.getCurrentInstance().addMessage(null, msgg);
		return "";
	}

	public void send(String index, String type) {
		Long i = Long.parseLong(index);
		System.out.println("values: " + getMsg() + "-" + index + "-" + type);
		FacesMessage msgg = new FacesMessage();
		try {
			if (type.equals("module")){
				ChargeModule m = srvCHM.findById(i);
				m.setStatus(Status.REGECTED);
				m.setMessage(msg);
				srvCHM.update(m);
			}else{
				BonusParticipant b = srvCHB.findById(i);
				b.setStatus(Status.REGECTED);
				b.setMessage(msg);
				srvCHB.update(b);
			}
			cha.setStatus(Status.REGECTED);
			srvCHA.update(cha);
			msgg.setSeverity(FacesMessage.SEVERITY_INFO);
			msgg.setSummary("Operation reussite");
		} catch (Exception e) {
			e.printStackTrace();
			msgg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msgg.setSummary("Operation echouée");
			msgg.setDetail(e.getMessage());
		}
		setMsg("");
		FacesContext.getCurrentInstance().addMessage(null, msgg);
	}

	public List<ChargeHoraire> getChas() {
		// HashMap<String, Integer> params = new HashMap<>();
		// params.put("status", Status.MODIFIED.²);

		System.out.println("status in('" + (Status.MODIFIED.ordinal()) + "','"
				+ Status.PENDING + "')");
		this.chas = srvCHA.findByWhere(
				"status in('" + Status.MODIFIED.ordinal() + "','"
						+ Status.PENDING.ordinal() + "','"
								+ Status.REGECTED.ordinal() + "')", null);
		return chas;
	}

	public Double getTotal_bonus(boolean f) {
		Double result = 0.0;
		for (BonusParticipant b : cha.getBonus()) {
			if (f && b.getBonus().getIntitule().contains("PFE"))
				result += b.getBonus().getVolumeHoraire() * b.getNbSection();
			if (!f && !b.getBonus().getIntitule().contains("PFE"))
				result += b.getBonus().getVolumeHoraire() * b.getNbSection();
		}
		return result;
	}

	public Double getTotal() {
		return getTotal_bonus(true) + getTotal_bonus(false) + getTotal_modules();
	}

	public Double getTotal_modules() {
		Double result = 0.0;
		for (ChargeModule m : cha.getModules()) {
			result += m.getVhCour() * m.getNbCour() * 1.5 + m.getVhTD()
					* m.getNbTD() + m.getVhTP() * m.getNbTP() * 0.75;
		}
		return result;
	}

	public String show(String index) {
		this.index = Integer.parseInt(index);
		this.cha = chas.get(this.index);
		return "show.xhtml?faces-redirect=true";
	}

	public String save() {
		FacesMessage msg = new FacesMessage();
		try {
			System.out.println("values: " + this.getMsg() + "-"
					+ cha.getModules().get(0).getMessage());
			System.out.println("_____" + this.index + "---");
			ChargeHoraire ch = getChas().get(this.index);
			ch.setVhModule(cha.getVhModule());
			ch.setVhBonus(cha.getVhBonus());
			ch.setStatus(Status.VALID);
			srvCHA.update(ch);
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Operation échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "";
	}
	
	public String saveAn() {
		FacesMessage msg = new FacesMessage();
		try {
			srvANN.update(annee);
			if(annee.getIsCurrent()){
				srvANN.updateQuery("update AnneeUniversitaire set isCurrent=0 where intitule <> '" + annee.getIntitule()+"'");
			}
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Annee actualiser avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Operation échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "";
	}	

	public String showAn(int index) {
		this.annee = getAnnees().get(index);
		return "";
	}
	
	public String newAn() {
		this.annee = new AnneeUniversitaire();
		return "new.xhtml";
	}
	
	public String dropAn(int index) {
		FacesMessage msg = new FacesMessage();
		try {
			srvANN.delete(getAnnees().get(index));
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Annee supprimer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Operation échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "";
	}

	public ChargeHoraire getCha() {
		return cha;
	}

	public void setCha(ChargeHoraire cha) {
		this.cha = cha;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}
