package com.gerolivo.stargazerdiary;

import java.util.Calendar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gerolivo.stargazerdiary.domain.Observation;
import com.gerolivo.stargazerdiary.domain.Role;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.domain.Telescope;
import com.gerolivo.stargazerdiary.domain.TelescopeType;
import com.gerolivo.stargazerdiary.repositories.RoleRepository;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;

@Component
@Profile("dev")
public class BootStrapData {

	@Bean
	CommandLineRunner boostrap(StargazerRepository observerRepository, PasswordEncoder encoder, RoleRepository roleRepository/*, RoleRepository2 roleRepository2*/) {
		return (args) -> {
			Role roleUser = roleRepository.findByRole("USER");// = new Role("USER");
            Role roleAdmin = roleRepository.findByRole("ADMIN");// = new Role("ADMIN");
            Role roleActuator = roleRepository.findByRole("ACTUATOR");// = new Role("ACTUATOR");
            if(roleUser == null) {
            	roleUser = roleRepository.saveAndFlush(new Role("USER"));
            }
            
            if(roleAdmin == null) {
            	roleAdmin = roleRepository.saveAndFlush(new Role("ADMIN"));
            }
            
            if(roleActuator == null) {
            	roleActuator = roleRepository.saveAndFlush(new Role("ACTUATOR"));
            }

            String encodedPassword = encoder.encode("password");
            
            if(observerRepository.findByUserName("user") == null) {
            	Stargazer stargazer1 = new Stargazer("user", encodedPassword);
            	stargazer1.addRole(roleUser);
            	Telescope telescope = new Telescope("Stargazer's Dobson", "Meade", "Lightbridge", "Lot of cool features", TelescopeType.DOBSON);
    			Telescope telescope2 = new Telescope("Stargazer's Reflector2", "Meade", "Lightbridge", "Lot of cool features 2", TelescopeType.REFLECTOR);
    			stargazer1.addTelescope(telescope);
    			stargazer1.addTelescope(telescope2);
    			Observation observation1 = new Observation("Observation name 1", Calendar.getInstance().getTime(), "Some report 1");
    			Observation observation2 = new Observation("Observation name 2", Calendar.getInstance().getTime(), "Some report 2");
    			stargazer1.addObservation(observation1);
    			stargazer1.addObservation(observation2);
    			observerRepository.saveAndFlush(stargazer1);
            }
            
            if(observerRepository.findByUserName("userAdmin") == null) {
            	Stargazer stargazerAdmin = new Stargazer("userAdmin", encodedPassword);
    			
    			stargazerAdmin.addRole(roleUser);
    			stargazerAdmin.addRole(roleAdmin);
    			stargazerAdmin.addRole(roleActuator);
    			
    			Telescope telescope3 = new Telescope("Stargazer's Reflector3", "Meade", "Lightbridge", "Lot of cool features 3", TelescopeType.REFLECTOR);
    			Observation observation3 = new Observation("Observation name 3", Calendar.getInstance().getTime(), "Some report 3");
    			stargazerAdmin.addTelescope(telescope3);
    			stargazerAdmin.addObservation(observation3);;
    			
    			observerRepository.saveAndFlush(stargazerAdmin);
            }
		};
	}
}
