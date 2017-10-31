package com.gerolivo.stargazerdiary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gerolivo.stargazerdiary.astrodata.AstroObjectData;
import com.gerolivo.stargazerdiary.services.AstroDataService;

@Controller
@RequestMapping("/astrodata")
public class AstroDataController {

	AstroDataService astroDataService;
	
	public AstroDataController(AstroDataService astroDataService) {
		this.astroDataService = astroDataService;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("showingHome", false);
	    model.addAttribute("showingTelescopeList", false);
	    model.addAttribute("showingMyObservationsList", false);
	    model.addAttribute("showingAstroData", true);
	}
	
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("astroObjectName", new String());
		return "astrodata/astrodata";
	}
	
	@GetMapping("result")
	public String result(Model model, @RequestParam("astroObjectName") String astroObjectName) {
		
		AstroObjectData skyObjectValue = astroDataService.getAstroDataForObjectName(astroObjectName);
		if(skyObjectValue.getRa()==null)
		{
			String errorMessage = astroObjectName + " not found, please check the name is correct.";
			model.addAttribute("errorAstroObjectNotFound", errorMessage);
			model.addAttribute("astroObjectNotFound", true);
			return "astrodata/astrodata";
		}
		
		StringBuilder coordinates = astroDataService.generateCoordinate(skyObjectValue);
		model.addAttribute("astroObjectName", astroObjectName);
		model.addAttribute("astroObjectType", skyObjectValue.getCategory().getAvmdesc());
		model.addAttribute("astroObjectCoordinates", coordinates.toString());
		model.addAttribute("foundAstroObject", skyObjectValue);
		
		StringBuilder stringBuilderUrl = astroDataService.generatePlanetariumIFrameUrl(skyObjectValue);
		model.addAttribute("iframeUrl", stringBuilderUrl.toString());
		
		StringBuilder stringBuilderUrlWS = astroDataService.generateWikiskyIFrameUrl(skyObjectValue);
		model.addAttribute("iframeUrlWS", stringBuilderUrlWS.toString());
		
		return "astrodata/result";
	}
}
