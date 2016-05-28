package com.department.cd;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import com.department.ejb.FiliereService;
import com.department.ejb.RubriqueService;
import com.department.entities.Filiere;
import com.department.entities.Rubrique;

@ManagedBean(name = "cd_filieres")
public class FilieresBean {
	private List<Filiere> filieres;

	public FilieresBean() {
		// TODO Auto-generated constructor stub
		this.filiere = new Filiere(); 
	}

	@EJB
	private RubriqueService rubService;
	@EJB
	private FiliereService filService;
	
	private Rubrique rubrique = new Rubrique();	  
   
	public Rubrique getRubrique() {
		return rubrique;
	}


	public void setRubrique(Rubrique rubrique) {
		this.rubrique = rubrique;
	} 
	
	public void testEditor() throws Exception{
		rubService.create(rubrique);
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				 "ajouter",null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	} 
	 
	public List<Filiere> getFilieres() {
		filieres = filService.findAll(null);
		return filieres;
	}

	public Filiere getFiliere() {
		return filiere;
	}

	public String showFiliere(String index) {
		System.out.println(index);
		this.filiere = filieres.get(Integer.parseInt(index));
		System.out.println(filiere);
		return "show.xhtml";
	}

	public String newFiliere() {
		this.filiere = new Filiere();
		return "show.xhtml";
	}

	@EJB
	private FiliereService filiereService;
	private Filiere filiere;
	private UploadedFile file;
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
	
	public void saveFiliere() {
		FacesMessage msg = new FacesMessage();
		try {
			if (file != null) {
				filiere.setFile_data(IOUtils.toByteArray(file.getInputstream()));
				filiere.setFile_name(file.getFileName());
				filiere.setFile_contentType(file.getContentType()); 
			}
			System.out.println(filiere.getFile_name());
			filiereService.create(filiere);
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Filiere enregistrer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Filiere enregistrement échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String dropFiliere() {
		System.out.println("drop " + filiere.getId());
		try { 
			filService.delete(filiere);// createQuery("delete from Filiere where id = "+filiere.getId()).executeUpdate();
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Filiere suppression échouée", e.getMessage()));
		}
		return "";
	}

	
}
