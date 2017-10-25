package com.gerolivo.stargazerdiary.domain;

import org.hibernate.validator.constraints.NotBlank;

public class StargazerDtoNoMail {
	
	@NotBlank(message = "Please enter a valid name")
    private String name;
	
	@NotBlank(message = "Please enter a password")
    private String password;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
