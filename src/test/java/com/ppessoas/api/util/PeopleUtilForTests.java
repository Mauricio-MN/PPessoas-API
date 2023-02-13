package com.ppessoas.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.ppessoas.api.model.Address;
import com.ppessoas.api.model.People;

public class PeopleUtilForTests {
	
	private String generateString() {
		String uuid = UUID.randomUUID().toString().replaceAll("_", "");
		return uuid;
	}
	
	public People createPeople() {
		People people = new People();
		people.setId(1L);
		people.setName("Júnior" + generateString());
		Date date;
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse("10-10-2001");
			people.setDateOfBirth(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Address address = new Address();
		address.setId(1L);
		address.setCep("00000000" + generateString());
		address.setAddress("Rua dos Bobos" + generateString());
		address.setNumber(0);
		address.setCity("Lugar Nenhum" + generateString());
		address.setPeople(people);

		Address address2 = new Address();
		address2.setId(2L);
		address2.setCep("87654321" + generateString());
		address2.setAddress("Rua dos 3" + generateString());
		address2.setNumber(123);
		address2.setCity("Castanhoba" + generateString());
		address2.setPeople(people);

		Set<Address> addresses = new HashSet<>();
		addresses.add(address);
		addresses.add(address2);

		people.setAddresses(addresses);
		people.setMainAddress(address);

		return people;
	}

	public People modifyPeople(People people) {
		for (Address address : people.getAddresses()) {
			address.setNumber(address.getNumber() + 1);
			address.setAddress(address.getAddress() + generateString());
			address.setCep(address.getCep() + generateString());
			address.setCity(address.getCity() + generateString());
		}

		people.setName(people.getName() + generateString());
		Date date;
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse("10-10-2002");
			people.setDateOfBirth(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return people;
	}
	
	public People peopleAddAddress(People people) {
		Address address = new Address();
		address.setCep("01010101" + generateString());
		address.setAddress("Rua Capim" + generateString());
		address.setNumber(1010);
		address.setCity("Longe" + generateString());

		List<Address> addresses = new ArrayList<>();
		addresses.add(address);

		people.addAddresses(addresses);

		return people;
	}
	
}
