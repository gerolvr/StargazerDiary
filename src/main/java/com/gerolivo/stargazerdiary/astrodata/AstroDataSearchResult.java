package com.gerolivo.stargazerdiary.astrodata;

public class AstroDataSearchResult {

	private boolean found = false;
	private String astroObjectName = new String();
	private String astroObjectType = new String();
	private String astroObjectCoordinates = new String();
	private String planetariumUrl = new String();
	private String wiskySkyUrl = new String();
	private String imageUrl = new String();
	


	public AstroDataSearchResult(boolean found, String astroObjectName, String astroObjectType,
			String astroObjectCoordinates, String planetariumUrl, String wiskySkyUrl, String imageUrl) {
		super();
		this.found = found;
		this.astroObjectName = astroObjectName;
		this.astroObjectType = astroObjectType;
		this.astroObjectCoordinates = astroObjectCoordinates;
		this.planetariumUrl = planetariumUrl;
		this.wiskySkyUrl = wiskySkyUrl;
		this.imageUrl = imageUrl;
	}



	public AstroDataSearchResult() {
	}



	public boolean isFound() {
		return found;
	}



	public void setFound(boolean found) {
		this.found = found;
	}



	public String getAstroObjectName() {
		return astroObjectName;
	}



	public void setAstroObjectName(String astroObjectName) {
		this.astroObjectName = astroObjectName;
	}



	public String getAstroObjectType() {
		return astroObjectType;
	}



	public void setAstroObjectType(String astroObjectType) {
		this.astroObjectType = astroObjectType;
	}



	public String getAstroObjectCoordinates() {
		return astroObjectCoordinates;
	}



	public void setAstroObjectCoordinates(String astroObjectCoordinates) {
		this.astroObjectCoordinates = astroObjectCoordinates;
	}

	public String getPlanetariumUrl() {
		return planetariumUrl;
	}



	public void setPlanetariumUrl(String planetariumUrl) {
		this.planetariumUrl = planetariumUrl;
	}



	public String getWiskySkyUrl() {
		return wiskySkyUrl;
	}



	public void setWiskySkyUrl(String wiskySkyUrl) {
		this.wiskySkyUrl = wiskySkyUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	@Override
	public String toString() {
		return "AstroDataSearchResult [found=" + found + ", astroObjectName=" + astroObjectName + ", astroObjectType="
				+ astroObjectType + ", astroObjectCoordinates=" + astroObjectCoordinates + ", planetariumUrl="
				+ planetariumUrl + ", wiskySkyUrl=" + wiskySkyUrl + ", imageUrl=" + imageUrl + "]";
	}
	
}
