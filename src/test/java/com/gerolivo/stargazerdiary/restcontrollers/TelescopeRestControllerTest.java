package com.gerolivo.stargazerdiary.restcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerolivo.stargazerdiary.domain.Stargazer;
import com.gerolivo.stargazerdiary.domain.Telescope;
import com.gerolivo.stargazerdiary.domain.TelescopeType;
import com.gerolivo.stargazerdiary.repositories.StargazerRepository;
import com.gerolivo.stargazerdiary.services.SkyObservationService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TelescopeRestController.class/*, secure = false*/)
public class TelescopeRestControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
    private SkyObservationService skyObservationService;
	
	@MockBean
    private StargazerRepository stargazerRepository;
	
	@Autowired
	private WebApplicationContext context;
	
	Stargazer stargazer;
	Telescope telescope1;
	Telescope telescope3;
	
	@Before
	public void setup() {
//		mockMvc = MockMvcBuilders
//				.webAppContextSetup(context)
//				.apply(springSecurity())
//				.build();
		
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
	
	@Test
	@WithMockUser //(username = "user", password = "password", roles = "USER")
	public void testGetTelescopeList() throws Exception {

		Mockito.when(this.stargazerRepository.findByUserName("user"))
			.thenReturn(stargazer);

		MvcResult result = mockMvc.perform(get("/api/v1/telescopes/telescopeList")
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
      String expected="[ {\n" + 
      		"  \"id\" : null,\n" + 
      		"  \"name\" : \"Tele1\",\n" + 
      		"  \"maker\" : \"Maker1\",\n" + 
      		"  \"model\" : \"Model1\",\n" + 
      		"  \"features\" : \"Here are some features1\",\n" + 
      		"  \"telescopeType\" : \"CATADIOPTRIC\"\n" + 
      		"}, {\n" + 
      		"  \"id\" : null,\n" + 
      		"  \"name\" : \"Tele2\",\n" + 
      		"  \"maker\" : \"Maker2\",\n" + 
      		"  \"model\" : \"Model2\",\n" + 
      		"  \"features\" : \"Here are some features2\",\n" + 
      		"  \"telescopeType\" : \"DOBSON\"\n" + 
      		"} ]";
      
      assertEquals(expected, result.getResponse().getContentAsString());
      
      JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);

	}

	@Test
	@WithMockUser
	public void testAddTelescope() throws Exception {
		telescope3.setId((long) 1);
		ArgumentCaptor<Telescope> argument = ArgumentCaptor.forClass(Telescope.class);
		
		mockMvc.perform(post("/api/v1/telescopes/saveTelescope")
				.content(getObjectasJsonString(telescope3))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("Telescope successfuly saved"));
		verify(skyObservationService, times(1)).addorUpdateTelescope(Mockito.any());
		
		verify(skyObservationService).addorUpdateTelescope(argument.capture());
		assertEquals(telescope3.getId(), argument.getValue().getId());
		assertEquals(telescope3.getName(), argument.getValue().getName());
		assertEquals(telescope3.getMaker(), argument.getValue().getMaker());
		assertEquals(telescope3.getModel(), argument.getValue().getModel());
		assertEquals(telescope3.getFeatures(), argument.getValue().getFeatures());
		assertEquals(telescope3.getTelescopeType(), argument.getValue().getTelescopeType());
		
	}
	
	@Test
	@WithMockUser
	public void testEditTelescopeShouldReturnUnauthorizedToAdd() throws Exception {
		telescope3.setId((long) 3);
		Mockito.when(this.stargazerRepository.findByUserName("user"))
		.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope((long) 3))
		.thenReturn(telescope3);

		mockMvc.perform(post("/api/v1/telescopes/saveTelescope")
				.content(getObjectasJsonString(telescope3))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().string("You are not authorized to update this telescope with id: 3"));

	}

	@Test
	@WithMockUser
	public void testGetTelescope() throws Exception {
		Mockito.when(this.stargazerRepository.findByUserName("user"))
		.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(Mockito.any()))
		.thenReturn(telescope1);
		
		MvcResult result = mockMvc.perform(get("/api/v1/telescopes/get/1")
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
      String expected="{\n" + 
      		"  \"id\" : null,\n" + 
      		"  \"name\" : \"Tele1\",\n" + 
      		"  \"maker\" : \"Maker1\",\n" + 
      		"  \"model\" : \"Model1\",\n" + 
      		"  \"features\" : \"Here are some features1\",\n" + 
      		"  \"telescopeType\" : \"CATADIOPTRIC\"\n" + 
      		"}";
      
      assertEquals(expected, result.getResponse().getContentAsString());
      
      JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
      System.out.println("Response: " + result.getResponse().getContentAsString());
	}
	
	@Test
	@WithMockUser
	public void testGetTelescopeShouldReturnUnauthorizedToGet() throws Exception {
		Mockito.when(this.stargazerRepository.findByUserName("user"))
		.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(Mockito.any()))
		.thenReturn(telescope3);
		
		mockMvc.perform(get(
				"/api/v1/telescopes/get/1").accept(
				MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().string("You are not authorized to get this telescope with id: 1"));
	}

	@Test
	@WithMockUser
	public void testDeleteTelescope() throws Exception {
		telescope3.setId((long) 1);
		ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
		
		Mockito.when(this.stargazerRepository.findByUserName("user"))
			.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(Mockito.any()))
			.thenReturn(telescope1);
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
				"/api/v1/telescopes/delete/1").accept(
				MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("Telescope successfuly deleted"));
		
		verify(skyObservationService, times(1)).deleteTelescope((long) 1);
		verify(skyObservationService).deleteTelescope(argument.capture());
		assertThat(argument.getValue()).isEqualTo(1);

	}
	
	@Test
	@WithMockUser
	public void testDeleteTelescopeShouldReturnUnauthorizedToDelete() throws Exception {
		Mockito.when(this.stargazerRepository.findByUserName("user"))
			.thenReturn(stargazer);
		Mockito.when(skyObservationService.getTelescope(Mockito.any()))
			.thenReturn(telescope3);
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
			.delete("/api/v1/telescopes/delete/1")
			.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().string("You are not authorized to delete this telescope with id: 1"));
	}
	
	private static String getObjectasJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}  

}
