package com.spt.evt.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
/**
 * Created by : Anuchit Prasertsang 
 * Created Date : 06/11/2014
 */
public class ExaminationRoomControllerTest extends TemplateTestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExaminationRoomControllerTest.class);
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void get_showEvaluateBoard() throws Exception {
		String yourId = "1";
		mockMvc.perform(get("/examinationRoom").param("yourId", yourId))
		.andExpect(status().isOk())
		.andExpect(view().name("examinationRoom"))
		.andExpect(forwardedUrl("/WEB-INF/layouts/master.jsp"));
	}  

	@Test
	public void jsonSample() throws Exception {
		LOGGER.debug("AAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
	}  


}