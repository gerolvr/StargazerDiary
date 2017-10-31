package com.gerolivo.stargazerdiary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Telescope extends AbstractDomainClass{

    @NotBlank
    @Size(max=100)
	private String name;
    
	private String maker;
	private String model;
	
	@Column(columnDefinition = "TEXT")
	private String features;
	
	@ManyToOne
    private Stargazer stargazer;
	
	private TelescopeType telescopeType;
	
	public Telescope() {
	}

	public Telescope(String name, String maker, String model, String features, TelescopeType telescopeType) {
		this.name = name;
		this.maker = maker;
		this.model = model;
		this.features = features;
		this.telescopeType = telescopeType;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public TelescopeType getTelescopeType() {
		return telescopeType;
	}

	public void setTelescopeType(TelescopeType telescopeType) {
		this.telescopeType = telescopeType;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObserver(Stargazer stargazer) {
		this.stargazer = stargazer;
	}

	@Override
	public String toString() {
		return "Telescope [name=" + name + ", maker=" + maker + ", model=" + model + ", features=" + features
				+ ", telescopeType=" + telescopeType + "]";
	}
	
}
