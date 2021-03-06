package com.spt.evt.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.json.JSONObject;

import com.spt.evt.dao.*;
import com.spt.evt.entity.Course;
import com.spt.evt.entity.Subject;
import com.spt.evt.service.*;

import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectServiceImpl implements SubjectService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SubjectServiceImpl.class);

	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private CourseService courseService;

	@Override
	public List<Subject> findByCourse(Course course) {
		return this.subjectDao.findByCourse(course);
	}

	@Override
	@Transactional
	public void setData(String dataForm) {
		JSONObject jsonObj = new JSONObject(dataForm);
		Subject subject = new Subject();
		Long passIdToLong = Long.parseLong(jsonObj.getString("id"));
		Course course = courseService.findById(passIdToLong);
		subject.setName(jsonObj.getString("subjectName"));
		subject.setDescription(jsonObj.getString("subjectDescription"));
		subject.setCourse(course);	
		subjectDao.persist(subject);
	}

	@Override
	public Subject findById(Long id) {
		return this.subjectDao.findById(id);
	}

	@Override
	@Transactional
	public void deleteDataById(Long id) {
		Subject subject = findById(id);
		this.subjectDao.removeSubject(subject);
	}

	@Override
	@Transactional
	public void editData(String dataForm) {
		JSONObject jsonObj = new JSONObject(dataForm);
		Subject subject = new Subject();
		Long passIdToLong = Long.parseLong(jsonObj.getString("idCourse"));
		Course course = courseService.findById(passIdToLong);
		subject.setId(Long.parseLong(jsonObj.getString("idSubject")));
		subject.setName(jsonObj.getString("subjectNameEdit"));
		subject.setDescription(jsonObj.getString("subjectDescriptionEdit"));
		subject.setCourse(course);
		subjectDao.update(subject);
	}
}