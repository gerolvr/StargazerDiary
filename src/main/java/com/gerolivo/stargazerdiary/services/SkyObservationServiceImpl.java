package com.gerolivo.stargazerdiary.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gerolivo.stargazerdiary.domain.Observation;
import com.gerolivo.stargazerdiary.domain.Telescope;
import com.gerolivo.stargazerdiary.repositories.ObservationRepository;
import com.gerolivo.stargazerdiary.repositories.TelescopeRepository;


@Service
public class SkyObservationServiceImpl implements SkyObservationService {
	
	private TelescopeRepository telescopeRepository;
	private ObservationRepository observationRepository;

	public SkyObservationServiceImpl(TelescopeRepository telescopeRepository,
			ObservationRepository observationRepository) {
		this.telescopeRepository = telescopeRepository;
		this.observationRepository = observationRepository;
	}

	@Override
	public Telescope addorUpdateTelescope(Telescope telescope) {
		return telescopeRepository.save(telescope);
		
	}

	@Override
	public Telescope getTelescope(Long id) {
		return telescopeRepository.findOne(id);
	}

	@Override
	public List<Telescope> getTelescopesList() {
		return telescopeRepository.findAll();
	}
	
	@Override
	public void deleteTelescope(Long id) {
		telescopeRepository.delete(id);
	}
	
	@Override
	public Observation addorUpdateObservation(Observation observationReport) {
		return observationRepository.save(observationReport);
	}

	@Override
	public Observation getObservation(Long id) {
		return observationRepository.findOne(id);
	}

	@Override
	public List<Observation> getObservationsList() {
		return observationRepository.findAll();
	}

	@Override
	public void deleteObservation(Long id) {
		observationRepository.delete(id);		
	}

}
