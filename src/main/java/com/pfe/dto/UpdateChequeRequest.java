package com.pfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChequeRequest {
	
	private String id;
    private Integer[] visDeForme;
    private Integer[] inexpoitable;

}
