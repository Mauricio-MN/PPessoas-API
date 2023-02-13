package com.ppessoas.api.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ppessoas.api.model.Address;
import com.ppessoas.api.model.People;
import com.ppessoas.api.service.PeopleService;

@RestController
@RequestMapping("/people")
public class PeopleController {
	
	private final PeopleService peopleService;

	public PeopleController(PeopleService peopleService) {
		this.peopleService = peopleService;
	}
	
	@PostMapping("/add")
	@ResponseStatus(code = HttpStatus.CREATED)
	public People insertPeople(@RequestBody People people) {
		return peopleService.insertPeople(people);
	}
	
	@GetMapping("/list/all")
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<People> findAll() {
		List<People> all = peopleService.findAll();
		return all;
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.FOUND)
	public People findById(@PathVariable Long id) {
		return peopleService.findById(id);
	}
	
	@GetMapping("/list/{ids}")
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<People> findAllById(@PathVariable List<Long> ids) {
		return peopleService.findAllById(ids);
	}
	
	@GetMapping("/{id}/addresses")
	@ResponseStatus(code = HttpStatus.FOUND)
	public Set<Address> findAddressesByPeopleId(@PathVariable Long id) {
		return peopleService.findAddressesByPeopleId(id);
	}
	
	@GetMapping("/{id}/mainaddress")
	@ResponseStatus(code = HttpStatus.FOUND)
	public Address getMainAddressByPeopleId(@PathVariable Long id) {
		return peopleService.getMainAddressByPeopleId(id);
	}
	
	@PutMapping("/{id}/edit")
	@ResponseStatus(code = HttpStatus.OK)
	public People update(@PathVariable Long id, @RequestBody People peopleInformation) {
		return peopleService.update(id, peopleInformation);
	}
	
	@DeleteMapping("/{id}/addresses/delete")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteAddresses(@PathVariable Long id, @RequestBody People peopleInformation) {
		peopleService.deletePeapleAddresses(id, peopleInformation);
	}
	
	@DeleteMapping("/{id}/addresses/delete/all")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteAllAddresses(@PathVariable Long id) {
		peopleService.deleteAllPeapleAddresses(id);
	}
	
}
