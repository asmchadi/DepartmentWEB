package com.department.cd;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import com.department.ejb.DocumentService;
import com.department.entities.Document;

@ManagedBean(name = "cd_doc")
public class DocumentBean {
	@EJB
	private DocumentService docService;
	private List<Document> documents;
	private Document document;
	private UploadedFile file;

	public DocumentBean() {
		// TODO Auto-generated constructor stub
		this.document = new Document();
	}

	@PostConstruct
	private void init() {
		// TODO Auto-generated method stub
		documents = docService.findAll(null);
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public Document getDocument() {
		return document;
	}

	public String showDocument(String index) {
		System.out.println(index);
		this.document = documents.get(Integer.parseInt(index));
		System.out.println(document);
		return "show.xhtml";
	}

	public String newDocument() {
		this.document = new Document();
		return "show.xhtml";
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public DefaultStreamedContent streamContent(byte[] doce, String name,
			String type) throws Exception {
		ByteArrayInputStream array = new ByteArrayInputStream(doce);
		DefaultStreamedContent content = new DefaultStreamedContent(array, type);
		content.setName(name);
		return content;
	}

	public void saveDocument() {
		FacesMessage msg = new FacesMessage();
		try {
			if (!file.getFileName().trim().equals("")) {
				document.setFile_data(IOUtils.toByteArray(file.getInputstream()));
				document.setFile_name(file.getFileName());
				document.setFile_contentType(file.getContentType());
			}
			System.out.println(document.getFile_name());
			if (document.getId() == null)
				docService.create(document);
			else {
				Document d = docService.findById(document.getId());
				d.setIntitule(document.getIntitule());

				if (!file.getFileName().trim().equals("")) {
					d.setFile_data(document.getFile_data());
					d.setFile_contentType(document.getFile_contentType());
					d.setFile_name(document.getFile_name());
				}
				docService.update(d);
			}
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("Document enregistrer avec succée");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Document enregistrement échouée");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String dropDocument() {
		System.out.println("drop " + document.getId());
		try {
			docService.delete(document);// createQuery("delete from Document where id = "+document.getId()).executeUpdate();
			return "index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Document suppression échouée", e.getMessage()));
		}
		return "";
	}

}
