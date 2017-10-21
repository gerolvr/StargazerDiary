package com.gerolivo.stargazerdiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gerolivo.stargazerdiary.domain.Stargazer;

public interface StargazerRepository extends JpaRepository<Stargazer, Long> {

	Stargazer findByUserName(String userName);

}
