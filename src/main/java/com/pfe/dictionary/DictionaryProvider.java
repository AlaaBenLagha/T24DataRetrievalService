package com.pfe.dictionary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pfe.dto.SelectItemDTO;

public class DictionaryProvider {
	
	
	
	
	private static final Map<String, String> visDeForme = new HashMap<String, String>() {{
        put("17", "Montant mal saisi");
        put("23", "Manque endos");
        put("30", "Signature manquante");
        put("32", "Mentions obligatoires manquantes");
        put("33", "Signtatures non conforme");
        put("36", "Valeur mal presentee");
        put("37", "Endossement irregulier");
    }};

    private static final Map<String, String> inexploitable = new HashMap<String, String>() {{
        put("14", "Cheque inferieur 20 DINARS");
        put("15", "Prob de prov sur un cheque sur soi mm");
        put("20", "Titulaire du compte");
        put("21", "Donnes mal saisies");
        put("22", "Manque empreinte scanner/Cachet BQ");
        put("25", "Image non exploitable");
        put("31", "Valeur prescrite");
        put("35", "Absence de signature");
        put("38", "Valeur contrefaite");
        put("39", "Valeur mal acheminee");
        put("43", "Valeur deja reglee");
        put("44", "Valeur deja rejeter");
    }};

    public static List<SelectItemDTO> getViseDeFormeDictionary(String code) {
        return Arrays.stream(code.split(" "))
                .map(codeKey -> new SelectItemDTO(codeKey, visDeForme.get(codeKey)))
                .collect(Collectors.toList());
    }

    public static List<SelectItemDTO> getInexploitableDictionary(String code) {
        return Arrays.stream(code.split(" "))
                .map(codeKey -> new SelectItemDTO(codeKey, inexploitable.get(codeKey)))
                .collect(Collectors.toList());
    }
    
    public static List<SelectItemDTO> getViseDeFormeDictionary() {
        return visDeForme.entrySet().stream()
                .map(entry -> new SelectItemDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static List<SelectItemDTO> getInexploitableDictionary() {
        return inexploitable.entrySet().stream()
                .map(entry -> new SelectItemDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}

