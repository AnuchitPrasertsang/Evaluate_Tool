package com.spt.evt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spt.evt.entity.Room;
import com.spt.evt.service.ReportService;

@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;

	@RequestMapping(value="/report",method=RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		JSONObject roomInformation = this.reportService.findByStatus();
		Map model = new HashMap();
		model.put("room", roomInformation.toString());

		return new ModelAndView("report",model);
	}

	@RequestMapping(value="/getroomscore",method=RequestMethod.POST)
	public @ResponseBody String getRoomScore(@RequestParam(value="dataRoomId") String dataRoomId ,HttpServletRequest arg0,HttpServletResponse arg1) {
		JSONObject roomDetail = new JSONObject(dataRoomId);
		Long roomId		 	= Long.parseLong(roomDetail.getString("roomId"));
		JSONObject roomScore = this.reportService.getScoreOfRoom(roomId);

		return roomScore.toString();
	}

}
