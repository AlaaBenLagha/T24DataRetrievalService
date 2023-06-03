package com.pfe.modifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Entry {
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
	private String opposition;
	private String cloture;
	private Integer[] visDeForme = new Integer[4];
	private String ftDesionPai;
	private String valConsultee;
	private String dateImg;
	private String numCpt;
	private String ribTireur;

   

    
}
