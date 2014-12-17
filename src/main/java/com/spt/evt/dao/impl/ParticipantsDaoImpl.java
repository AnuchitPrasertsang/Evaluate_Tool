package com.spt.evt.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spt.evt.dao.ParticipantsDao;
import com.spt.evt.entity.Participants;
import com.spt.evt.entity.Person;
import com.spt.evt.entity.Room;
import com.spt.evt.dao.impl.TemplateEntityManagerDao;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class ParticipantsDaoImpl extends TemplateEntityManagerDao implements ParticipantsDao {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ParticipantsDaoImpl.class);
	
	@Override
	@Transactional(readOnly = true)
	public List<Participants> findByExample(Participants participants) {
		Query query = this.getEntityManager().createQuery("FROM Participants");
		return query.getResultList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Participants> findByRoom(Room room) {
		Criteria criteria = ((Session) this.getEntityManager().getDelegate()).createCriteria(Participants.class);
		criteria.add(Restrictions.eq("room", room));
		List<Participants> result = criteria.list();
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Participants> findByPerson(Person person) {
		Criteria criteria = ((Session) this.getEntityManager().getDelegate()).createCriteria(Participants.class);
		criteria.add(Restrictions.eq("person", person));
		List<Participants> result = criteria.list();
		return result;
	}

    @Override
    @Transactional(readOnly = true)
    public List<Participants> findPersonWithRequestCommittee(Room room) {
        Criteria criteria=((Session)this.getEntityManager().getDelegate()).createCriteria(Participants.class);
        criteria.add(Restrictions.eq("room",room));
        criteria.add(Restrictions.in("role", Arrays.asList("wait", "see")));
        List<Participants> result = criteria.list();
        return result;
    }

    @Override
    @Transactional
    public void setRoleInPaticipants(Long paticipantId) {
      Participants participants= (Participants) this.getEntityManager().find(Participants.class,paticipantId);
      System.out.print("========================"+participants+"==========================");
      participants.setRole("see");
      this.getEntityManager().merge(participants);
    }

    @Override
    @Transactional
    public String addRequestCommittee(Room room, Person person) {
        Criteria criteriaAdd=((Session)this.getEntityManager().getDelegate()).createCriteria(Participants.class);
        criteriaAdd.add(Restrictions.eq("room",room));
        criteriaAdd.add(Restrictions.eq("person",person));
        Participants add= (Participants) criteriaAdd.uniqueResult();
        if(add==null){
            Participants participants=new Participants();
            participants.setRole("wait");
            participants.setModulator(false);
            participants.setPerson(person);
            participants.setRoom(room);
            this.getEntityManager().merge(participants);
            return "success";
        }else{
            return "fail";
        }

    }

    @Override
    @Transactional
    public void removeRequestCommittee(Room room, Person person) {
        Participants participants= new Participants();
        participants.setPerson(person);
        participants.setRoom(room);
        System.out.print("========================================+++++++++++++++"+participants);
        this.getEntityManager().remove(participants);
    }

    @Override
    @Transactional
    public void addModulatorAndUpdateCommittee(Room roomApprove, Person personApprove, Person personInRoom) {
        Criteria criteriaAdd=((Session)this.getEntityManager().getDelegate()).createCriteria(Participants.class);
        criteriaAdd.add(Restrictions.eq("room",roomApprove));
        criteriaAdd.add(Restrictions.eq("person",personApprove));
        Participants add= (Participants) criteriaAdd.uniqueResult();
        //this.getEntityManager().merge(add);
        //Participants participantsa= (Participants) this.getEntityManager().find(Participants.class,2L);
      System.out.println("========Add======"+add.getRole());
       /* Criteria criteriaUpdate=((Session)this.getEntityManager().getDelegate()).createCriteria(Participants.class);
        criteriaUpdate.add(Restrictions.eq("room",roomApprove));
        criteriaUpdate.add(Restrictions.eq("person",personInRoom));
        Participants update= (Participants) criteriaUpdate.uniqueResult();
        update.setRole("committee");
        update.setModulator(false);
        add.setRoom(roomApprove);
        add.setPerson(personInRoom);
        this.getEntityManager().merge(update);
        Participants participantsup=this.getEntityManager().find(Participants.class,2L);
        System.out.println("========Add======"+participantsup.getModulator());*/
    }

    @Override
    @Transactional(readOnly = true)
    public Participants findById(Long id) {
        Participants participants= (Participants) this.getEntityManager().find(Participants.class,id);
        return participants;
    }

}
