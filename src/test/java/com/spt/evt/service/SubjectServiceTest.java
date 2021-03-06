package com.spt.evt.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spt.evt.dao.SubjectDao;
import com.spt.evt.entity.Course;
import com.spt.evt.entity.Subject;

/**
 * Created by : Anuchit Prasertsang Created Date : 28/10/2014
 */
public class SubjectServiceTest extends AbstractTestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubjectServiceTest.class);

	@Autowired
	private SubjectDao subjectDao;
	
	@Test
	public void testFindSubjectByCourseShouldBeNotNull() {
		Course course = new Course();
		course.setId(1L);
		List<Subject> subjects = this.subjectDao.findByCourse(course);
		Assert.assertNotNull(subjects);
	}

}
