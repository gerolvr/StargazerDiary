package com.gerolivo.stargazerdiary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("showingHome", true);
	    model.addAttribute("showingTelescopeList", false);
	    model.addAttribute("showingMyObservationsList", false);
	    model.addAttribute("showingAstroData", false);
	}
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
