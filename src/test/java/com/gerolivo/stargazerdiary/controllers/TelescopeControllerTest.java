package com.gerolivo.stargazerdiary.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.domain.Telescope;
import com.gerolivo.stargazerdiary.domain.TelescopeType;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;
import com.gerolivo.stargazerdiary.services.SkyObservationService;

/**
 * Tests for {@link }TelescopeController}
 * @author gerolvr
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TelescopeController.class)
@ContextConfiguration
public class TelescopeControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
    private SkyObservationService skyObservationService;
	
	@MockBean
    private StargazerRepository stargazerRepository;
	
	@Autowired
	private WebDriver webDriver;
	
	Stargazer stargazer;
	Telescope telescope1;
	Telescope telescope3;
	
	@Before
	public void setup() {
		
		stargazer = new Stargazer("user", "password");
		telescope1 = new Telescope("Tele1", "Maker1", "Model1", "Here are some features1", TelescopeType.CATADIOPTRIC);
		Telescope mockTelescope2 = new Telescope("Tele2", "Maker2", "Model2", "Here are some features2", TelescopeType.DOBSON);
		telescope3 = new Telescope("Tele3", "Maker3", "Model3", "Here are some features3", TelescopeType.DOBSON);
		
		stargazer.addTelescope(telescope1);
		stargazer.addTelescope(mockTelescope2);
				
	}
	
	 
	
    @Test
    public void shouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isUnauthorized());
    }
    
    /**
     * {@link }{@link TelescopeController#getTelescopesList(org.springframework.ui.Model, org.springframework.security.core.userdetails.User)}
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void shouldReturnTelescopeList() throws Exception {
    	given(this.stargazerRepository.findByUserName("user"))
			.willReturn(stargazer);
    	
//    	mockMvc.perform(get("/telescopes/telescopeList"))
//		.andDo(print());
        
        this.webDriver.get("/telescopes/telescopeList");

        List<WebElement> webElements = webDriver.findElements(By.xpath("//table//tr"));
        // Header row + 2 telescopes
        assertThat(webElements.size()).isEqualTo(3);
        
        webElements = webDriver.findElements(By.xpath("//h1[text()='My telescopes']//following-sibling::div//tr[2]/td"));
        assertThat(webElements.size()).isEqualTo(6);
		assertThat(webElements.get(0).getText()).isEqualTo("Tele1");
		assertThat(webElements.get(1).getText()).isEqualTo("Maker1");
		assertThat(webElements.get(2).getText()).isEqualTo("Model1");
		assertThat(webElements.get(3).getText()).isEqualTo("Here are some features1");
		assertThat(webElements.get(4).getText()).isEqualTo(TelescopeType.CATADIOPTRIC.toString());
		List<WebElement> webElementsEditDelete = webElements.get(5).findElements(By.xpath(".//a"));
		assertThat(webElementsEditDelete.size()).isEqualTo(2);
		assertThat(webElementsEditDelete.get(0).getText()).isEqualTo("Edit");
		assertThat(webElementsEditDelete.get(1).getText()).isEqualTo("Delete");
		
		webElements = webDriver.findElements(By.xpath("//h1[text()='My telescopes']//following-sibling::div//tr[3]/td"));
		assertThat(webElements.size()).isEqualTo(6);
		assertThat(webElements.get(0).getText()).isEqualTo("Tele2");
		assertThat(webElements.get(1).getText()).isEqualTo("Maker2");
		assertThat(webElements.get(2).getText()).isEqualTo("Model2");
		assertThat(webElements.get(3).getText()).isEqualTo("Here are some features2");
		assertThat(webElements.get(4).getText()).isEqualTo(TelescopeType.DOBSON.toString());
		webElementsEditDelete = webElements.get(5).findElements(By.xpath(".//a"));
		assertThat(webElementsEditDelete.size()).isEqualTo(2);
		assertThat(webElementsEditDelete.get(0).getText()).isEqualTo("Edit");
		assertThat(webElementsEditDelete.get(1).getText()).isEqualTo("Delete");
    }
    
	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	public void shouldReturnAddTelescopeForm() {
		this.webDriver.get("/telescopes/add");
		WebElement formWebElement = webDriver.findElement(By.name("telescopeForm"));
		List<WebElement> webElements = formWebElement.findElements(By.className("form-group"));
		assertThat(webElements.size()).isEqualTo(6);
		
		assertThat(webElements.get(0).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Name *");
		assertThat(webElements.get(0).findElement(By.id("inputTelescopeName")).getTagName()).isEqualTo("input");
		
		assertThat(webElements.get(1).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Maker");
		assertThat(webElements.get(1).findElement(By.id("inputTelescopeMaker")).getTagName()).isEqualTo("input");
		
		assertThat(webElements.get(2).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Model");
		assertThat(webElements.get(2).findElement(By.id("inputTelescopeModel")).getTagName()).isEqualTo("input");
		
		assertThat(webElements.get(3).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Features");
		assertThat(webElements.get(3).findElement(By.id("features")).getTagName()).isEqualTo("textarea");
		
		assertThat(webElements.get(4).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Type");
		assertThat(webElements.get(4).findElement(By.id("telescopeType")).getTagName()).isEqualTo("select");
		assertThat(webElements.get(4).findElement(By.id("telescopeType")).findElements(By.tagName("option")).size()).isEqualTo(4);
	
	}
	
	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	public void shouldReturnEditTelescopeForm() {
		Mockito.when(this.stargazerRepository.findByUserName("user"))
		.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(Mockito.any()))
		.thenReturn(telescope1);
		
//		MvcResult result = mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk()).andReturn();//andReturn();
		this.webDriver.get("/telescopes/edit/1");
		WebElement formWebElement = webDriver.findElement(By.name("telescopeForm"));
		List<WebElement> webElements = formWebElement.findElements(By.className("form-group"));
		assertThat(webElements.size()).isEqualTo(6);
		
		assertThat(webElements.get(0).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Name *");
		assertThat(webElements.get(0).findElement(By.id("inputTelescopeName")).getTagName()).isEqualTo("input");
		assertThat(webElements.get(0).findElement(By.id("inputTelescopeName")).getAttribute("value")).isEqualTo(telescope1.getName());
		
		assertThat(webElements.get(1).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Maker");
		assertThat(webElements.get(1).findElement(By.id("inputTelescopeMaker")).getTagName()).isEqualTo("input");
		assertThat(webElements.get(1).findElement(By.id("inputTelescopeMaker")).getAttribute("value")).isEqualTo(telescope1.getMaker());
		
		assertThat(webElements.get(2).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Model");
		assertThat(webElements.get(2).findElement(By.id("inputTelescopeModel")).getTagName()).isEqualTo("input");
		assertThat(webElements.get(2).findElement(By.id("inputTelescopeModel")).getAttribute("value")).isEqualTo(telescope1.getModel());
		
		assertThat(webElements.get(3).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Features");
		assertThat(webElements.get(3).findElement(By.id("features")).getTagName()).isEqualTo("textarea");
		assertThat(webElements.get(3).findElement(By.id("features")).getAttribute("value")).isEqualTo(telescope1.getFeatures());
		
		assertThat(webElements.get(4).findElement(By.tagName("label")).getText()).isEqualTo("Telescope Type");
		assertThat(webElements.get(4).findElement(By.id("telescopeType")).getTagName()).isEqualTo("select");
		assertThat(webElements.get(4).findElement(By.id("telescopeType")).findElements(By.tagName("option")).size()).isEqualTo(4);
		assertThat(webElements.get(4).findElement(By.id("telescopeType")).findElement(By.xpath("//option[@selected='selected']")).getText()).isEqualTo(telescope1.getTelescopeType().toString());
		
	}
	
	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	public void shouldReturnUnauthorizedWhenEditTelescope() throws Exception {
		
		Mockito.when(this.stargazerRepository.findByUserName("user"))
			.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(Mockito.any()))
			.thenReturn(telescope3);
		
		mockMvc.perform(get("/telescopes/edit/1"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	public void shouldDeleteTelescopeThenReturn302Redirection() throws Exception {
		
		ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
		
		Mockito.when(this.stargazerRepository.findByUserName("user"))
			.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(new Long(1)))
			.thenReturn(telescope1);
		
		mockMvc.perform(get("/telescopes/delete/1"))
			.andDo(print())
			.andExpect(status().isFound());
		
		verify(skyObservationService, times(1)).deleteTelescope(new Long(1));
		verify(skyObservationService).deleteTelescope(argument.capture());
		assertThat(argument.getValue()).isEqualTo(1);
	}
	
	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	public void shouldDeleteTelescopeThenReturnUpdatedList() throws Exception {
		
		ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
		
		Mockito.when(this.stargazerRepository.findByUserName("user"))
			.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(new Long(1)))
			.thenReturn(telescope1);
		
		webDriver.get("/telescopes/delete/1");
		
        List<WebElement> webElements = webDriver.findElements(By.xpath("//table//tr"));
        // Header row + 1 telescopes
        assertThat(webElements.size()).isEqualTo(2);
		
		webElements = webDriver.findElements(By.xpath("//table//tr[2]/td[1]"));
		assertThat(webElements.get(0).getText()).isEqualTo("Tele2");
		
		verify(skyObservationService, times(1)).deleteTelescope(new Long(1));
		verify(skyObservationService).deleteTelescope(argument.capture());
		assertThat(argument.getValue()).isEqualTo(1);
	}

	
	@Test
	@WithMockUser(username = "user", password = "password", roles = "USER")
	public void shouldReturnUnauthorizedWhenDelteTelescope() throws Exception {
		Mockito.when(this.stargazerRepository.findByUserName("user"))
			.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(Mockito.any()))
			.thenReturn(telescope3);
		
		mockMvc.perform(get("/telescopes/delete/1"))
			.andDo(print())
			.andExpect(status().isUnauthorized());

	}

}
