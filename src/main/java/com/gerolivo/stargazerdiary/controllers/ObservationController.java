package com.gerolivo.stargazerdiary.controllers;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gerolivo.stargazerdiary.domain.Observation;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.exceptions.NotFoundException;
import com.gerolivo.stargazerdiary.exceptions.UnauthorizedException;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;
import com.gerolivo.stargazerdiary.services.SkyObservationService;

@Controller
@RequestMapping("/observations")
public class ObservationController {

	private SkyObservationService skyObservationService; 
	private StargazerRepository stargazerRepository;
	
	public ObservationController(SkyObservationService skyObservationService
			, StargazerRepository stargazerRepository) {
		this.skyObservationService = skyObservationService;
		this.stargazerRepository = stargazerRepository;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("showingHome", false);
	    model.addAttribute("showingObservationList", false);
	    model.addAttribute("showingMyObservationsList", true);
	    model.addAttribute("showingAstroData", false);
	}
	
	@GetMapping("")
	public String home() {
		return "redirect:/observations/observationList";
	}
	
//	@GetMapping("/observationFullList")
//	public String getObservationsFullList(Model model) {
//		List<Observation> observationsList = skyObservationService.getObservationsList();
//		model.addAttribute("observationsList", observationsList);
//		return "observation/observationList";
//	}
	
	@GetMapping("/observationList")
	public String getObservationList(Model model, @AuthenticationPrincipal User user) {
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		model.addAttribute("observationList", stargazer.getObservations());
		return "observation/observationList";
	}
	
	@GetMapping("/add")
	public String addObservation(Model model) {
		
		model.addAttribute("observation", new Observation());
		model.addAttribute("header", "Add a new observation");
		model.addAttribute("title", "Stargazer Diary - Add a new observation");
		
		return "observation/observationForm";
	}
	
	@PostMapping("/observationForm")
	public String addObservationForm(@Valid @ModelAttribute("observation") Observation observationReport, BindingResult bindingResult, @AuthenticationPrincipal User user) {
		System.out.println(observationReport);
		if(bindingResult.hasErrors())
		{
			return "observation/observationForm"; 
		}
		System.out.println(observationReport);
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		observationReport.setStargazer(stargazer);
		
		skyObservationService.addorUpdateObservation(observationReport);
		
		return "redirect:/observations/observationList";
	}
	
	@GetMapping("/edit")
	public String editObservation(@RequestParam Long observationId, Model model, @AuthenticationPrincipal User user) throws Exception {
		Observation observationReport = skyObservationService.getObservation(observationId);
		if(observationReport == null) {
			throw new NotFoundException("Could not find an observation with id: " + observationId);
		}
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(!stargazer.getObservations().contains(observationReport)) {
			throw new UnauthorizedException("You are not authorized to edit this observation with id: " + observationId);
		}		
		System.out.println(observationReport);
		model.addAttribute("observation", observationReport);
		model.addAttribute("header", "Edit an observation");
		model.addAttribute("title", "Stargazer Diary - Edit an observation");
		
		return "observation/observationForm"; 
	}
	
	@GetMapping("/delete")
	public String deleteObservationByParam(@RequestParam Long observationId, @AuthenticationPrincipal User user) throws Exception {
		Observation observation = skyObservationService.getObservation(observationId);
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(observation == null) {
			throw new NotFoundException("Could not delete a observation with id: " + observationId);
		}
		if(!stargazer.getObservations().contains(observation)) {
			throw new UnauthorizedException("You are not authorized to delete this observation with id: " + observationId);
		}
		stargazer.removeObservation(observation);
		skyObservationService.deleteObservation(observationId);
		
		return "redirect:/observations/observationList";
	}
	
//	@GetMapping("/edit/{id}")
//	public String editObservationByVariablePath(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) throws Exception {
//		Observation observation = skyObservationService.getObservation(id);
//		
//		if(observation == null) {
//			throw new NotFoundException("Could not find a observation with id: " + id);
//		}
//		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
//		if(!stargazer.getObservations().contains(observation)) {
//			throw new UnauthorizedException("You are not authorized to edit this observation with id: " + id); 
//		}
//		model.addAttribute("observation", observation);
//		
//		return "observation/observationForm"; 
//	}
	
//	@GetMapping("/delete/{id}")
//	public String deleteObservationById(@PathVariable Long id, @AuthenticationPrincipal User user) throws Exception
//	{
//		Observation observation = skyObservationService.getObservation(id);
//		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
//		
//		if(observation == null) {
//			throw new NotFoundException("Could not delete a observation with id: " + id);
//		}
//		if(!stargazer.getObservations().contains(observation)) {
//			throw new UnauthorizedException("You are not authorized to delete this observation with id: " + id); 
//		}
//		stargazer.removeObservation(observation);
//		skyObservationService.deleteObservation(id);
//		
//		return "redirect:/observations/observationList";
//	}

}
