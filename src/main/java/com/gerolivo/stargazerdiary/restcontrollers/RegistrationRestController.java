package com.gerolivo.stargazerdiary.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerolivo.stargazerdiary.domain.Role;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.domain.StargazerDtoNoMail;
import com.gerolivo.stargazerdiary.services.RegistrationService;

@RestController
@RequestMapping("${restapi.path}" + "/registration")
public class RegistrationRestController {
	
	@Autowired
	RegistrationService registrationService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody StargazerDtoNoMail stargazerDto){
		System.out.println("Name: " + stargazerDto.getName());
		System.out.println("Password: " + stargazerDto.getPassword());
		Stargazer existingStargazer = registrationService.findUserByName(stargazerDto.getName());
		if (existingStargazer != null) {
			return new ResponseEntity<>("There is already a user with this name.", HttpStatus.BAD_REQUEST);
		}
		else {
			// Create a new Stargazer acount
			Stargazer stargazer = new Stargazer(stargazerDto.getName(),
					passwordEncoder.encode(stargazerDto.getPassword()));

			stargazer.setEnabled(true);
			
			Role userRole = registrationService.findRole("USER");
			stargazer.addRole(userRole);
			
			registrationService.saveStargazer(stargazer);
			
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
