package com.gerolivo.stargazerdiary.services;

import java.util.List;

import com.gerolivo.stargazerdiary.domain.Telescope;

public interface SkyObservationService {

	public Telescope addorUpdateTelescope(Telescope telescope);
	public Telescope getTelescope(Long id);
	public List<Telescope> getTelescopesList();
	public void deleteTelescope(Long id);
	
}
