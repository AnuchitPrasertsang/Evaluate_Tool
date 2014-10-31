package com.spt.evt.service;

import org.json.JSONObject;

public interface EvaluateBoardService {
	public JSONObject getCourseInformation(Long examinerId,Long committeeId,Long courseId);
	public String	scoring(Long committeeId,Long examinerId,Long topicId,Double score,String comment);
}

