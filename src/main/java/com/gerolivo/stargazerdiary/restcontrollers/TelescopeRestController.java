package com.gerolivo.stargazerdiary.restcontrollers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.domain.Telescope;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;
import com.gerolivo.stargazerdiary.services.SkyObservationService;

@RestController
@RequestMapping("${restapi.path}" + "/telescopes")
public class TelescopeRestController {
	
	Logger logger = Logger.getLogger(TelescopeRestController.class);

	private SkyObservationService skyObservationService; 
	private StargazerRepository stargazerRepository;
	
	public TelescopeRestController(SkyObservationService skyObservationService
			, StargazerRepository stargazerRepository) {
		this.skyObservationService = skyObservationService;
		this.stargazerRepository = stargazerRepository;
	}


	@GetMapping("/telescopeList")
	public List<Telescope> getTelescopeList(@AuthenticationPrincipal User user) {
		logger.debug("Getting telescope list for user: " + user.getUsername());
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		stargazer.getTelescopes();
		return stargazer.getTelescopes();
	}
	
	@PostMapping("/saveTelescope")
	public ResponseEntity<String> addTelescope(@RequestBody Telescope newTelescope, @AuthenticationPrincipal User user) throws Exception {

		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		
		Telescope currentTelescope = null;
		if(newTelescope.getId() != null) {
			currentTelescope = skyObservationService.getTelescope(newTelescope.getId());
		}
		// Update existing telescope
		if(currentTelescope != null) {
			if(!stargazer.getTelescopes().contains(currentTelescope)) {
				throw new Exception("You are not authorized to update this telescope with id: " + newTelescope.getId()); 
			}
			currentTelescope.setFeatures(newTelescope.getFeatures());
			currentTelescope.setMaker(newTelescope.getMaker());
			currentTelescope.setModel(newTelescope.getModel());
			currentTelescope.setName(newTelescope.getName());
			currentTelescope.setTelescopeType(newTelescope.getTelescopeType());
			skyObservationService.addorUpdateTelescope(currentTelescope);
		}
		else {
			newTelescope.setObserver(stargazer);
			skyObservationService.addorUpdateTelescope(newTelescope);
		}

		return new ResponseEntity<String>("Telescope successfuly saved", HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public Telescope getTelescope(@PathVariable Long id, @AuthenticationPrincipal User user) throws Exception {
		
		Telescope telescope = skyObservationService.getTelescope(id);
		
		if(telescope == null) {
			throw new Exception("Could not find a telescope with id: " + id);
		}
		
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(!stargazer.getTelescopes().contains(telescope)) {
			throw new Exception("You are not authorized to get this telescope with id: " + id);
		}
		
		return telescope;
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTelescope(@PathVariable Long id, @AuthenticationPrincipal User user) throws Exception {

		Telescope telescope = skyObservationService.getTelescope(id);

		if(telescope == null) {
			throw new Exception("Could not delete a telescope with id: " + id);
		}

		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(!stargazer.getTelescopes().contains(telescope)) {
			throw new Exception("You are not authorized to delete this telescope with id: " + id); 
		}
		
		stargazer.removeTelescope(telescope);
		skyObservationService.deleteTelescope(id);
		
		return new ResponseEntity<String>("Telescope successfuly deleted", HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(HttpServletRequest httpServletRequest, Exception exception){
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
