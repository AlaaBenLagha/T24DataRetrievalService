package com.pfe.model;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name ="T24CHK")
public class T24Cheque{
	

	@Id
	private String id;
	private String codeVal;
	private String codeRemettant;
	private String dateOperation;
	private Float mntCheque;
	private String mntReclame;
	private String mntRegulInt;
	private String agence;
	private String ribBenef;
	private String nomBenef;
	private String dateEmission;
	private String situationBenef;
	private String natureCmpt;
	private String certifie;
	private Integer statut;
	private String inexploitableString;
	private boolean[] inexpoitableVerrou = new boolean[4];
	private String viseDeformeString;
	private boolean[] visDeFormeVerrou = new boolean[4];
	private String opposition;
	private String cloture;
	private Integer[] visDeForme = new Integer[4];
	private Integer[] inexpoitable = new Integer[4];
	@OneToMany(mappedBy = "t24cheque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectItem> viceDeFormeSelectedItems = new ArrayList<>();
	@OneToMany(mappedBy = "t24cheque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectItem> inexploitabeleSelectedItems = new ArrayList<>();
	private String pathSignature;
	private String dateImgNew;
	private String ftDesionPai;
	private String valConsultee;
	private String checkSignatureReference;
	private String dateImg;
	private String numCpt;
	private String ribTireur;
	private String numChq;
	private boolean notifChq;
	private String CMC7 = "";
	
	@ElementCollection
	private List<String> refSignature = new ArrayList<String>();
	
	private boolean viewed = false;
	private Boolean isSelected =false;
	
	
	
	
	
	
	
		public void setCloture(String cloture) {
			this.cloture = cloture;
			updateNotifChq();
		}
	
		private void updateNotifChq() {
			if ((this.certifie != null && this.certifie.equals("YES")) || 
				(this.cloture != null && !this.cloture.equals("")) || 
				(this.opposition != null && !this.opposition.equals(""))) {
					notifChq = true;
			} else {
					notifChq = false;
			}
		}
	
	

	    
	    
		
		
		public void setOpposition(String opposition) {
			this.opposition = opposition;
			updateNotifChq();
		}
		
		
		public void setCertifie(String certifie) {
			this.certifie = certifie;
			updateNotifChq();
		}
		
		//getters and setters

	
	
	
	
	
	
//	public T24Cheque(RejetChq rejetChq2) {
//		super();
//		Id = rejetChq2.getId();
//		this.mntCheque = rejetChq2.getMntCheque();
//		this.ribBenef = rejetChq2.getRibBenef();
//		// this.signataire = rejetChq2.getSignataire();
//		this.numCpt = rejetChq2.getNumCpt();
//	}
	
		
		
	
	public boolean isNotifChq() {
		return notifChq;
	}

	public String getCertifie() {
		return certifie;
	}



	public String getOpposition() {
		return opposition;
	}

	

	public String getCloture() {
		return cloture;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getCodeVal() {
		return codeVal;
	}

	public void setCodeVal(String codeVal) {
		this.codeVal = codeVal;
	}


	public String getCodeRemettant() {
		return codeRemettant;
	}


	public void setCodeRemettant(String codeRemettant) {
		this.codeRemettant = codeRemettant;
	}


	public String getDateOperation() {
		return dateOperation;
	}


	public void setDateOperation(String dateOperation) {
		this.dateOperation = dateOperation;
	}

	public Float getMntCheque() {
		return mntCheque;
	}

	public void setMntCheque(Float mntCheque) {
		this.mntCheque = mntCheque;
	}

	public String getMntReclame() {
		return mntReclame;
	}

	public void setMntReclame(String mntReclame) {
		this.mntReclame = mntReclame;
	}

	public String getMntRegulInt() {
		return mntRegulInt;
	}

	public void setMntRegulInt(String mntRegulInt) {
		this.mntRegulInt = mntRegulInt;
	}

	public String getAgence() {
		return agence;
	}

	public void setAgence(String agence) {
		this.agence = agence;
	}

	public String getRibBenef() {
		return ribBenef;
	}

	public void setRibBenef(String ribBenef) {
		this.ribBenef = ribBenef;
	}

	public String getNomBenef() {
		return nomBenef;
	}

	public void setNomBenef(String nomBenef) {
		this.nomBenef = nomBenef;
	}


	public String getDateEmission() {
		return dateEmission;
	}


	public void setDateEmission(String dateEmission) {
		this.dateEmission = dateEmission;
	}


	public String getSituationBenef() {
		return situationBenef;
	}


	public void setSituationBenef(String situationBenef) {
		this.situationBenef = situationBenef;
	}


	public String getNatureCmpt() {
		return natureCmpt;
	}

	public void setNatureCmpt(String natureCmpt) {
		this.natureCmpt = natureCmpt;
	}

	public Integer getStatut() {
		return statut;
	}

	public void setStatut(Integer statut) {
		this.statut = statut;
	}

	public String getInexploitableString() {
		return inexploitableString;
	}

	public void setInexploitableString(String inexploitableString) {
		this.inexploitableString = inexploitableString;
	}

	public Integer[] getVisDeForme() {
		return visDeForme;
	}


	public void setVisDeForme(Integer[] visDeForme) {
		this.visDeForme = visDeForme;
	}
	
	public String getFtDesionPai() {
		return ftDesionPai;
	}
	
	public void setFtDesionPai(String ftDesionPai) {
		this.ftDesionPai = ftDesionPai;
	}

	public String getValConsultee() {
		return valConsultee;
	}


	public void setValConsultee(String valConsultee) {
		this.valConsultee = valConsultee;
	}

	public String getDateImg() {
		return dateImg;
	}


	public void setDateImg(String dateImg) {
		this.dateImg = dateImg;	}





	public void setNumCpt(String numCpt) {
		this.numCpt = numCpt;
	}




	public void setRibTireur(String ribTireur) {
		this.ribTireur = ribTireur;
	}

	

	public Integer[] getInexpoitable() {
		return inexpoitable;
	}
    
    
	public void setInexploitable(Integer[] inexploitable) {
	    this.inexpoitable = inexploitable;
	}

	public boolean[] getInexpoitableVerrou() {
		return inexpoitableVerrou;
	}

	public void setInexpoitableVerrou(boolean[] inexpoitableVerrou) {
		this.inexpoitableVerrou = inexpoitableVerrou;
	}

	public String getViseDeformeString() {
		return viseDeformeString;
	}

	public void setViseDeformeString(String viseDeformeString) {
		this.viseDeformeString = viseDeformeString;
	}

	public boolean[] getVisDeFormeVerrou() {
		return visDeFormeVerrou;
	}

	public void setVisDeFormeVerrou(boolean[] visDeFormeVerrou) {
		this.visDeFormeVerrou = visDeFormeVerrou;
	}

	public List<SelectItem> getViceDeFormeSelectedItems() {
		return viceDeFormeSelectedItems;
	}

	public void setViceDeFormeSelectedItems(List<SelectItem> viceDeFormeSelectedItems) {
		this.viceDeFormeSelectedItems = viceDeFormeSelectedItems;
	}

	public List<SelectItem> getInexploitabeleSelectedItems() {
		return inexploitabeleSelectedItems;
	}

	public void setInexploitabeleSelectedItems(List<SelectItem> inexploitabeleSelectedItems) {
		this.inexploitabeleSelectedItems = inexploitabeleSelectedItems;
	}

	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	public void setInexpoitable(Integer[] inexpoitable) {
		this.inexpoitable = inexpoitable;
	}



	public void setNotifChq(boolean notifChq) {
		this.notifChq = notifChq;
	}


	

	public void setNumChq(String numChq) {
		this.numChq = numChq;
	}

	public String getDateImgNewTC() {
	    if (this.dateOperation != null && !this.dateOperation.isEmpty()) {
	        String[] splitDate = this.dateOperation.split("-");
	        if(splitDate.length == 3){
	            if(splitDate[0].length() == 4 && splitDate[1].length() == 2 && splitDate[2].length() == 2){
	                DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	                DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
	                LocalDate date = LocalDate.parse(this.dateOperation, oldFormat);
	                return date.format(newFormat);
	            }else{
	                throw new IllegalArgumentException("dateOperation does not match the expected format yyyy-MM-dd. It is: " + this.dateOperation);
	            }
	        }else{
	            throw new IllegalArgumentException("dateOperation does not match the expected format yyyy-MM-dd. It is: " + this.dateOperation);
	        }
	    }
	    return null;
	}

	

	
	public void setPathSignature(String pathSignature) {
		this.pathSignature = pathSignature;
	}

    public void setDateImgNew(String dateImgNew) {
        this.dateImgNew = dateImgNew;
    }

   
	    
    public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public List<String> getRefSignature() {
		return refSignature;
	}

	public void setRefSignature(List<String> refSignature) {
		this.refSignature = refSignature;
	}


	public void setCMC7(String cMC7) {
		CMC7 = cMC7;
	}



	public void setCheckSignatureReference(String checkSignatureReference) {
		this.checkSignatureReference = checkSignatureReference;
	}

	public String getPathSignature() {
		return pathSignature;
	}

	public String getNumCpt() {
		return numCpt;
	}

	public String getRibTireur() {
		return ribTireur;
	}

	public String getNumChq() {
		return numChq;
	}

	public String getCMC7() {
		return CMC7;
	}

	public String getCheckSignatureReference() {
		return checkSignatureReference;
	}

	public String getDateImgNew() {
		return dateImgNew;
	}

	public T24Cheque(String id, Integer statut) {
		super();
		this.id = id;
		this.statut = statut;
	}
	
	
	
	

	
	

    
    
}

	




	



	
	


	
	
	


	