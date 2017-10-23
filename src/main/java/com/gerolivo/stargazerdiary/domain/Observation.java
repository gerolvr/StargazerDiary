package com.gerolivo.stargazerdiary.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gerolivo.stargazerdiary.utils.ObservationCustomeDateDeserializer;

@Entity
public class Observation extends AbstractDomainClass{

	@NotBlank
    @Size(max=100)
	private String name;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yy")
	@JsonSerialize(using=ObservationCustomeDateDeserializer.class)
	@Basic(optional = true)
	private Date date;
	
	private String report;
	
	@ManyToOne
    private Stargazer stargazer;
	
	public Observation() {
	}

	public Observation(String name, Date date, String report) {
		this.name = name;
		this.date = date;
		this.report = report;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void setStargazer(Stargazer stargazer) {
		this.stargazer = stargazer;
	}

	@Override
	public String toString() {
		return "ObservationReport [name=" + name + ", date=" + date + ", report=" + report + "]";
	}

	
}
