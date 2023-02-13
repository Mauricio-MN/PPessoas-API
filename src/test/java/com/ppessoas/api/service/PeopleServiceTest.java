package com.ppessoas.api.service;

import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import com.ppessoas.api.model.Address;
import com.ppessoas.api.model.People;
import com.ppessoas.api.repository.AddressRepository;
import com.ppessoas.api.repository.PeopleRepository;
import com.ppessoas.api.util.PeopleUtilForTests;

@ExtendWith(MockitoExtension.class)
public class PeopleServiceTest {

	@Mock
	private PeopleRepository peopleRepository;

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private PeopleService peopleService;
	
	PeopleUtilForTests peopleUtilForTests;
	
	@DirtiesContext
	@BeforeEach
	public void setUp() {
		peopleUtilForTests = new PeopleUtilForTests();
	}

	@Test
	void testInsertPeople() throws ParseException {
		People people = peopleUtilForTests.createPeople();

		when(peopleRepository.saveAndFlush(people)).thenReturn(people);

		People savedPeople = peopleService.insertPeople(people);

		assertEquals(people, savedPeople);
		verify(peopleRepository, times(1)).saveAndFlush(people);
	}

	@Test
	void testFindAll() {
		List<People> peopleList = Arrays.asList(new People(), new People());
		when(peopleRepository.findAll()).thenReturn(peopleList);

		List<People> result = peopleService.findAll();

		assertEquals(result, peopleList);
		verify(peopleRepository, times(1)).findAll();
	}

	@Test
	void testFindById() {
		People expectedPeople = peopleUtilForTests.createPeople();
		when(peopleRepository.findById(expectedPeople.getId())).thenReturn(Optional.of(expectedPeople));

		People result = peopleService.findById(expectedPeople.getId());

		assertNotNull(result);
		assertEquals(expectedPeople, result);
		verify(peopleRepository, times(1)).findById(expectedPeople.getId());
	}

	@Test
	void testFindAllById() {

		Long id1 = 1L;
		Long id2 = 2L;
		List<Long> ids = Arrays.asList(id1, id2);

		People people1 = peopleUtilForTests.createPeople();
		people1.setId(id1);
		People people2 = peopleUtilForTests.createPeople();
		people2.setId(id2);
		List<People> expectedPeople = Arrays.asList(people1, people2);

		when(peopleRepository.findAllById(ids)).thenReturn(expectedPeople);

		List<People> result = peopleService.findAllById(ids);

		assertEquals(expectedPeople, result);
	}

	@Test
	void testUpdate() {
		People people = peopleUtilForTests.createPeople();
		when(peopleRepository.findById(1L)).thenReturn(Optional.of(people));
		when(peopleRepository.saveAndFlush(people)).thenReturn(people);
		
		List<Long> ids = new ArrayList<>();
		List<Address> addresses = new ArrayList<>();
		for(Address address : people.getAddresses()) {
			addresses.add(address);
			ids.add(address.getId());
		}
		Address mainAddress = people.getMainAddress();
		Long mainAddressId = people.getMainAddress().getId();
		when(addressRepository.findAllById(ids)).thenReturn(addresses);
		when(addressRepository.findById(mainAddressId)).thenReturn(Optional.of(mainAddress));

		People updatedPeople = peopleService.update(1L, people);

		assertNotNull(updatedPeople);
		assertEquals(people.getId(), updatedPeople.getId());
		assertEquals(people.getName(), updatedPeople.getName());

		verify(peopleRepository, times(1)).findById(1L);
		verify(peopleRepository, times(1)).saveAndFlush(people);
	}
	
	@Test
	void testDeletePeapleAddresses() {
		People people = peopleUtilForTests.createPeople();
		when(peopleRepository.saveAndFlush(people)).thenReturn(people);
		People savedPeople = peopleService.insertPeople(people);
		
		List<Long> ids = new ArrayList<>();
		List<Address> addresses = new ArrayList<>();
		for(Address address : people.getAddresses()) {
			when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
			ids.add(address.getId());
			addresses.add(address);
		}
		
		doAnswer((invocation) -> {
			peopleRepository.findById(1L);
			for(Address address : addresses) {
				savedPeople.removeAddress(address);
			}
			  return Optional.of(savedPeople);
			}).when(addressRepository).deleteAllById(ids);
		
		doAnswer((invocation) -> {
			  return Optional.of(savedPeople);
			}).when(peopleRepository).findById(1L);
		
		peopleService.deletePeapleAddresses(1L, peopleRepository.findById(1L).get());
		
		
		People peopleDeletedAddresses = peopleRepository.findById(1L).get();
		
		assertEquals(peopleDeletedAddresses.getAddresses().size(), 0);

		verify(peopleRepository, times(4)).findById(1L);
		verify(peopleRepository, times(1)).saveAndFlush(people);
	}
	
	@Test
	void testDeleteAllPeapleAddresses() {
		People people = peopleUtilForTests.createPeople();
		when(peopleRepository.saveAndFlush(people)).thenReturn(people);
		People savedPeople = peopleService.insertPeople(people);
		
		List<Long> ids = new ArrayList<>();
		List<Address> addresses = new ArrayList<>();
		for(Address address : people.getAddresses()) {
			when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
			ids.add(address.getId());
			addresses.add(address);
		}
		
		doAnswer((invocation) -> {
			peopleRepository.findById(1L);
			for(Address address : addresses) {
				savedPeople.removeAddress(address);
			}
			  return Optional.of(savedPeople);
			}).when(addressRepository).deleteAllById(ids);
		
		doAnswer((invocation) -> {
			  return Optional.of(savedPeople);
			}).when(peopleRepository).findById(1L);
		
		peopleService.deleteAllPeapleAddresses(1L);
		
		
		People peopleDeletedAddresses = peopleRepository.findById(1L).get();
		
		assertEquals(peopleDeletedAddresses.getAddresses().size(), 0);

		verify(peopleRepository, times(3)).findById(1L);
		verify(peopleRepository, times(1)).saveAndFlush(people);
	}

}
