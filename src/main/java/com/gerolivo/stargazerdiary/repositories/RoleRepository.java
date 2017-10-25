package com.gerolivo.stargazerdiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gerolivo.stargazerdiary.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRole(String role);
	
}
