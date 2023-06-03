package com.pfe.exceptions;

public class NoDataException extends Exception {
	
	
	public NoDataException(String errorMessage) {
        super(errorMessage);
    }
	
	public NoDataException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
	
	

}
