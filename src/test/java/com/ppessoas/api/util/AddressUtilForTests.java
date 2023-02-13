package com.ppessoas.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ppessoas.api.model.Address;
import com.ppessoas.api.model.People;

public class AddressUtilForTests {

	private String generateString() {
		String uuid = UUID.randomUUID().toString().replaceAll("_", "");
		return uuid;
	}
	
	public Address createAddress() {
		Address address = new Address();
		address.setCep("00000000" + generateString());
		address.setAddress("Rua dos Bobos" + generateString());
		address.setNumber(0);
		address.setCity("Lugar Nenhum" + generateString());

		Set<Address> addresses = new HashSet<>();
		addresses.add(address);

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

		people.setAddresses(addresses);
		address.setPeople(people);
		
		return address;
	}

	public Address modifyAddress(Address address) {
		address.setNumber(address.getNumber() + 1);
		address.setAddress(address.getAddress() + generateString());
		address.setCep(address.getCep() + generateString());
		address.setCity(address.getCity() + generateString());

		return address;
	}
	
}
