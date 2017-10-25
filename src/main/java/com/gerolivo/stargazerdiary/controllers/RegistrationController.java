package com.gerolivo.stargazerdiary.controllers;

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
import com.gerolivo.stargazerdiary.domain.StargazerDtoNoMail;
import com.gerolivo.stargazerdiary.services.RegistrationService;

@Controller
public class RegistrationController {

	@Autowired
	RegistrationService registrationService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new StargazerDtoNoMail());
		return "registration/register";
	}
	
	@PostMapping("/register")
	public ModelAndView register(ModelAndView modelAndView, @Valid @ModelAttribute("user") StargazerDtoNoMail stargazerDto,  BindingResult bindingResult) {

		Stargazer existingStargazer = registrationService.findUserByName(stargazerDto.getName());
		if (existingStargazer != null) {
			modelAndView.addObject("alreadyRegisteredError",
					"There is already a user with this name.");
			modelAndView.setViewName("registration/register");
			bindingResult.reject("name");
		}
		
		if(bindingResult.hasErrors()) {
			modelAndView.setViewName("registration/register");
		}
		else {
			// Create a new Stargazer account
			Stargazer stargazer = new Stargazer(stargazerDto.getName(), passwordEncoder.encode(stargazerDto.getPassword()));

			stargazer.setEnabled(true);

			
			Role userRole = registrationService.findRole("USER");
			stargazer.addRole(userRole);
			
			registrationService.saveStargazer(stargazer);
			
			// For demo purpose, enable the account immediately.
			registrationService.autologin(stargazerDto.getName(), stargazerDto.getPassword());
			modelAndView.addObject("thankyouMessage", "Thanks for registering. Your are now logged in.");
			
			modelAndView.setViewName("registration/register");
		}
		return modelAndView;
	}
}
