package com.pfe.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class SelectItem {
	
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
			
	

	    private String label;
	    private String value;
	    
	    // Constructor
	    public SelectItem(String label, String value) {
	        this.label = label;
	        this.value = value;
	    }
	    
	    



	    // Getters and setters
	    public String getLabel() {
	        return label;
	    }

	    public void setLabel(String label) {
	        this.label = label;
	    }

	    public String getValue() {
	        return value;
	    }

	    public void setValue(String value) {
	        this.value = value;
	    }
	    
	    
	    @ManyToOne
	    @JoinColumn(name = "t24cheque_id")
	    private T24Cheque t24cheque;
	    
	
	
	    @JsonIgnore
	    public void setT24Cheque(T24Cheque t24Cheque) {
	        this.t24cheque = t24Cheque;
	    }

	    @JsonIgnore
	    public T24Cheque getT24Cheque() {
	        return t24cheque;
	    }

}

