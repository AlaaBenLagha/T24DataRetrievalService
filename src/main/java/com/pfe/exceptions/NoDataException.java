package com.pfe.exceptions;

public class NoDataException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoDataException(String errorMessage) {
        super(errorMessage);
    }
	
	public NoDataException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
	
	

}
