package com.ppessoas.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressTest {
	
	private Address address;
	
	@BeforeEach
	public void setUp() {
		address = new Address();
		address.setCep("12345678");
		address.setAddress("Rua Teste");
		address.setNumber(123);
		address.setCity("Cidade Teste");
	}

	@Test
	public void testGetPeople() {
		People people = new People();
		address.setPeople(people);
		assertEquals(people, address.getPeople());
	}
	
	@Test
	public void testRemovePeople() {
		People people = new People();
		address.setPeople(people);
		address.removePeople();
		assertNotNull(address);
		assertEquals(null, address.getPeople());
	}
}
