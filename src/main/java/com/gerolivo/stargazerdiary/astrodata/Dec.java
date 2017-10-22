package com.gerolivo.stargazerdiary.astrodata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dec {
	
	private String decimal;
	private String d;
	private String m;
	private String s;

	public String getDecimal() {
		return decimal;
	}

	public void setDecimal(String decimal) {
		this.decimal = decimal;
	}

	public String getD() {
		return d;
	}

	/**
	 * When there is a space in the search, i.e. "M 51" rather than "M51",
	 * the decimal value is " 47" rather than "+47", needs to trim the leading
	 * whitespace.
	 */
	public void setD(String d) {
		this.d = d.trim();
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return "Dec [decimal=" + decimal + ", d=" + d + ", m=" + m + ", s=" + s + "]";
	}
	
	

}
