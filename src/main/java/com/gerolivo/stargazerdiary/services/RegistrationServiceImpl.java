package com.gerolivo.stargazerdiary.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.gerolivo.stargazerdiary.domain.Role;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.repositories.RoleRepository;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	StargazerRepository stargazerRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public Stargazer findUserByName(String name) {
		return stargazerRepository.findByUserName(name);
	}

	@Override
	public Stargazer findUserByEmail(String email) {
		return stargazerRepository.findByEmail(email);
	}

	@Override
	public void saveStargazer(Stargazer stargazer) {
		stargazerRepository.save(stargazer);
	}

	@Override
	public Role findRole(String role) {
		return roleRepository.findByRole(role);
	}

	@Override
	public void autologin(String username, String password) {
		Logger logger = Logger.getLogger(RegistrationServiceImpl.class);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, password, userDetails.getAuthorities());

		authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			logger.debug(String.format("Auto login %s successfully!", username));
		}
		else {
			logger.debug(String.format("Auto login %s failed!", username));
		}

	}

}
