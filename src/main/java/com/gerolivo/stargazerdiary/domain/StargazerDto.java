package com.gerolivo.stargazerdiary.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class StargazerDto {
	
	@NotBlank(message = "Please enter a valid name")
    private String name;
	
	@NotBlank(message = "Please enter a password")
    private String password;
    
    @Email(message = "Please enter a valid e-mail")
    @NotBlank(message = "Please enter an e-mail")
    private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
