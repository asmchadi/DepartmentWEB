package com.department.en;

import java.io.ByteArrayInputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import com.department.ejb.EnseignantService;
import com.department.entities.Enseignant;

@ManagedBean(name = "enseignantBean")
public class EnseignantBean {

	@EJB
	private EnseignantService srvEnseignant;

	private Enseignant enseignant;
	private UploadedFile file;
	private UploadedFile photo;

	public EnseignantBean() {

	}

	@PostConstruct
	private void init() {
		enseignant = srvEnseignant.findFirst();
		if (enseignant == null) {
			enseignant = new Enseignant();
			enseignant.setNom("Enseignant 1");

			try {
				srvEnseignant.create(enseignant);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String update() {
		try {
			if (file != null) {
				enseignant
						.setCvData(IOUtils.toByteArray(file.getInputstream()));
				enseignant.setCvName(file.getFileName());
				enseignant.setCvContentType(file.getContentType());
			}
			if (file != null) {
				enseignant.setPhotoData(IOUtils.toByteArray(photo
						.getInputstream()));
				enseignant.setPhotoName(photo.getFileName());
				enseignant.setPhotoContentType(photo.getContentType());
			}
			System.out.println(enseignant.getCvName());
			srvEnseignant.update(enseignant);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public DefaultStreamedContent streamCV() throws Exception {
		return streamContent(enseignant.getCvData(),enseignant.getCvName(), enseignant.getCvContentType());
	}

	public DefaultStreamedContent streamPhoto() throws Exception {
		return streamContent(enseignant.getPhotoData(),enseignant.getPhotoName(), enseignant.getPhotoContentType());
	}

	public DefaultStreamedContent streamContent(byte[] file, String name,
			String type) throws Exception {
		if (file != null) {
			ByteArrayInputStream array = new ByteArrayInputStream(file);
			DefaultStreamedContent content = new DefaultStreamedContent(array,
					type);
			content.setName(name);
			return content;
		}
		return new DefaultStreamedContent();

	}

	public Enseignant getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}

	public String p() {
		return "My prop";
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public UploadedFile getPhoto() {
		return photo;
	}

	public void setPhoto(UploadedFile photo) {
		this.photo = photo;
	}
}
