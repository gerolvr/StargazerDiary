package com.gerolivo.stargazerdiary.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import com.gerolivo.stargazerdiary.astrodata.AstroObjectData;
import com.gerolivo.stargazerdiary.astrodata.Dec;
import com.gerolivo.stargazerdiary.astrodata.RightAsention;


@RunWith(SpringRunner.class)
@RestClientTest({ AstroDataService.class})
public class AstroDataServiceImplTest {

	@Autowired
	private MockRestServiceServer mockServer;
	
	@Autowired
	private AstroDataService astroDataService;

	@Test
	public void testGetAstroDataForObjectName() {
		this.mockServer.expect(requestTo("http://www.strudel.org.uk/lookUP/json/?name=M101"))
		.andRespond(withSuccess(getClassPathResource(),
				MediaType.APPLICATION_JSON));
		AstroObjectData astroDataForObjectName = astroDataService.getAstroDataForObjectName("M101");
		assertThat(astroDataForObjectName.getCategory().getAvmdesc()).isEqualTo("Interacting Galaxies");
		
		assertThat(astroDataForObjectName.getRa().getDecimal()).isEqualTo("210.8024292");
		assertThat(astroDataForObjectName.getRa().getH()).isEqualTo("14");
		assertThat(astroDataForObjectName.getRa().getM()).isEqualTo("03");
		assertThat(astroDataForObjectName.getRa().getS()).isEqualTo("12.583");
		
		assertThat(astroDataForObjectName.getDec().getDecimal()).isEqualTo("54.3487500");
		assertThat(astroDataForObjectName.getDec().getD()).isEqualTo("+54");
		assertThat(astroDataForObjectName.getDec().getM()).isEqualTo("20");
		assertThat(astroDataForObjectName.getDec().getS()).isEqualTo("55.500");
		
		assertThat(astroDataForObjectName.getImage().getSrc()).isEqualTo("http://server7.sky-map.org/imgcut?survey=DSS2&w=256&h=256&ra=14.05349528&de=54.3487500&angle=1.25&output=PNG");
		
		assertThat(astroDataForObjectName.getTarget().getName()).isEqualTo("M101");
	}

	@Test
	public void testGenerateCoordinate() {
		AstroObjectData astroObjectData = new AstroObjectData();
		//M101
		RightAsention ra = new RightAsention();
		ra.setDecimal("210.8024292");
		ra.setH("14");
		ra.setM("03");
		ra.setS("12.583");
		Dec dec = new Dec();
		dec.setDecimal("54.3487500");
		dec.setD("+54");
		dec.setM("20");
		dec.setS("55.500");
		astroObjectData.setDec(dec);
		astroObjectData.setRa(ra);
		StringBuilder generatedCoordinate = astroDataService.generateCoordinate(astroObjectData);
		assertThat(generatedCoordinate.toString()).isEqualTo("Coordinates:" +
				" Right ascension= 210.8024292 / 14:03:12.583" + 
				", Declination= 54.3487500 / +54:20:55.500");
		
		}
	
	private ClassPathResource getClassPathResource() {
		return new ClassPathResource("com.gerolivo.stargazerdiary.services/astroObjectDetails.json");
	}

}
