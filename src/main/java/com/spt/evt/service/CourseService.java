package com.spt.evt.service;

import com.spt.evt.entity.Course;
import org.json.JSONObject;


public interface CourseService {
	public Course findById(Long id);
	public void setData(String dataForm);
	public JSONObject getAllCourse();
	public void deleteDataById(Long id);
	public void editData(String dataForm);
}
