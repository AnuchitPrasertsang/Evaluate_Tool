package com.spt.evt.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spt.evt.dao.RoomDao;
import com.spt.evt.entity.Room;
import com.spt.evt.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoomServiceImpl.class);

	@Autowired
	private RoomDao roomDao;

	@Override
	public List<Room> findAll() {
		return this.roomDao.findAll();
	}

	@Override
	public Room findById(Long id) {
		return this.roomDao.findById(id);
	}

	@Override
	public List<Room> findByStatus(String status) {
		return this.roomDao.findByStatus(status);
	}

	@Override
	public void setStatusRoom(Room room) {
		this.roomDao.setStatusByRoom(room);
		
	}

    @Override
    public void setStatusRoomReady(Long roomId) {
           this.roomDao.setStatusRoomReady(roomId);
    }

    @Override
	public void removeRoom(Long roomLongId) {
		this.roomDao.setRemoveRoom(roomLongId);
	}

	@Override
	public void addRoom(Room data){
		//this.roomDao.setAddRoom(data);
		LOGGER.debug("RoomServiceImplAddRoom");
		this.roomDao.setAddRoom(data);
	}
}
