package com.gerolivo.stargazerdiary.astrodata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

	private String avmdesc;

	public String getAvmdesc() {
		return avmdesc;
	}

	public void setAvmdesc(String avmdesc) {
		this.avmdesc = avmdesc;
	}

	@Override
	public String toString() {
		return "Category [avmdesc=" + avmdesc + "]";
	}
	
}
