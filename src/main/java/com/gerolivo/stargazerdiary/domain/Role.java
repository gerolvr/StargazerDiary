package com.gerolivo.stargazerdiary.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	protected Role() {}
	
	public Role(String role) {
		this.role = role;
	}

	@ManyToMany(mappedBy = "roles")
	Set<Stargazer> stargazers = new HashSet<Stargazer>();
	
	private String role;

	public Set<Stargazer> getStargazers() {
		return stargazers;
	}

	public void setStargazers(Set<Stargazer> stargazers) {
		this.stargazers = stargazers;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void addObserver(Stargazer stargazer) {
		this.stargazers.add(stargazer);
	}
	
	public void removeObserver(Stargazer stargazer) {
		stargazers.remove(stargazer);
	}
	
}
