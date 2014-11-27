package com.spt.evt.service.impl;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spt.evt.entity.Participants;
import com.spt.evt.entity.Person;
import com.spt.evt.entity.Room;
import com.spt.evt.service.ExaminationRoomService;

@Service
public class ExaminationRoomServiceImpl extends ProviderService implements ExaminationRoomService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExaminationRoomServiceImpl.class);
	@Override
	public JSONObject getRoomInformation() {
		JSONObject allRoom = new JSONObject();
		JSONObject roomDetail = null;
		List<Room> rooms = this.getRoomService().findAll();
		for (Room room : rooms) {
			roomDetail = new JSONObject();
			roomDetail.put("id", room.getId());
			roomDetail.put("name",room.getName());
			roomDetail.put("courseId", room.getCourseId());
			roomDetail.put("description",room.getDescription());
			roomDetail.put("startTime",room.getStartTime());
			roomDetail.put("endTime",room.getEndTime());
			roomDetail.put("status",room.getStatus());

			Long roomId = room.getId();
			findExaminerAndModulator(roomDetail, room);
			allRoom.append("room", roomDetail);

		}
		return allRoom;
	}

	private void findExaminerAndModulator(JSONObject roomDetail, Room room) {
		Long personModulatorId;
		String personModulatorName;
		String personModulatorLastName;
		String personModulator;
		Long personExaminerId;
		String personExaminerName;
		String personExaminerLastName;
		String personExaminer;
		
		List<Participants> participantsList = this.getParticipantsService().findByRoom(room);
		for(Participants participants : participantsList){
			Boolean isModulator = participants.getModulator();
			String role = participants.getRole();

			if(role.equals("examiner")){
				personExaminerId = participants.getPerson().getId();
				personExaminerName = participants.getPerson().getName();
				personExaminerLastName = participants.getPerson().getLastName();
				personExaminer = personExaminerName + " " + personExaminerLastName;
				roomDetail.put("examinerId", personExaminerId.toString());
				roomDetail.put("examiner", personExaminer);
			}
			else if(isModulator){
				personModulatorId = participants.getPerson().getId();
				personModulatorName = participants.getPerson().getName();
				personModulatorLastName = participants.getPerson().getLastName();
				personModulator = personModulatorName +" "+ personModulatorLastName;
				roomDetail.put("modulatorId", personModulatorId);
				roomDetail.put("modulator", personModulator);

			}

		}
	}

	@Override
	public JSONObject getPersonInRoomInformation(Long roomId, Long examinerId, Long committeeId, Long modulatorId) {
		JSONObject personInRoomInformation = new JSONObject();
		Room room			= this.getRoomService().findById(roomId);
		Person examiner 	= this.getPersonService().findById(examinerId);
		Person committee 	= this.getPersonService().findById(committeeId);
		Person modulator	= this.getPersonService().findById(modulatorId);
		personInRoomInformation.put("idRoom", room.getId());
		personInRoomInformation.put("idCourse", room.getCourseId());
		personInRoomInformation.put("idExaminer", examiner.getId());
		personInRoomInformation.put("nameExaminer", examiner.getName());
		personInRoomInformation.put("lastNameExaminer", examiner.getLastName());
		personInRoomInformation.put("idCommittee", committee.getId());
		personInRoomInformation.put("nameCommittee", committee.getName());
		personInRoomInformation.put("lastNameCommittee", committee.getLastName());
		personInRoomInformation.put("idModulator", modulator.getId());
		return personInRoomInformation;
	}

	public JSONObject findParticipantsByPersonId(Long personId) {
		JSONObject participantsDetail = new JSONObject();
		JSONObject personDetail = null;
		Person person = this.getPersonService().findById(personId);
		List<Participants> participants = findParticipantsByPerson(person);
		for(Participants participant : participants){
			personDetail = new JSONObject();
			personDetail.put("idRoom", participant.getRoom().getId());
			personDetail.put("idPerson", participant.getPerson().getId());
			participantsDetail.append("room", personDetail);
		}
		
		return participantsDetail;
	}
	
	public List<Participants> findParticipantsByPerson(Person person) {
		List<Participants> participants = this.getParticipantsService().findByPerson(person);
		return participants;
	}

}