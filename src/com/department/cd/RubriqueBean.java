package com.department.cd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.department.ejb.RubriqueService;
import com.department.entities.Rubrique;

@ManagedBean(name = "aa_rubrique")
@SessionScoped
public class RubriqueBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private RubriqueService rubService;
	private List<Rubrique> rubriques;
	private Rubrique rubrique;

	public List<Rubrique> getRubriques() {
		setRubriques(rubService.findByQuery("from Rubrique"));
		return rubriques;
	}

	public List<Rubrique> getParents() {
		return rubService.findByQuery("from Rubrique where id != "
				+ rubrique.getId());		 
	} 

	public ArrayList<Object> fun() {
		List<Rubrique> cours = rubService.findAll("parent_id");
		ArrayList<Object> lst = new ArrayList<Object>();
		ArrayList<Rubrique> lstCour = new ArrayList<Rubrique>();

		Rubrique autre = new Rubrique();
		autre.setId(-1l);
		autre.setIntitule("Autre");		
		
		Long flag =  -1l;
		for(int i=0;i<cours.size();i++){
			System.out.println(cours.get(i).getIntitule());
			if(cours.get(i).getParent()==null){				
				cours.get(i).setParent(autre);  
			}
			if(i+1 < cours.size() && cours.get(i+1).getParent()==null){				
				cours.get(i+1).setParent(autre);  
			}
			lstCour.add(cours.get(i));
			if(i+1 < cours.size() && cours.get(i+1).getParent().getId() != flag){
				System.out.println(cours.get(i).getParent());
				flag = cours.get(i+1).getParent().getId();
				ArrayList<Object> lstModule = new ArrayList<Object>();
				lstModule.add(0,cours.get(i).getParent());
				lstModule.add(1,lstCour);
				lst.add(lstModule);
				lstCour = new ArrayList<Rubrique>();
			}else if(i+1==cours.size()){
				ArrayList<Object> lstModule = new ArrayList<Object>();
				lstModule.add(0,cours.get(i).getParent());
				lstModule.add(1,lstCour);
				lst.add(lstModule);
			}
		}
		return lst;
	}

	
	
	public void setRubriques(List<Rubrique> rubriques) {
		this.rubriques = rubriques;
	}

	public Rubrique getRubrique() {
		return rubrique;
	}

	public void setRubrique(Rubrique rubrique) {
		this.rubrique = rubrique;
	}

	public String save() throws Exception {
		Rubrique rub = new Rubrique();
		if (rubrique.getId() != null) {
			rub = rubService.findById(rubrique.getId());
		}
		rub.setHtmlContent(rubrique.getHtmlContent());
		rub.setIntitule(rubrique.getIntitule());
		rub.setIsExtern(rubrique.getIsExtern());
		rub.setIsVisible(rubrique.getIsVisible());
		rub.setUrl(rubrique.getUrl());
		rub.setMenu(rubrique.getMenu());
		rub.setPosition(rubrique.getPosition());
		rub.setParent(rubService.findById(rubrique.getParent().getId()));
		if (rubrique.getId() != null) {
			rubService.update(rub);
		} else {
			rubService.create(rub);
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Operation effecutuer", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "";
	}

	public String display(Long id) {
		this.rubrique = rubService.findById(id);
		if (rubrique.getParent()==null)
			rubrique.setParent(new Rubrique());
		return "show.xhtml";
	}

	public String show() {
		this.rubrique = new Rubrique();
		this.rubrique.setParent(new Rubrique());
		return "show.xhtml";
	}

	public String delete(Long id) throws Exception {
		//Rubrique r = rubService.findById(id);
		Rubrique rub = new Rubrique();
		rub.setId(id);
		rubService.delete(rub);
		return "index";
	}
}
