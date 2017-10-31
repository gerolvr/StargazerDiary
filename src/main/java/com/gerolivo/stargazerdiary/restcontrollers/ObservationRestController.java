package com.gerolivo.stargazerdiary.restcontrollers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
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

import com.gerolivo.stargazerdiary.domain.Observation;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;
import com.gerolivo.stargazerdiary.services.SkyObservationService;

@RestController
@RequestMapping("${restapi.path}" + "/observations")
public class ObservationRestController {

	Logger logger = Logger.getLogger(ObservationRestController.class);
	
	private SkyObservationService skyObservationService; 
	private StargazerRepository stargazerRepository;
	
	public ObservationRestController(SkyObservationService skyObservationService
			, StargazerRepository stargazerRepository) {
		this.skyObservationService = skyObservationService;
		this.stargazerRepository = stargazerRepository;
	}


	@GetMapping("/observationList")
	public List<Observation> getObservationList(@AuthenticationPrincipal User user) {

		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		
		return stargazer.getObservations();
	}
	
	@PostMapping("/saveObservation")
	public ResponseEntity<String> addObservation(@RequestBody Observation newObservation, @AuthenticationPrincipal User user) {
		
		logger.debug("Saving Observation with id " + newObservation.getId());
		
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		newObservation.setStargazer(stargazer);
		
		Observation currentObservation = null;
		if(newObservation.getId() != null) {
			currentObservation = skyObservationService.getObservation(newObservation.getId());
		}
		if(currentObservation != null) {
			currentObservation.setDate(newObservation.getDate());
			currentObservation.setName(newObservation.getName());
			currentObservation.setReport(newObservation.getReport());
			skyObservationService.addorUpdateObservation(currentObservation);
		}
		else {
			skyObservationService.addorUpdateObservation(newObservation);
		}
		
		logger.debug("Saved Observation with id: " + newObservation.getId());
	
		return new ResponseEntity<String>("Observation successfuly saved", HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public Observation getObservation(@PathVariable(value="id") Long id, @AuthenticationPrincipal User user) throws Exception {
		
		logger.debug("Getting Observation with id " + id);
		
		Observation observation = skyObservationService.getObservation(id);

		if(observation == null) {
			throw new Exception("Could not find an observation with id: " + id);
		}
		
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(!stargazer.getObservations().contains(observation)) {
			throw new Exception("You are not authorized to get this observation with id: " + id);
		}
		
		logger.debug("Got Observation with id " + id);
		
		return observation;
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteObservation(@PathVariable(value="id") Long id, @AuthenticationPrincipal User user) throws Exception {
		
		logger.debug("Deleting Observation with id " + id);
		
		Observation observation = skyObservationService.getObservation(id);
		
		if(observation == null) {
			throw new Exception("Could not delete an observation with id: " + id);
		}
		Stargazer stargazer = stargazerRepository.findByUserName(user.getUsername());
		if(!stargazer.getObservations().contains(observation)) {
			throw new Exception("You are not authorized to delete this observation with id: " + id); 
		}
		
		stargazer.removeObservation(observation);
		skyObservationService.deleteObservation(id);
		
		logger.debug("Deleted Observation with id " + id);
		
		return new ResponseEntity<String>("Observation successfuly deleted", HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(HttpServletRequest httpServletRequest, Exception exception){
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
