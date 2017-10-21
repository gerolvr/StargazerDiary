package com.gerolivo.stargazerdiary.controllers;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.domain.Telescope;
import com.gerolivo.stargazerdiary.exceptions.NotFoundException;
import com.gerolivo.stargazerdiary.exceptions.UnauthorizedException;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;
import com.gerolivo.stargazerdiary.services.SkyObservationService;

@Controller
@RequestMapping("/telescopes")
public class TelescopeController {

	private SkyObservationService skyObservationService; 
	private StargazerRepository stargazerRepository;
	
	public TelescopeController(SkyObservationService skyObservationService
			, StargazerRepository stargazerRepository) {
		this.skyObservationService = skyObservationService;
		this.stargazerRepository = stargazerRepository;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("showingHome", false);
	    model.addAttribute("showingTelescopeList", true);
	    model.addAttribute("showingMyObservationsList", false);
	    model.addAttribute("showingAstroData", false);
	    
	}
	
	@GetMapping("")
	public String home() {
		return "redirect:/telescopes/telescopeList";
	}
	
//	@GetMapping("/telescopeFullList")
//	public String getTelescopesFullList(Model model) {
//		List<Telescope> telescopesList = skyObservationService.getTelescopesList();
//		model.addAttribute("telescopesList", telescopesList);
//		
//		return "telescope/telescopeList";
//	}
	
	@GetMapping("/telescopeList")
	public String getTelescopesList(Model model, @AuthenticationPrincipal User user) {
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		model.addAttribute("telescopesList", stargazer.getTelescopes());
		
		return "telescope/telescopeList";
	}
	
	@GetMapping("/add")
	public String addTelescope(Model model) {
		model.addAttribute("telescope", new Telescope());
		
		return "telescope/telescopeForm";
	}
	
	@PostMapping("/telescopeForm")
	public String addTelescopeForm(@Valid @ModelAttribute("telescope") Telescope telescope, BindingResult bindingResult, @AuthenticationPrincipal User user) {
		if(bindingResult.hasErrors())
		{
			return "telescope/telescopeForm"; 
		}
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		telescope.setObserver(stargazer);

		skyObservationService.addorUpdateTelescope(telescope);
		
		return "redirect:/telescopes/telescopeList";
	}
	
	@GetMapping("/edit/{id}")
	public String editTelescopeByVariablePath(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) throws Exception {
		Telescope telescope = skyObservationService.getTelescope(id);
		if(telescope == null) {
			throw new NotFoundException("Could not find a telescope with id: " + id);
		}
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(!stargazer.getTelescopes().contains(telescope)) {
			throw new UnauthorizedException("You are not authorized to edit this telescope with id: " + id); 
		}
		model.addAttribute("telescope", telescope);
		
		return "telescope/telescopeForm"; 
	}
	
	@GetMapping("/delete/{id}")
	public String deleteTelescopeById(@PathVariable/*(value="id")*/ Long id, @AuthenticationPrincipal User user) throws Exception
	{
		Telescope telescope = skyObservationService.getTelescope(id);
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(telescope == null) {
			throw new NotFoundException("Could not delete unexisting telescope with id: " + id);
		}
		if(!stargazer.getTelescopes().contains(telescope)) {
			throw new UnauthorizedException("You are not authorized to delete this telescope with id: " + id); 
		}
		stargazer.removeTelescope(telescope);
		skyObservationService.deleteTelescope(id);
		
		return "redirect:/telescopes/telescopeList";
	}

}
