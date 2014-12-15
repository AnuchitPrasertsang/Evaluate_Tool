package com.spt.evt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spt.evt.entity.Person;
import com.spt.evt.service.PersonService;
import com.spt.evt.entity.MemberRegister;

@Controller
public class MemberRegisterController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberRegisterController.class);
	
	@Autowired
	private PersonService personService;

	@RequestMapping(value="/memberRegister",method=RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		return new ModelAndView("memberRegister"); // ชื่อของ tile ที่เรา definition ในไฟล์ tiles.xml

	}
	
	@RequestMapping(value="/memberRegister", method = RequestMethod.POST)
	public @ResponseBody String register(@RequestParam(value="dataForm") String dataForm) {
		personService.setData(dataForm);
		System.out.println("DATA"+dataForm);
		List<Person> result = this.personService.findAll();
		logger.info("Result  ::: " + result.toString());
		
		return "Seccessful";
	}
	
	@RequestMapping(value="/memberLogIn", method = RequestMethod.POST)
	public @ResponseBody String login(@RequestParam(value="data") String data) {
		
		System.out.println("DATA"+data);
		
		return "Seccessful";
	}

}
