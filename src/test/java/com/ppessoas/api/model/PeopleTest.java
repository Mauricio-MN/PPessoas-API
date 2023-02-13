package com.ppessoas.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class PeopleTest {

	private People people;
	private Address address1;
	private Address address2;
	
	@BeforeEach
	public void setUp() {
		people = new People();
		
		address1 = new Address();
		address1.setCep("00000000");
		address1.setAddress("Rua dos Bobos");
		address1.setNumber(0);
		address1.setCity("Lugar Nenhum");
		
		address2 = new Address();
		address2.setCep("01010101");
		address2.setAddress("Rua Vazia");
		address2.setNumber(9999);
		address2.setCity("Algum Lugar");
		
		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		addresses.add(address2);
		
		people.setName("Júnior");
		people.setDateOfBirth(new java.util.Date());
		people.setAddresses(addresses);
		people.setMainAddress(address1);
	}
	
	@Test
	public void getAddressesTest() {
		assertThat(people.getAddresses().size()).isEqualTo(2);
		assertThat(people.getAddresses().contains(address1)).isTrue();
		assertThat(people.getAddresses().contains(address2)).isTrue();
	}
	
	@Test
	public void setAddressesTest() {
		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		people.setAddresses(addresses);
		
		assertThat(people.getAddresses().size()).isEqualTo(1);
		assertThat(people.getAddresses().contains(address1)).isTrue();
		assertThat(people.getAddresses().contains(address2)).isFalse();
	}
	
	@Test
	public void testRemoveAddress() {
		people.removeAddress(address1);
		
		assertEquals(1, people.getAddresses().size());
	}
	
}