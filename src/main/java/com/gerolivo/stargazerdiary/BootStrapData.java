package com.gerolivo.stargazerdiary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
			Role roleUser = new Role("USER");
			Role roleAdmin = new Role("ADMIN");
			roleRepository.save(roleUser);
			roleRepository.save(roleAdmin);
			String encodedPassword = encoder.encode("password");
			Stargazer stargazer1 = new Stargazer("user", encodedPassword);
			Stargazer stargazer2 = new Stargazer("user2", encodedPassword);
			Stargazer stargazerAdmin = new Stargazer("userAdmin", encodedPassword);
			stargazer1.addRole(roleUser);
			stargazer2.addRole(roleUser);
			stargazerAdmin.addRole(roleUser);
			stargazerAdmin.addRole(roleAdmin);
			
			Telescope telescope = new Telescope("Stargazer's Dobson", "Meade", "Lightbridge", "Lot of cool features", TelescopeType.DOBSON);
			Telescope telescope2 = new Telescope("Stargazer's Reflector2", "Meade", "Lightbridge", "Lot of cool features 2", TelescopeType.REFLECTOR);
			stargazer1.addTelescope(telescope);
			stargazer1.addTelescope(telescope2);
			
			observerRepository.save(stargazer1);
			observerRepository.save(stargazer2);
			observerRepository.save(stargazerAdmin);
			observerRepository.flush();
			
			
		};
	}
}
