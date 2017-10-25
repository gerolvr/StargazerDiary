package com.gerolivo.stargazerdiary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.gerolivo.stargazerdiary.domain.Role;
import com.gerolivo.stargazerdiary.repositories.RoleRepository;

@Component
public class BootStrapRoles {

	@Bean
	CommandLineRunner boostrapRoles(RoleRepository roleRepository) {
		return (args) -> {
			Role roleUser = new Role("USER");
			Role roleAdmin = new Role("ADMIN");
			Role roleActuator = new Role("ACTUATOR");
			if(roleRepository.findByRole("USER") == null) {
				roleRepository.saveAndFlush(roleUser);
			}
			if(roleRepository.findByRole("ADMIN") == null) {
				roleRepository.saveAndFlush(roleAdmin);
			}
			if(roleRepository.findByRole("ACTUATOR") == null) {
				roleRepository.saveAndFlush(roleActuator);
			}
//			roleRepository.save(roleUser);
//			roleRepository.save(roleAdmin);
//			roleRepository.save(roleActuator);
//			roleRepository.flush();
		};
	}
}
