package com.ppessoas.api.controller;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppessoas.api.model.Address;
import com.ppessoas.api.model.People;
import com.ppessoas.api.service.PeopleService;
import com.ppessoas.api.util.PeopleUtilForTests;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PeopleControllerTest {

	private PeopleUtilForTests peopleUtilForTests;

	private MockMvc mockMvc;

	@Mock
	private PeopleService peopleService;

	@InjectMocks
	private PeopleController peopleController;

	@Before
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(peopleController).build();
		peopleUtilForTests = new PeopleUtilForTests();
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testInsertPeople() throws Exception {
		People people = peopleUtilForTests.createPeople();
		people.setId(null);
		for (Address address : people.getAddresses()) {
			address.setId(null);
		}

		doAnswer((invocation) -> {
			people.setId(1L);
			Long i = 0L;
			for (Address address : people.getAddresses()) {
				i++;
				address.setId(i);
			}
			return people;
		}).when(peopleService).insertPeople(people);

		mockMvc.perform(post("/people/add").contentType(MediaType.APPLICATION_JSON).content(asJsonString(people)))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(people)));
	}

	@Test
	public void testFindAll() throws Exception {
		People people = peopleUtilForTests.createPeople();
		People people2 = peopleUtilForTests.createPeople();
		people2.setId(2L);
		List<People> peoples = new ArrayList<>();
		peoples.add(people);
		peoples.add(people2);
		when(peopleService.findAll()).thenReturn(peoples);

		mockMvc.perform(get("/people/list/all")).andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(peoples)));
	}

	@Test
	public void testFindById() throws Exception {
		People people = peopleUtilForTests.createPeople();

		when(peopleService.findById(1L)).thenReturn(people);

		mockMvc.perform(get("/people/1")).andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(people)));
	}

	@Test
	public void findAllById() throws Exception {
		People people = peopleUtilForTests.createPeople();
		People people2 = peopleUtilForTests.createPeople();
		people2.setId(2L);
		List<People> peoples = new ArrayList<>();
		List<Long> ids = new ArrayList<>();
		peoples.add(people);
		peoples.add(people2);
		ids.add(people.getId());
		ids.add(people2.getId());

		when(peopleService.findAllById(ids)).thenReturn(peoples);

		mockMvc.perform(get("/people/list/1,2")).andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(peoples)));
	}

	@Test
	public void findAddressesByPeopleId() throws Exception {
		People people = peopleUtilForTests.createPeople();
		when(peopleService.findAddressesByPeopleId(1L)).thenReturn(people.getAddresses());

		mockMvc.perform(get("/people/1/addresses")).andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(people.getAddresses())));
	}

	@Test
	public void getMainAddressByPeopleId() throws Exception {
		People people = peopleUtilForTests.createPeople();
		when(peopleService.getMainAddressByPeopleId(1L)).thenReturn(people.getMainAddress());

		mockMvc.perform(get("/people/1/mainaddress")).andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(people.getMainAddress())));
	}

	@Test
	public void update() throws Exception {
		People people = peopleUtilForTests.createPeople();
		when(peopleService.update(1L, people)).thenReturn(people);

		mockMvc.perform(put("/people/1/edit").contentType(MediaType.APPLICATION_JSON).content(asJsonString(people)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(people)));
	}

	@Test
	public void deleteAddresses() throws Exception {
		People people = peopleUtilForTests.createPeople();
		List<Address> addresses = new ArrayList<>();
		doAnswer((invocation) -> {
			for (Address address : addresses) {
				people.removeAddress(address);
			}
			return null;
		}).when(peopleService).deletePeapleAddresses(1L, people);

		mockMvc.perform(delete("/people/1/addresses/delete").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(people))).andExpect(status().isOk());
	}

	@Test
	public void deleteAllAddresses() throws Exception {
		People people = peopleUtilForTests.createPeople();
		List<Address> addresses = new ArrayList<>();
		doAnswer((invocation) -> {
			for (Address address : addresses) {
				people.removeAddress(address);
			}
			return null;
		}).when(peopleService).deleteAllPeapleAddresses(1L);

		mockMvc.perform(delete("/people/1/addresses/delete/all")).andExpect(status().isOk());
	}

}