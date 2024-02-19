package com.application.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins = System.getenv("CORS_URL").split(",");


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		        .allowedOrigins(allowedOrigins)
		        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		        .allowedHeaders("Accept", "Origin", "Content-Type", "Depth", "User-Agent", "If-Modified-Since,","Access-Control-Allow-Origin","Access-Control-Allow-Credentials",
						"Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With", "X-File-Name")
				.allowCredentials(true)
		        .maxAge(3600);      
	}



}
