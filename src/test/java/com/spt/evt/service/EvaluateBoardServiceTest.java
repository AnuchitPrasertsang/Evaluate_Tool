package com.spt.evt.service;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by : Anuchit Prasertsang Created Date : 28/10/2014
 */
public class EvaluateBoardServiceTest extends AbstractTestService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EvaluateBoardService committeeService;

	public EvaluateBoardService getCommitteeService() {
		return committeeService;
	}

	public void setCommitteeService(EvaluateBoardService committeeService) {
		this.committeeService = committeeService;
	}
	

	@Test
	public void remark() {
		logger.debug("-= Begin test CommitteeServiceTest() =-");
	}

	@Test
	public void testCourseInformationShouldBeJsonOfCourse() {
		Long examinerId = 2L;
		Long committeeId = 3L;
		Long courseId = 1L;
		JSONObject courseInformation = this.getCommitteeService().getCourseInformation(examinerId, committeeId, courseId);
		Assert.assertNotNull(courseInformation.get("subject"));
		Assert.assertTrue(0!=courseInformation.length());
		//		Assert.assertThat("\"\"", is("\"\""));
	}

}