package com.spt.evt.controller;

import com.spt.evt.entity.Course;
import com.spt.evt.entity.ScoreBoard;
import com.spt.evt.entity.Subject;
import com.spt.evt.entity.Topic;
import com.spt.evt.service.CourseService;
import com.spt.evt.service.ScoreBoardService;
import com.spt.evt.service.SubjectService;
import com.spt.evt.service.TopicService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

@Controller
public class ManagerCourseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private CourseService courseService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private ScoreBoardService  scoreBoardService;

	@RequestMapping(value="/managerCourse",method=RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String yourId = request.getParameter("yourId");
		String name = request.getParameter("yourName");
		String lastName = request.getParameter("yourLastName");
		String yourPosition = request.getParameter("yourPosition");
		JSONObject allCourse = this.courseService.getAllCourse();
		Map model = new HashMap();
		model.put("yourId", yourId);
		model.put("name", name);
		model.put("lastName", lastName);
		model.put("yourPosition", yourPosition);
		model.put("allCourse", allCourse.toString());
		return new ModelAndView("managerCourse",model);
	}

	@RequestMapping(value="/managerCourseShow",method=RequestMethod.GET)
	public ModelAndView handleGetRequestShow(HttpServletRequest request,
										 HttpServletResponse response) throws Exception {
		String yourId = request.getParameter("yourId");
		String name = request.getParameter("yourName");
		String lastName = request.getParameter("yourLastName");
		String yourPosition = request.getParameter("yourPosition");
		JSONObject allCourse = this.courseService.getAllCourse();
		Map model = new HashMap();
		model.put("yourId", yourId);
		model.put("name", name);
		model.put("lastName", lastName);
		model.put("yourPosition", yourPosition);
		model.put("allCourse", allCourse.toString());
		return new ModelAndView("managerCourseShow",model);
	}

	@RequestMapping(value="/showDetail", method = RequestMethod.POST)
	public @ResponseBody String showDetail(@RequestParam(value="dataForm") String dataForm) {
		JSONObject courseInformation = new JSONObject();
		JSONObject allFi = new JSONObject();
		JSONObject topicElement = null;
		JSONObject idJsonDelete = new JSONObject(dataForm);
		Long passToLong = idJsonDelete.getLong("id");
		Course course = courseService.findById(passToLong);
		List<Subject> subjects = subjectService.findByCourse(course);

		JSONObject subjectElement = null;
		for (Subject subject : subjects) {
			subjectElement = new JSONObject();
			subjectElement.put("nameSubject", subject.getName());
			subjectElement.put("id", subject.getId());
			List<Topic> topics = topicService.findBySubject(subject);
			for (Topic topic:topics){
				topicElement = new JSONObject();
				topicElement.put("nameTopic",topic.getName()+" : "+topic.getDescription() );
				subjectElement.append("topicPack", topicElement);
			}
			courseInformation.append("subject", subjectElement);

		}

		return courseInformation.toString();
	}

	@RequestMapping(value="/saveCourse", method = RequestMethod.POST)
	public @ResponseBody String saveCourse(@RequestParam(value="dataForm") String dataForm) {
		courseService.setData(dataForm);
		return "Seccessful";
	}

	@RequestMapping(value="/deleteCourse", method = RequestMethod.POST)
	public @ResponseBody String deleteCourse(@RequestParam(value="dataForm") String dataForm) {
		String returnMessage;
		JSONObject idJsonDelete = new JSONObject(dataForm);
		Long passToLong = Long.parseLong(idJsonDelete.getString("id"));
	
		Course course = courseService.findById(passToLong);
		List<Subject> ss = subjectService.findByCourse(course);

		if (ss.isEmpty()) {
			courseService.deleteDataById(passToLong);
			returnMessage = "1";
		}else{
			returnMessage = "0";
		}
		return returnMessage;
	}

	@RequestMapping(value="/editCourse", method = RequestMethod.POST)
	public @ResponseBody String editCourse(@RequestParam(value="dataForm") String dataForm) {
		courseService.editData(dataForm);
		return "Seccessful";
	}

	@RequestMapping(value="/saveSubject", method = RequestMethod.POST)
	public @ResponseBody String saveSubject(@RequestParam(value="dataForm") String dataForm) {
		subjectService.setData(dataForm);
		
		return "Seccessful";
	}

	@RequestMapping(value="/deleteSubject", method = RequestMethod.POST)
	public @ResponseBody String deleteSubject(@RequestParam(value="dataForm") String dataForm) {
		String returnMessage;
		JSONObject idJsonDelete = new JSONObject(dataForm);
		Long passToLong = Long.parseLong(idJsonDelete.getString("id"));	
		Subject subject = subjectService.findById(passToLong);
		List<Topic> tp = topicService.findBySubject(subject);

		if (tp.isEmpty()) {
			subjectService.deleteDataById(passToLong);
			returnMessage = "1";
		}else{
			returnMessage = "0";
		}
		return returnMessage;
	}

	@RequestMapping(value="/editSubject", method = RequestMethod.POST)
	public @ResponseBody String editSubject(@RequestParam(value="dataForm") String dataForm) {
		subjectService.editData(dataForm);

		return "Seccessful";
	}

	@RequestMapping(value="/saveTopic", method = RequestMethod.POST)
	public @ResponseBody String saveTopic(@RequestParam(value="dataForm") String dataForm) {
		topicService.setData(dataForm);
		return "Seccessful";
	}

	@RequestMapping(value="/deleteTopic", method = RequestMethod.POST)
	public @ResponseBody String deleteTopic(@RequestParam(value="dataForm") String dataForm) {
		String returnMessage;
		JSONObject idJsonDelete = new JSONObject(dataForm);
		Long passToLong = Long.parseLong(idJsonDelete.getString("id"));
		Topic topic = topicService.findById(passToLong);
		List<ScoreBoard> sb = scoreBoardService.findByTopic(topic);
		if (sb.isEmpty()) {
			topicService.deleteDataById(passToLong);
			returnMessage = "1";
		}else{
			returnMessage = "0";
		}
		
		return returnMessage;
	}

	@RequestMapping(value="/editTopic", method = RequestMethod.POST)
	public @ResponseBody String editTopic(@RequestParam(value="dataForm") String dataForm) {
		topicService.editData(dataForm);

		return "Seccessful";
	}

	@RequestMapping(value="/courseGetSubject", method = RequestMethod.POST)
	public @ResponseBody String courseGetSubject(@RequestParam(value="dataForm") String dataForm) {
		JSONObject idJsonDelete = new JSONObject(dataForm);		
		Long passToLong = Long.parseLong(idJsonDelete.getString("id"));
		Course course = courseService.findById(passToLong);
		List<Subject> listAllSubject = subjectService.findByCourse(course);

		JSONObject report = new JSONObject();
		for (Subject it : listAllSubject) {
			JSONObject listAfterLoop = new JSONObject();
			listAfterLoop.put("subjectName",it.getName());
			listAfterLoop.put("subjectid",it.getId());
			report.append("data",listAfterLoop);			
		}

		return report.toString();
	}

	

	@RequestMapping(value="/subjectGetTopic", method = RequestMethod.POST)
	public @ResponseBody String subjectGetTopic(@RequestParam(value="dataForm") String dataForm) {
		JSONObject idJsonDelete = new JSONObject(dataForm);		
		Long passToLong = Long.parseLong(idJsonDelete.getString("id"));
		Subject subject = subjectService.findById(passToLong);
		List<Topic> listAllTopic = topicService.findBySubject(subject);

		JSONObject report = new JSONObject();
		for (Topic it : listAllTopic) {
			JSONObject listAfterLoop = new JSONObject();
			listAfterLoop.put("topicName",it.getName());
			listAfterLoop.put("topicid",it.getId());
			report.append("data",listAfterLoop);
		}
		return report.toString();
	}

	@RequestMapping(value="/courseGetDetail", method = RequestMethod.POST)
	public @ResponseBody String courseGetDetail(@RequestParam(value="dataForm") String dataForm) {
		JSONObject idJsonDelete = new JSONObject(dataForm);
		Long passToLong = Long.parseLong(idJsonDelete.getString("id"));
		Course course = courseService.findById(passToLong);

		JSONObject report = new JSONObject();
			JSONObject setKeyAfterGet = new JSONObject();
			setKeyAfterGet.put("courseName",course.getName());
			setKeyAfterGet.put("courseDescription",course.getDescription());
			report.append("data",setKeyAfterGet);
		return report.toString();
	}

	@RequestMapping(value="/subjectGetDetail", method = RequestMethod.POST)
	public @ResponseBody String subjectGetDetail(@RequestParam(value="dataForm") String dataForm) {
		JSONObject idJsonDelete = new JSONObject(dataForm);
		JSONObject report = new JSONObject();
		if (!(idJsonDelete.toString().equals("{\"id\":null}"))) {
			String passToLong = (String) idJsonDelete.get("id");
			Subject subject = subjectService.findById(Long.parseLong(passToLong));
			JSONObject setKeyAfterGet = new JSONObject();
			setKeyAfterGet.put("subjectName", subject.getName());
			setKeyAfterGet.put("subjectDescription", subject.getDescription());
			report.append("data", setKeyAfterGet);
		}
		return report.toString();
	}

	@RequestMapping(value="/topicGetDetail", method = RequestMethod.POST)
	public @ResponseBody String topicGetDetail(@RequestParam(value="dataForm") String dataForm) {
		JSONObject idJsonDelete = new JSONObject(dataForm);
		Long passToLong = Long.parseLong(idJsonDelete.getString("id"));
		Topic topic = topicService.findById(passToLong);

		JSONObject report = new JSONObject();
		JSONObject setKeyAfterGet = new JSONObject();
		setKeyAfterGet.put("topicName",topic.getName());
		setKeyAfterGet.put("topicDescription",topic.getDescription());
		report.append("data",setKeyAfterGet);

		return report.toString();
	}

	@RequestMapping(value="/courseGetAll", method = RequestMethod.POST)
	public @ResponseBody String courseGetAll(@RequestParam(value="dataForm") String dataForm) {
		JSONObject allCourse = this.courseService.getAllCourse();
		return allCourse.toString();
	}

	@RequestMapping(value="/sendCourse", method = RequestMethod.POST)
	public @ResponseBody String sendDescription() {
		JSONObject nameLarge = new JSONObject();
		JSONObject nameSmall = null;
		List<Course> result = this.courseService.findAll();
		for (Course course : result){
			nameSmall = new JSONObject();
			nameSmall.put("idCourse", course.getId());
			nameSmall.put("descriptionCourse", course.getDescription());
			nameLarge.append("idAndDescription", nameSmall);
		}
		return nameLarge.toString();
	}
}
