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
public class ImageResponse {
	

	private String status;


	private ImageData data;

	private String errorCode;


	private String errorMessage;
	
	
	

}
