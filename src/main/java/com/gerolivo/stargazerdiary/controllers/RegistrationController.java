package com.gerolivo.stargazerdiary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {

	@GetMapping("/register")
	public String register() {
		return "registration/register";
	}
}
