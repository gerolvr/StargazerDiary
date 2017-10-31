package com.gerolivo.stargazerdiary.services;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gerolivo.stargazerdiary.astrodata.AstroObjectData;

@Service
public class AstroDataServiceImpl implements AstroDataService{

	Logger log = Logger.getLogger(AstroDataServiceImpl.class);
	
	private final RestTemplate restTemplate;

	public AstroDataServiceImpl(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	/**
	 * Returns a {@link AstroObjectData} object that contains the data of an astronomical object.
	 * Consume a rest service to retrieve these data.
	 * @param  astronomicalObjectName the name of the of the astronomical object to search
	 * @return  the SkyObjectValue for the specified astronomical object name
	 * @see AstroObjectData
	 */
	@Override
	@Cacheable("astroObjectData")
	public AstroObjectData getAstroDataForObjectName(String astronomicalObjectName) {
		
		// Customize the Jackson converter because the REST service responds
		// with a "text/javascript" header content, rather than the usual "application/json"
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		ArrayList<MediaType> arrayList = new ArrayList<MediaType>(Arrays.asList(
				new MediaType("application", "json"),
				new MediaType("text", "javascript")));
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(arrayList);
		restTemplate.setMessageConverters(
				new ArrayList<HttpMessageConverter<?>>(Arrays.asList(mappingJackson2HttpMessageConverter)));
		
		AstroObjectData skyObjectValue = restTemplate.getForObject(
				"http://www.strudel.org.uk/lookUP/json/?name=" + astronomicalObjectName, AstroObjectData.class);
		log.debug(skyObjectValue.toString());

		return skyObjectValue;
	}

	/**
	 * Returns a StringBuilder object that contains an url to a planetarium
	 * view for the given the {@link AstroObjectData}.
	 * @param  astroObjectData the object for which the url will be generated
	 * @return  the planetarium url for the specified astronomical object
	 * @see AstroObjectData
	 */
	@Override
	public StringBuilder generatePlanetariumIFrameUrl(AstroObjectData astroObjectData) {
		StringBuilder stringBuilderUrl = new StringBuilder();
		stringBuilderUrl.append("http://lcogt.net/virtualsky/embed/?projection=gnomic&");
		stringBuilderUrl.append("ra=" + astroObjectData.getRa().getDecimal() + "&");
		stringBuilderUrl.append("dec=" + astroObjectData.getDec().getDecimal() + "&");
		stringBuilderUrl.append(
				"showdate=true&showposition=false&constellationlabels=true&constellations=true&constellationboundaries=true&fov=45&showgalaxy=true&");
		stringBuilderUrl.append("objects=" + astroObjectData.getTarget().getName());
		
		return stringBuilderUrl;
	}
	
	/**
	 * Returns a StringBuilder object that contains an url to an optical
	 * view for the given the {@link AstroObjectData}.
	 * @param  astroObjectData the object for which the url will be generated
	 * @return  the optical url for the specified astronomical object
	 * @see AstroObjectData
	 */
	@Override
	public StringBuilder generateWikiskyIFrameUrl(AstroObjectData astroObjectData) {
		int raH, decD;
		int sign = 1;
		
		raH = new Integer(astroObjectData.getRa().getH());
		sign = raH < 0 ? -1 : 1;

		double ra = sign *
				(Math.abs(new Double(astroObjectData.getRa().getH()))
						+ (new Double(astroObjectData.getRa().getM()) / 60.0)
						+ (new Double(astroObjectData.getRa().getS()) / 3600.0));
		
		decD = new Integer(astroObjectData.getDec().getD());
		sign = decD < 0 ? -1 : 1;
		double dec = sign *
				(Math.abs(new Double(astroObjectData.getDec().getD()))
						+ (new Double(astroObjectData.getDec().getM()) / 60.0)
						+ (new Double(astroObjectData.getDec().getS()) / 3600.0));

		StringBuilder stringBuilderUrl = new StringBuilder();
		stringBuilderUrl.append("http://server1.wikisky.org/v2?");
		stringBuilderUrl.append("ra=" + ra + "&");
		stringBuilderUrl.append("de=" + dec + "&");
		stringBuilderUrl.append("zoom=6&img_source=DSS2");
		
		return stringBuilderUrl;
	}

	/**
	 * Returns a StringBuilder object that contains a text containing
	 * the coordinates of the given {@link AstroObjectData}
	 * i.e. Coordinates: Right ascension= 210.8024292 / 14:03:12.583, Declination= 54.3487500 / +54:20:55.500
	 * @param  astroObjectData the object for which the coordinates will be generated
	 * @return  the coordinates for the specified astronomical object
	 * @see AstroObjectData
	 */
	@Override
	public StringBuilder generateCoordinate(AstroObjectData astroObjectData) {
		StringBuilder stringBuilderCoordinates = new StringBuilder();
		stringBuilderCoordinates.append("Coordinates:");
		stringBuilderCoordinates.append(" Right ascension= " + astroObjectData.getRa().getDecimal() + " / " +
				astroObjectData.getRa().getH() + ":" + astroObjectData.getRa().getM() + ":" + astroObjectData.getRa().getS());
		stringBuilderCoordinates.append(", Declination= " + astroObjectData.getDec().getDecimal() + " / " +
				astroObjectData.getDec().getD() + ":" + astroObjectData.getDec().getM() + ":" + astroObjectData.getDec().getS());
		
		return stringBuilderCoordinates;
	}

}