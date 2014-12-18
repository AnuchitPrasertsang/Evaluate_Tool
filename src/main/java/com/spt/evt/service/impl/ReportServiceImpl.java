package com.spt.evt.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.spt.evt.entity.*;

import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spt.evt.service.ReportService;

@Service
public class ReportServiceImpl extends ProviderService implements ReportService{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

	@Override
	public JSONObject getAllScore() {
		String roomStatus = "Completed";
		List<Room> rooms = this.getRoomService().findByStatus(roomStatus);						
		JSONObject reportNoSort = generateScoreJsonObjectByRoom(rooms);
		JSONArray reportNoSortArray = new JSONArray(""+reportNoSort.get("report")) ;
			
		TreeMap<String,List<JSONObject>> reportTreeMap = new TreeMap<String,List<JSONObject>>();
		for(int n = 0; n < reportNoSortArray.length(); n++)
			{
				if (reportTreeMap.containsKey(reportNoSortArray.getJSONObject(n).getString("examiner"))) {
					List<JSONObject> listObject = reportTreeMap.get(reportNoSortArray.getJSONObject(n).getString("examiner"));
					listObject.add(reportNoSortArray.getJSONObject(n));
					reportTreeMap.put(reportNoSortArray.getJSONObject(n).getString("examiner"),listObject);

				}
				else{
					List<JSONObject> listObject = new ArrayList<JSONObject>();
					listObject.add(reportNoSortArray.getJSONObject(n));
					reportTreeMap.put(reportNoSortArray.getJSONObject(n).getString("examiner"),listObject);
				}
			}
			
		JSONArray reportArray = new JSONArray() ;
		JSONObject report = new JSONObject();
		for(String key: reportTreeMap.keySet()) {
			for (JSONObject jsList : reportTreeMap.get(key)) {	
				reportArray.put(jsList);
			}
		}
		report.put("report",reportArray);	
		return report;
	}

	@Override
	public JSONObject getAllExaminerCompleted() {
		JSONObject report = new JSONObject();
		String roomStatus = "Completed";
		List<Room> rooms = this.getRoomService().findByStatus(roomStatus);
		Map map = new HashMap();

		for(Room c:rooms){
			if(map.get(c.getName())==null){
				map.put(c.getName(),c);
			}
		}	
			
		List<Room> roomsList = new ArrayList<Room>(map.values());
		for (Room room : roomsList ) {
			JSONObject examinerReport = new JSONObject();
			Person examiner = this.getParticipantsService().findByExaminerInRoom(room);
			examinerReport.put("examinerId",examiner.getId());
			examinerReport.put("examiner", examiner.getName()+" "+examiner.getLastName());
			report.append("report", examinerReport);
		}
		return report;
	}

	private JSONObject generateScoreJsonObjectByRoom(List<Room> rooms) {
		Map<Room,Map<Topic,List<Double>>> scoreExaminerAll = prepareDataScoreBoard(rooms);
		Map<Room, Map<String, Object>> scoreCalculateds = this.getAveragesCalculationService().calculation(scoreExaminerAll);
		JSONObject report = new JSONObject();

		for(Room keyScoreCalculated : scoreCalculateds.keySet()){
			JSONObject examinerReport = new JSONObject();

			Map<String, Object> scoreMap = scoreCalculateds.get(keyScoreCalculated);
			for(String key: scoreMap.keySet()) {
				examinerReport.put(key, scoreMap.get(key));
			}

			Person examiner = this.getParticipantsService().findByExaminerInRoom(keyScoreCalculated);
			examinerReport.put("examinerId",examiner.getId());
			examinerReport.put("examiner", examiner.getName()+" "+examiner.getLastName());
			DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			DateFormat formatDate2 = new SimpleDateFormat("HH:mm");
			examinerReport.put("dateTest", formatDate.format(keyScoreCalculated.getStartTime())+" - "
				+formatDate2.format(keyScoreCalculated.getEndTime()));
			report.append("report", examinerReport);
			examinerReport.put("nameRoom",keyScoreCalculated.getName());
		}
		return report;
	}

	@Override
	public Map<Room,Map<Topic, List<Double>>> prepareDataScoreBoard(List<Room> rooms) {
		Map<Room,Map<Topic,List<Double>>> scoreExaminerAll = new HashMap<Room,Map<Topic,List<Double>>>();

		for (Room room : rooms) {
			List<ScoreBoard> scoreBoards = this.getScoreBoardService().findByRoom(room);
			Map<Topic,List<Double>> scoreEachTopicExaminer = new HashMap<Topic,List<Double>>();

			groupScoreOfTopic(scoreEachTopicExaminer, scoreBoards);

			scoreExaminerAll.put(room, scoreEachTopicExaminer);
		}
		return scoreExaminerAll;
	}

	public void groupScoreOfTopic(Map<Topic, List<Double>> scoreEachTopicExaminer, List<ScoreBoard> scoreBoards) {
		for(ScoreBoard scoreBoard:scoreBoards) {
			List<Double> scoreTopic = null;
			Topic topic = scoreBoard.getTopic();
			Double score = new Double(scoreBoard.getScore());

			if (scoreEachTopicExaminer.containsKey(topic)) {
				scoreTopic = scoreEachTopicExaminer.get(topic);
				scoreTopic.add(score);
			} else {
				scoreTopic = new ArrayList<Double>();
				scoreTopic.add(score);
				scoreEachTopicExaminer.put(topic, scoreTopic);
			}
		}
	}

	@Override
	public JSONObject getScoreByExaminer(Person examiner) {
		List<Participants> participantsList = this.getParticipantsService().findByPerson(examiner);
		List<Room> rooms = new ArrayList<Room>();
		for(Participants participants:participantsList){
			Room room = participants.getRoom();
			if (room.getStatus().equals("Completed")) {
				rooms.add(participants.getRoom());	
			}
		}
		JSONObject report = generateScoreJsonObjectByRoom(rooms);
		return report;
	}

	@Override
	public Person getPersonByExaminerId(Long examinerId) {
		Person examiner = this.getPersonService().findById(examinerId);
		return examiner;
	}

	@Override
	public JSONObject getCourseInformationSummary(Long roomId, Long examinerId, Long committeeId,Long courseId) {

		JSONObject courseInformation = new JSONObject();
		Room room			= this.getRoomService().findById(roomId);
		Person committee 	= this.getPersonService().findById(committeeId);
		Person examiner 	= this.getPersonService().findById(examinerId);
		Course course 		= this.getCourseService().findById(courseId);

		List<Subject> subjects = this.getSubjectService().findByCourse(course);

		JSONObject subjectElement = null;
		for (Subject subject : subjects) {
			subjectElement = new JSONObject();
			subjectElement.put("id", subject.getId());
			subjectElement.put("name",subject.getName());

			List<Topic> topics = this.getTopicService().findBySubject(subject);
			findScoreAddIntoJsonOfTopicSummary(room, committee,examiner, subjectElement, topics);
			courseInformation.append("subject", subjectElement);
		}
		return courseInformation;
	}

	private void findScoreAddIntoJsonOfTopicSummary(Room room,Person committee,Person examiner,JSONObject subjectElement, List<Topic> topics){
		float allScore = 0;
		int allTopic = 0;
		float changeMeanScore;

		for (Topic topic : topics) {
			JSONObject topicElement = new JSONObject();
			topicElement.put("id", topic.getId());
			topicElement.put("name",topic.getName());
			topicElement.put("description",topic.getDescription());

			List<ScoreBoard> scoreBoard = this.getScoreBoardService().findByRoomAndTopicAndExaminer( room, topic, examiner);

			JSONObject scoreAndComment = sumScoreAndComment(scoreBoard);

			if(scoreBoard != null){
				topicElement.put("score",scoreAndComment.get("meanScore"));
				topicElement.put("comment",scoreAndComment.get("sumComment"));

			}else{
				topicElement.put("score","-");
				topicElement.put("comment","");
			}

			String ss = scoreAndComment.get("meanScore").toString();
			changeMeanScore = Float.valueOf(ss);

			allScore += changeMeanScore;
			allTopic++;

			subjectElement.append("topic", topicElement);

		}

		if (allScore != 0) {
			subjectElement.append("averageScore", averageScoreAllSubject(allScore, allTopic));
		}else{
			subjectElement.append("averageScore", "0");
		}

	}

	public float averageScoreAllSubject(float score,int allTopic){
		float averageScore = 0;
		averageScore = (score/allTopic)*100;

		return  averageScore;
	}


	public JSONObject sumScoreAndComment(List<ScoreBoard> scoreBoard) {
		float sumScore = 0.0f;
		int sumTopic = 0;
		String sumComment = "";
		JSONObject result = new JSONObject();
		for (int i = 0; i < scoreBoard.size(); i++) {
			if (scoreBoard != null) {

				sumScore += scoreBoard.get(i).getScore();
				sumComment += "- "+scoreBoard.get(i).getComment() + "\n";
				sumTopic++;

			}
		}
		float returnScore = sumScore/sumTopic;
		BigDecimal ScoreTopic = new BigDecimal(returnScore).setScale(1,BigDecimal.ROUND_HALF_UP);

		result.put("meanScore",ScoreTopic);
		result.put("sumComment",sumComment);

		return	result;
	}
}
