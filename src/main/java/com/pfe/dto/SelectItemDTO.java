package com.pfe.dto;

public class SelectItemDTO {
	

	    private String label;
	    private String value;

	    // Constructor
	    public SelectItemDTO(String label, String value) {
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

}

