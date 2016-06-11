package com.department.en;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import com.department.ejb.FiliereService;
import com.department.ejb.ModuleService;
import com.department.entities.Enseignant;
import com.department.entities.Filiere;
import com.department.entities.Module;


@ManagedBean(name = "ens_module")
@SessionScoped
public class ModuleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private ModuleService moduleService;
	@EJB
	private FiliereService filiereService;
	
	private List<Module> modules;
	private String intitule;
	private Integer vhCour;
	private Integer vhTP;
	private Integer vhTD;
	private Integer semestre;
	private Long filiere;
	private Enseignant responsable;
	private List<Filiere> filieres;

     
    @ManagedProperty("#{carService}")
    private Module module;
     
    @PostConstruct
    public void init() {        modules = moduleService.findAll(null);       }
    public ModuleBean() {		}

	public List<Module> getModules() {        return modules;    }
    public void setModule(Module module) {        this.module = module;    }
	public Module getModule() {return module;	}	
	public String getIntitule() {return intitule;	}
	public void setIntitule(String intitule) {this.intitule = intitule;	}
	public Integer getVhCour() {return vhCour;	}
	public void setVhCour(Integer vhCour) {this.vhCour = vhCour;	}
	public Integer getVhTP() {return vhTP;	}
	public void setVhTP(Integer vhTP) {this.vhTP = vhTP;	}
	public Integer getVhTD() {return vhTD;	}
	public void setVhTD(Integer vhTD) {this.vhTD = vhTD;	}
	public Integer getSemestre() {return semestre;	}
	public void setSemestre(Integer semestre) {this.semestre = semestre;	}
	public Long getFiliere() {return filiere;	}
	public void setFiliere(Long filiere) {this.filiere = filiere;	}	
	public List<Filiere> getFilieres() {this.filieres = filiereService.findAll(null);	return filieres;	}
	
	public String addModule()
    {		
		
		Filiere filObj = filiereService.findById(filiere);
		module = new Module(intitule, filObj, this.responsable, semestre, vhCour, vhTP, vhTD);
		FacesMessage msg = new FacesMessage();
		try {
			moduleService.create(module);
			 msg = new FacesMessage("Module enregistrer avec succée",module.getIntitule());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Module enregistrement echoué");
		}        		     
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "index?faces-redirect=true";      
    }

	public String deleteModule(Module module) 
	{
		try { 
			moduleService.delete(module);			
			FacesMessage msg = new FacesMessage("Module supprimé",module.getIntitule());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Module suppression echouée", e.getMessage()));
		}
		return "index?faces-redirect=true";
	}
	  
    public void onRowEdit(RowEditEvent event) {
    	Module mo= (Module) event.getObject();
        System.out.println("event edit "+mo);
        try {
			moduleService.update(mo);
		} catch (Exception e) {
			e.printStackTrace();
		}
        FacesMessage msg = new FacesMessage("Module modifié",((Module) event.getObject()).getIntitule());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Modification Annulée",((Module) event.getObject()).getIntitule());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String list() {        
        return "index?faces-redirect=true";  
    } 
}