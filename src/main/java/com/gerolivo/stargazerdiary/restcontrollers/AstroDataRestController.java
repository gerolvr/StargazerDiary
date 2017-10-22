package com.gerolivo.stargazerdiary.restcontrollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerolivo.stargazerdiary.astrodata.AstroDataSearchResult;
import com.gerolivo.stargazerdiary.astrodata.AstroObjectData;
import com.gerolivo.stargazerdiary.services.AstroDataService;

@RestController
@RequestMapping("${restapi.path}" + "/astrodata")
public class AstroDataRestController {

	AstroDataService astroDataService;
	
	public AstroDataRestController(AstroDataService astroDataService) {
		this.astroDataService = astroDataService;
	}
	
	@GetMapping("/search/{astroObjectName}")
	public AstroDataSearchResult result(@PathVariable String astroObjectName) {
		
		AstroObjectData astroObjectData = astroDataService.getAstroDataForObjectName(astroObjectName);
		if(astroObjectData.getRa()==null)
		{
			AstroDataSearchResult astroDataSearchResult = new AstroDataSearchResult();
			astroDataSearchResult.setFound(false);
			return astroDataSearchResult;
		}
		
		StringBuilder coordinates = astroDataService.generateCoordinate(astroObjectData);
		StringBuilder stringBuilderUrlPlanetarium = astroDataService.generatePlanetariumIFrameUrl(astroObjectData);
		StringBuilder stringBuilderUrlWiskySky = astroDataService.generateWhiskyskyIFrameUrl(astroObjectData);
		
		AstroDataSearchResult astroDataSearchResult = new AstroDataSearchResult(
				true,
				astroObjectData.getTarget().getName(),
				astroObjectData.getCategory().getAvmdesc(),
				coordinates.toString(),
				stringBuilderUrlPlanetarium.toString(),
				stringBuilderUrlWiskySky.toString(),
				astroObjectData.getImage().getSrc()
				);
		
		System.out.println("AstroDataSearchResult: " + astroDataSearchResult.toString());
		
		return astroDataSearchResult;
	}
}