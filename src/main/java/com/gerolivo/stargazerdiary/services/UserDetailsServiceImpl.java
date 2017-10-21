package com.gerolivo.stargazerdiary.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gerolivo.stargazerdiary.domain.Role;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	StargazerRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("UserDetailsServiceImpl.UserName requesting: " + userName);
		Stargazer stargazer = userRepository.findByUserName(userName);
		
		// Check required to respect the contract from loadUserByUsername()
		if (stargazer == null) {
			throw new UsernameNotFoundException("The user could not be found: " + userName);
		} else if (stargazer.getRoles().isEmpty()) {
			throw new UsernameNotFoundException("The user has no GrantedAuthority: " + userName);
		}
		
		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		for(Role role : stargazer.getRoles()) {
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getRole());
			roles.add(simpleGrantedAuthority);
			
		}
		
		User userDetails = new User(stargazer.getUserName(), stargazer.getPassword(), roles);
		return userDetails;
	}

}
