package com.application.backend.configuration.jwt;

import java.security.Key;
import java.util.Date;

import com.application.backend.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.application.backend.exception.CatchException;
import com.application.backend.userdetails.CustomUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;


@Component
public class JwtUtils {

  private final String jwtSecret = System.getenv("SECRET_TOKEN");

  private final int jwtExpiration = Integer.parseInt(System.getenv("TOKEN_EXPIRE"));
  
  private static Key key;
  
  @PostConstruct
  public void init() {
	  key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
  
  //Tạo ra token từ chuỗi authentication
  public String createToken(Authentication authentication) {

	  CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getEmail()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
        .signWith(key)
        .compact();
  }

    public String createOAuthToken(Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key)
                .compact();
    }
  

  //Lấy email từ token đã được mã hóa
  public String getUserEmailFromJwtToken(String token) {
	  return Jwts.parserBuilder()
	    		.setSigningKey(key)
	    		.build()
	            .parseClaimsJws(token)
	            .getBody().getSubject();
	}

  public boolean validateJwtToken(String authToken){
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
    	throw new CatchException(e.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (ExpiredJwtException e) {
    	throw new CatchException(e.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (UnsupportedJwtException e) {
    	throw new CatchException(e.getMessage(), HttpStatus.UNAUTHORIZED);
    } catch (IllegalArgumentException e) {
    	throw new CatchException(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }
}
