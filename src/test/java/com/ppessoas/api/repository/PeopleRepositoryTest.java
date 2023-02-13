package com.ppessoas.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.ppessoas.api.model.People;
import com.ppessoas.api.util.PeopleUtilForTests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
class PeopleRepositoryTest {

    @Autowired
    private PeopleRepository peopleRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
	PeopleUtilForTests peopleUtilForTests;
	
    @DirtiesContext
	@BeforeEach
	public void setUp() {
		peopleUtilForTests = new PeopleUtilForTests();
	}
	
	@Test
	  public void should_find_no_peoples_if_people_is_empty() {
	    List<People> peoples = peopleRepository.findAll();

	    assertThat(peoples).isEmpty();
	  }

	  @Test
	  public void should_store_a_people() {
		People people = peopleUtilForTests.createPeople();
	    People saved = peopleRepository.save(people);

	    assertThat(saved.getName()).isEqualTo(people.getName());
	    assertThat(saved.getDateOfBirth()).isEqualTo(people.getDateOfBirth());
	    assertThat(saved.getAddresses()).isEqualTo(people.getAddresses());
	    assertThat(saved.getMainAddress()).isEqualTo(people.getMainAddress());
	  }

	  @Test
	  public void should_find_all_peoples() {
	    People people1 = peopleUtilForTests.createPeople();
	    entityManager.persist(people1);

	    People people2 = peopleUtilForTests.createPeople();
	    entityManager.persist(people2);

	    People people3 = peopleUtilForTests.createPeople();
	    entityManager.persist(people3);

	    List<People> peoples = peopleRepository.findAll();

	    assertThat(peoples).hasSize(3).contains(people1, people2, people3);
	  }

	  @Test
	  public void should_find_people_by_id() {
	    People tut1 = peopleUtilForTests.createPeople();
	    entityManager.persist(tut1);

	    People tut2 = peopleUtilForTests.createPeople();
	    entityManager.persist(tut2);

	    People foundPeople = peopleRepository.findById(tut2.getId()).get();

	    assertThat(foundPeople).isEqualTo(tut2);
	  }

	  @Test
	  public void should_update_people_by_id() {
	    People somePeople = peopleUtilForTests.createPeople();
	    entityManager.persist(somePeople);

	    People people = peopleUtilForTests.createPeople();
	    entityManager.persist(people);

	    People insertedPeople = peopleRepository.findById(people.getId()).get();
	    peopleUtilForTests.modifyPeople(insertedPeople);
	    peopleRepository.save(insertedPeople);

	    People checkPeople = peopleRepository.findById(people.getId()).get();
	    
	    assertThat(checkPeople.getId()).isEqualTo(insertedPeople.getId());
	    assertThat(checkPeople.getName()).isEqualTo(insertedPeople.getName());
	    assertThat(checkPeople.getAddresses()).isEqualTo(insertedPeople.getAddresses());
	    assertThat(checkPeople.getMainAddress()).isEqualTo(insertedPeople.getMainAddress());
	    assertThat(checkPeople.getDateOfBirth()).isEqualTo(insertedPeople.getDateOfBirth());
	  }

	  @Test
	  public void should_delete_people_by_id() {
	    People people1 = peopleUtilForTests.createPeople();
	    entityManager.persist(people1);

	    People people2 = peopleUtilForTests.createPeople();
	    entityManager.persist(people2);

	    People people3 = peopleUtilForTests.createPeople();
	    entityManager.persist(people3);

	    peopleRepository.deleteById(people2.getId());

	    List<People> peoples = peopleRepository.findAll();

	    assertThat(peoples).hasSize(2).contains(people1, people3);
	  }

	  @Test
	  public void should_delete_all_peoples() {
	    entityManager.persist(peopleUtilForTests.createPeople());
	    entityManager.persist(peopleUtilForTests.createPeople());

	    peopleRepository.deleteAll();

	    assertThat(peopleRepository.findAll()).isEmpty();
	  }

}
