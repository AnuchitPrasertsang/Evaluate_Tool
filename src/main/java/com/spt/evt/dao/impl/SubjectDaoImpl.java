package com.spt.evt.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.spt.evt.dao.CourseDao;
import com.spt.evt.dao.SubjectDao;
import com.spt.evt.entity.Base;
import com.spt.evt.entity.Course;
import com.spt.evt.entity.Subject;

@Repository
public class SubjectDaoImpl extends AbstracHibernateDaoSupport implements
		SubjectDao {
	private static final Logger logger = LoggerFactory
			.getLogger(SubjectDaoImpl.class);

	@Override
	public List<Subject> findByCourse(Course course) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Subject.class);
		criteria.add(Restrictions.eq("course", course));
		criteria.addOrder(Order.asc("id"));
		List<Subject> result = (List<Subject>) this.getHibernateTemplate().findByCriteria(criteria);
		return result;
	}
}