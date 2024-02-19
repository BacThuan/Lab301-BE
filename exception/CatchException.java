package com.application.backend.exception;

import org.springframework.http.HttpStatus;

public class CatchException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private HttpStatus status;
	public CatchException(String mess, HttpStatus status) {
		super(mess);	
		this.status = status;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	

}
