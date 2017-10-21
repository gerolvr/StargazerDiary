package com.gerolivo.stargazerdiary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gerolivo.stargazerdiary.domain.Observation;

public interface ObservationRepository extends JpaRepository<Observation, Long> {

}
