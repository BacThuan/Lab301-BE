package com.application.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.application.backend.model.User;
import com.application.backend.userdetails.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.findByEmail(email);
	
		
		if(user == null) throw new UsernameNotFoundException("User is not found!");
		
		
		return CustomUserDetails.create(user) ;
	}

}
