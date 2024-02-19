package com.application.backend.controller;

import com.application.backend.exception.NoPermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.application.backend.email.EmailHandler;
import com.application.backend.exception.CatchException;
import com.application.backend.exception.NotVerifiedException;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.mail.MessagingException;

@ControllerAdvice
public class HandleExceptionController {
	
	@Autowired
	private EmailHandler emailHandler;
	
	@ExceptionHandler(CatchException.class)
	public ResponseEntity<String> sendResponse(CatchException ex) {
		return new ResponseEntity<String>(ex.getMessage(),ex.getStatus());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> runTime(RuntimeException ex) {
		return new ResponseEntity<String>("Some thing wrong! Try again.",HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> serverError(BadCredentialsException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<String> messException(MessagingException ex) {
		
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotVerifiedException.class)
	public ResponseEntity<Object> notVerifiedException(NotVerifiedException ex)  {

		// create a thread to send email so that it will not block the process
		new Thread(new Runnable() {
		    public void run() {
		    	try {
					emailHandler.sendActiveEmail(ex.getEmail());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}).start();

		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(PayPalRESTException.class)
	public ResponseEntity<Object> paypalFail(PayPalRESTException ex)  {
		return new ResponseEntity<Object>("Transaction fail!", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoPermissionException.class)
	public ResponseEntity<Object>noPermission(NoPermissionException ex)  {
		return new ResponseEntity<Object>("You are not allowed!", HttpStatus.UNAUTHORIZED);
	}
	
}
