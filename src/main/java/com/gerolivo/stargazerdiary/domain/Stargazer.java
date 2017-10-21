package com.gerolivo.stargazerdiary.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Stargazer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String userName;
	private String password;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "stargazer")
    private List<Telescope> telescopes = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "stargazer")
    private List<Observation> observations = new ArrayList<>();	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable
	private Set<Role> roles = new HashSet<Role>();
	
	protected Stargazer() {
	}

	public Stargazer(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Telescope> getTelescopes() {
		return telescopes;
	}

	public void setTelescopes(List<Telescope> telescopes) {
		this.telescopes = telescopes;
	}
	
	public void addTelescope(Telescope telescope) {
		telescopes.add(telescope);
		telescope.setObserver(this);
	}
	
	public void removeTelescope(Telescope telescope) {
		telescope.setObserver(null);
		telescopes.remove(telescope);
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}
	
	public void addObservation(Observation observation) {
		observations.add(observation);
		observation.setStargazer(this);
	}
	
	public void removeObservation(Observation observation) {
		observation.setStargazer(null);
		observations.remove(observation);
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public void removeRole(Role role) {
		roles.remove(role);
	}
	
	
}
