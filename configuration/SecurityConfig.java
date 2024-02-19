package com.application.backend.configuration;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.application.backend.configuration.jwt.AuthTokenFilter;
import com.application.backend.configuration.jwt.HandleUnauthorized;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	    private final String[] allowedOrigins = System.getenv("CORS_URL").split(",");
	
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Autowired
		private HandleUnauthorized unauthorizedHandler;

		@Bean
		public AuthTokenFilter authJwtTokenFilter() {
		   return new AuthTokenFilter();
		}
		  
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
		      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		       
		      authProvider.setUserDetailsService(userDetailsService);
		      authProvider.setPasswordEncoder(passwordEncoder());
		   
		      return authProvider;
		}
		 
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		    return authConfig.getAuthenticationManager();
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
		    return new BCryptPasswordEncoder();
		}
		 
			 
	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	     
	        http.csrf(csrf -> csrf.disable())
	        	.cors(Customizer.withDefaults()) //lay bean co default la CorsConfigurationSource
	        	.exceptionHandling(exception ->exception.authenticationEntryPoint(unauthorizedHandler))
	        	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
	        	.authorizeHttpRequests(auth -> 
		            auth.requestMatchers("/","/images/**","/login","/register","/oauth/**","/activeAccount",
									"/ws/**","/chatUser/**","/chat-endpoint/**","/verifyEmail","/createNewPassword","/images","/public/**").permitAll()
			            	.anyRequest().authenticated()
		        );

	        
	        http.authenticationProvider(authenticationProvider());
	        http.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	
	 
	        return http.build();
	    }
	    
	    @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	    	
	    	 CorsConfiguration configuration = new CorsConfiguration();
	    	 
	    	    configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
	    	    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
				configuration.setAllowCredentials(true);
	    	    configuration.setAllowedHeaders(Arrays.asList(
	    	            "Accept", "Origin", "Content-Type", "Depth", "User-Agent", "If-Modified-Since,","Access-Control-Allow-Origin","Access-Control-Allow-Credentials",
	    	            "Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With", "X-File-Name"));

	    	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    	    
	    	    source.registerCorsConfiguration("/**", configuration);
	    	    
	    	return source;
	    }


}
