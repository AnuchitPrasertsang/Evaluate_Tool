package com.spt.evt.dao;

import com.spt.evt.entity.Person;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by : Anuchit Prasertsang Created Date : 28/10/2014
 */
public class PersonDaoTest extends AbstractTestDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonDaoTest.class);

	@Autowired
	private PersonDao personDao;

	@Test
	public void remark() {
		LOGGER.debug("/*** Begin test PersonDaoTest() ***/");
	}

	@Test
	public void testFindPersonByIdShouldBeNotNull() {
		Person person = this.personDao.findById(1L);
		Assert.assertNotNull(person);
	}

	@Test
	public void testFindPersonByUserNameAndPasswordShouldBeNotNull() {
		String userName = ("admin");
		String password = ("admin");
		List<Person> persons = this.personDao.findByUserNameAndPassword(userName, password);
		Assert.assertNotNull(persons);
	}

	@Test
	public void testPersistShouldBeIdNotNull(){

		Person person = new Person();

		person.setName("nameadmin");
		person.setLastName("lastnameadmin");
		person.setGender("genderadmin");
		person.setEmail("emailadmin");
		person.setPassword("passwordadmin");
		person.setInstitute("instituteadmin");
		person.setPhone("phonenumberadmin");
		person.setInternship("internshipadmin");
		person.setFacebook("facebookadmin");
		this.personDao.persist(person);

		LOGGER.debug("{}",person.getId());
	
		Assert.assertNotNull(person.getId());

	}

}
