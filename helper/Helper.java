package com.application.backend.helper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import com.application.backend.exception.CatchException;
import com.paypal.base.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public class Helper {
	
	// return response
	public static ResponseEntity<Object> res(Object data, HttpStatus status){
		return new ResponseEntity<Object>(data,status);
	}
	
	// get day dd/MM/yyy
	public static String getDate(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return  date.format(formatter);
	}

	public static String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	// from dd/MM/yyyy to Date
	public static Date getDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new CatchException("Some thing wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// generate random number
	public static int createNumber(int digit) {
        Random random = new Random();
        return random.nextInt(digit);
	}

	// convert price
	public static String convertPrice (Long price){
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		return decimalFormat.format(price) + " VND";
	}
	
	// generate verify code
	public static String createVerifyCode() {
		String verifyCode = "";
		for (int i = 0; i < 7 ; ++i) {
			verifyCode += String.valueOf(Helper.createNumber(10)) ;
		}
		return verifyCode ;
	}

	// get time unit value
	public static Integer getTimeUnitValue(String unit) {
		switch (unit) {
			case "days":
				return 1;
			case "weeks":
				return 7;
			case "months":
				return 30;
			case "years":
				return 365;
		}
		return 0;
	}

}
