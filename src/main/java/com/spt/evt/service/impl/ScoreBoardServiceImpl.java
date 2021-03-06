package com.spt.evt.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spt.evt.dao.ScoreBoardDao;
import com.spt.evt.entity.Person;
import com.spt.evt.entity.Room;
import com.spt.evt.entity.ScoreBoard;
import com.spt.evt.entity.Topic;
import com.spt.evt.service.ScoreBoardService;

import java.util.List;

@Service
public class ScoreBoardServiceImpl implements ScoreBoardService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ScoreBoardServiceImpl.class);

	@Autowired
	private ScoreBoardDao scoreBoardDao;

	@Override
	public ScoreBoard findByRoomAndCommiteeAndTopicAndExaminer(Room room, Person committee, Topic topic,
			Person examiner) {
		return scoreBoardDao.findByRoomAndCommiteeAndTopicAndExaminer(room, committee, topic,
				examiner);
	}

	@Override
	public List<ScoreBoard> findByRoomAndTopicAndExaminer(Room room, Topic topic, Person examiner){
		return scoreBoardDao.findByRoomAndTopicAndExaminer(room, topic, examiner);
	}


	@Override
	public void save(ScoreBoard scoreBoard) {
		scoreBoardDao.save(scoreBoard);		
	}

	@Override
	public void saveOrUpdate(ScoreBoard scoreBoard) {
		scoreBoardDao.saveOrUpdate(scoreBoard);		
	}

	@Override
	public List<ScoreBoard> findByRoom(Room room) {
		return this.scoreBoardDao.findByRoom(room);
	}

	@Override
	public List<ScoreBoard> findByTopic(Topic topic) {
		return this.scoreBoardDao.findByTopic(topic);
	}


}