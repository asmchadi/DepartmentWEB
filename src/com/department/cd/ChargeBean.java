package com.department.cd;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.department.ejb.BonusParticipantService;
import com.department.ejb.ChargeHoraireService;
import com.department.ejb.ChargeModuleService;
import com.department.entities.BonusParticipant;
import com.department.entities.ChargeHoraire;
import com.department.entities.ChargeModule;
import com.department.utils.Status;

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
	// @EJB
	// private AnneeService srvANN;
	// private AnneeUniversitaire annee;
	private List<ChargeHoraire> chas;
	private Integer index;
	private String msg = "";

	public String test() {
		System.out.println("message de test " + msg);
		FacesMessage msgg = new FacesMessage();
		msgg.setSeverity(FacesMessage.SEVERITY_INFO);
		msgg.setSummary("Operation reussite");
		FacesContext.getCurrentInstance().addMessage(null, msgg);
		return "";
	}

	public void send(String index, String type) {
		Integer i = Integer.parseInt(index);
		System.out.println("values: " + getMsg() + "-" + index + "-" + type);
		FacesMessage msgg = new FacesMessage();
		try {
			if (type.equals("module")){
				ChargeModule m = cha.getModules().get(i);
				m.setStatus(Status.REGECTED);
				m.setMessage(msg);
				srvCHM.update(m);
			}else{
				BonusParticipant b = cha.getBonus().get(i);
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

	public ChargeHoraire getCha() {
		return cha;
	}

	public void setCha(ChargeHoraire cha) {
		this.cha = cha;
	}

	public Double getTotal_bonus() {
		Double result = 0.0;
		for (BonusParticipant b : cha.getBonus()) {
			result += b.getBonus().getVolumeHoraire() * b.getNbSection();
		}
		return result;
	}

	public Double getTotal() {
		return getTotal_bonus() + getTotal_modules();
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
			msg.setSummary("Pv publication échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "";
	}

}
