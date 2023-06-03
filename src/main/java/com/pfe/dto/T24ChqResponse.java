package com.pfe.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class T24ChqResponse {
	
	
	private String status;
	
	
	private T24chqData data;
	
	

	private String errorCode;

	
	private String errorMessage;

}
