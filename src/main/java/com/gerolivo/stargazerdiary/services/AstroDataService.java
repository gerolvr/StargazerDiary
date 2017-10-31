package com.gerolivo.stargazerdiary.services;

import com.gerolivo.stargazerdiary.astrodata.AstroObjectData;

public interface AstroDataService {

	AstroObjectData getAstroDataForObjectName(String astronomicalObjectName);
	StringBuilder generatePlanetariumIFrameUrl(AstroObjectData skyObjectValue);
	StringBuilder generateWikiskyIFrameUrl(AstroObjectData skyObjectValue);
	StringBuilder generateCoordinate(AstroObjectData skyObjectValue);
}
