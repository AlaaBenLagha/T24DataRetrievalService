package com.pfe.dto;

import java.util.List;


import com.pfe.model.T24Cheque;

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
public class T24chqData {
	
	
	private List<T24Cheque> record;

}
