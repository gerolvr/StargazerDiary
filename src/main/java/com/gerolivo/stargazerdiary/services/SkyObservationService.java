package com.gerolivo.stargazerdiary.services;

import java.util.List;

import com.gerolivo.stargazerdiary.domain.Observation;
import com.gerolivo.stargazerdiary.domain.Telescope;

public interface SkyObservationService {

	public Telescope addorUpdateTelescope(Telescope telescope);
	public Telescope getTelescope(Long id);
	public List<Telescope> getTelescopesList();
	public void deleteTelescope(Long id);
	
	public Observation addorUpdateObservation(Observation observationReport);
	public Observation getObservation(Long id);
	public List<Observation> getObservationsList();
	public void deleteObservation(Long id);
	
}
