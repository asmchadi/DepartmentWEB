package com.department.en;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.department.ejb.FiliereService;
import com.department.entities.Filiere;

@ManagedBean
	@RequestScoped
	public class FiliereConverter implements Converter {

	    @EJB
	    private FiliereService filiereService;

	    @Override
	    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
	        if (submittedValue == null || submittedValue.isEmpty()) {
	            return null;
	        }

	        try {
	            return filiereService.findById(Long.valueOf(submittedValue));
	        } catch (NumberFormatException e) {
	            throw new ConverterException(new FacesMessage(String.format("%s is not a valid filiere ID", submittedValue)), e);
	        }
	    }

	    @Override
	    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
	        if (modelValue == null) {
	            return "";
	        }

	        if (modelValue instanceof Filiere) {
	            return String.valueOf(((Filiere) modelValue).getId());
	        } else {
	            throw new ConverterException(new FacesMessage(String.format("%s is not a valid Filiere", modelValue)), null);
	        }
	    }

	}