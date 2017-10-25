package com.gerolivo.stargazerdiary.services;

import com.gerolivo.stargazerdiary.domain.Role;
import com.gerolivo.stargazerdiary.domain.Stargazer;

public interface RegistrationService {
	
	public Stargazer findUserByName(String name);
	
	public Stargazer findUserByEmail(String email);
	
	public void saveStargazer(Stargazer stargazer);
	
	public Role findRole(String role);
	
	public void autologin(String username, String password);

}
