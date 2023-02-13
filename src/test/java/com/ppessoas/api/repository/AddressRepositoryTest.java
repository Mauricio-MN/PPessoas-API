package com.ppessoas.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.ppessoas.api.model.Address;
import com.ppessoas.api.util.AddressUtilForTests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
public class AddressRepositoryTest {

	@Autowired
	private AddressRepository addressRepository;

	@PersistenceContext
	private EntityManager entityManager;

	AddressUtilForTests addressUtilForTests;

	@DirtiesContext
	@BeforeEach
	public void setUp() {
		addressUtilForTests = new AddressUtilForTests();
	}

	@Test
	public void should_find_no_addresss_if_address_is_empty() {
		List<Address> addresss = addressRepository.findAll();

		assertThat(addresss).isEmpty();
	}

	@Test
	public void should_store_a_address() {
		Address address = addressUtilForTests.createAddress();
		Address saved = addressRepository.save(address);

		assertThat(saved.getAddress()).isEqualTo(address.getAddress());
		assertThat(saved.getCep()).isEqualTo(address.getCep());
		assertThat(saved.getCity()).isEqualTo(address.getCity());
		assertThat(saved.getNumber()).isEqualTo(address.getNumber());
	}

	@Test
	public void should_find_address_by_id() {
		Address tut1 = addressUtilForTests.createAddress();
		entityManager.persist(tut1);

		Address tut2 = addressUtilForTests.createAddress();
		entityManager.persist(tut2);

		Address foundAddress = addressRepository.findById(tut2.getId()).get();

		assertThat(foundAddress).isEqualTo(tut2);
	}

	@Test
	public void should_update_address_by_id() {
		Address someAddress = addressUtilForTests.createAddress();
		entityManager.persist(someAddress);

		Address address = addressUtilForTests.createAddress();
		entityManager.persist(address);

		Address insertedAddress = addressRepository.findById(address.getId()).get();
		addressUtilForTests.modifyAddress(insertedAddress);
		addressRepository.save(insertedAddress);

		Address checkAddress = addressRepository.findById(address.getId()).get();

		assertThat(checkAddress.getAddress()).isEqualTo(insertedAddress.getAddress());
		assertThat(checkAddress.getCep()).isEqualTo(insertedAddress.getCep());
		assertThat(checkAddress.getCity()).isEqualTo(insertedAddress.getCity());
		assertThat(checkAddress.getNumber()).isEqualTo(insertedAddress.getNumber());
	}

}
