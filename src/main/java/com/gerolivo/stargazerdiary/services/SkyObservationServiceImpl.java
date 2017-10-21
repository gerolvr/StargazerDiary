package com.gerolivo.stargazerdiary.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gerolivo.stargazerdiary.domain.Telescope;
import com.gerolivo.stargazerdiary.repositories.TelescopeRepository;


@Service
public class SkyObservationServiceImpl implements SkyObservationService {
	
	private TelescopeRepository telescopeRepository;

	public SkyObservationServiceImpl(TelescopeRepository telescopeRepository) {
		this.telescopeRepository = telescopeRepository;
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

}
