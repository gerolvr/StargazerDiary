package com.gerolivo.stargazerdiary.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gerolivo.stargazerdiary.domain.Role;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.domain.StargazerDto;
import com.gerolivo.stargazerdiary.services.RegistrationService;

@Controller
public class RegistrationControllerWithEmail {

	@Autowired
	RegistrationService registrationService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/registerWithEmailConfirmation")
	public String register(Model model) {
		model.addAttribute("user", new StargazerDto());
		return "registration/registerWithEmailConfirmation";
	}
	
	@PostMapping("/registerWithEmailConfirmation")
	public ModelAndView register(ModelAndView modelAndView, @Valid @ModelAttribute("user") StargazerDto stargazerDto,  BindingResult bindingResult) {

		Stargazer existingStargazer = registrationService.findUserByName(stargazerDto.getName());
		if (existingStargazer != null) {
			modelAndView.addObject("alreadyRegisteredNameError",
					"There is already a user with this name.");
			modelAndView.setViewName("registration/registerWithEmailConfirmation");
			bindingResult.reject("name");
		}
		
		existingStargazer = registrationService.findUserByEmail(stargazerDto.getEmail());
		if (existingStargazer != null) {
			modelAndView.addObject("alreadyRegisteredEmailError",
					"There is already a user with this email.");
			modelAndView.setViewName("registration/registerWithEmailConfirmation");
			bindingResult.reject("email");
		}
		
		if(bindingResult.hasErrors()) {
			modelAndView.setViewName("registration/registerWithEmailConfirmation");
		}
		else {
			// Create a new Stargazer account
			// Todo: Clear not enabled account after 48 hours.
			Stargazer stargazer = new Stargazer(stargazerDto.getName(), passwordEncoder.encode(stargazerDto.getPassword()));
			
			stargazer.setEmail(stargazerDto.getEmail());
			// Disable until email link activated.
			stargazer.setEnabled(false);
		    // Random string token to include into the email link
			stargazer.setEmailConfirmationToken(UUID.randomUUID().toString());
			
			Role userRole = registrationService.findRole("USER");
			stargazer.addRole(userRole);
			
			registrationService.saveStargazer(stargazer);
			
//			registrationService.autologin(stargazerDto.getName(), stargazerDto.getPassword());
			modelAndView.addObject("thankyouMessage", "Thanks for registering. Your are now logged in. A confirmation email has been sent to " + stargazerDto.getEmail());
			
			modelAndView.setViewName("registration/registerWithEmailConfirmation");
		}
		return modelAndView;
	}
}
