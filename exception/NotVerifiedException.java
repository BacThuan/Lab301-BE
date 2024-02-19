package com.application.backend.exception;

public class NotVerifiedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String email;
	public NotVerifiedException(String mess, String email) {
		super(mess);	
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

}
