package com.ppessoas.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.ppessoas.api.model.Address;
import com.ppessoas.api.model.People;
import com.ppessoas.api.repository.AddressRepository;
import com.ppessoas.api.repository.PeopleRepository;
import com.ppessoas.api.util.NullPropertys;

@Service
public class PeopleService {

	@Autowired
	private PeopleRepository peopleRepository;
	
	@Autowired
	private AddressRepository addressRepository;
    
	public People insertPeople(People people) {
		for(Address address : people.getAddresses()) {
			people.setMainAddress(address);
			break;
		}
	    People savedPeople = peopleRepository.saveAndFlush(people);
	    return savedPeople;
	}
	
	public List<People> findAll() {
		List<People> peoples = peopleRepository.findAll();
		return peoples;
	}
	
	public People findById(Long id) {
		return getPeopleById(id);
	}
	
	public List<People> findAllById(List<Long> ids) {
		List<People> peoples = peopleRepository.findAllById(ids);
		if(peoples.size() < 1) {
			throw new ResourceNotFoundException("Peoples not found");
		}
		return peopleRepository.findAllById(ids);
	}
	
	public Set<Address> findAddressesByPeopleId(Long id) {
	    People people = peopleRepository.getReferenceById(id);
	    return people.getAddresses();
	}
	
	public Address getMainAddressByPeopleId(Long id) {
	    People people = peopleRepository.getReferenceById(id);
	    return people.getMainAddress();
	}
	
	public People update(Long id, People peopleInformation) {
	    People people = getPeopleById(id);
	    
	    People savedPeople = editPeople(people, peopleInformation);
	    
		return savedPeople;
	}
	

	public void deletePeapleAddresses(Long id, People partialPeopleInformation) {
		getPeopleById(id);
		
		List<Long> ids = getValidIdsFromAddresses(partialPeopleInformation.getAddresses());
		
		addressRepository.deleteAllById(ids);
	}
	
	public void deleteAllPeapleAddresses(Long id) {
		People people =  getPeopleById(id);
		
		List<Long> ids = getValidIdsFromAddresses(people.getAddresses());
		
		addressRepository.deleteAllById(ids);
	}
	
	private List<Long> getValidIdsFromAddresses(Set<Address> addresses) {
		List<Long> ids = new ArrayList<>();
		for(Address address : addresses) {
			addressRepository.findById(address.getId()).
					orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + address.getId()));
			ids.add(address.getId());
		}
		return ids;
	}
	
	private People editPeople(People people, People peopleInformation) {
		editPeapleAddresses(people, peopleInformation);
	    copyPastePeoplePropertys(people, peopleInformation);
	    
	    return peopleRepository.saveAndFlush(people); 
	}
	
	private void editPeapleAddresses(People people, People peopleInformation) {
		List<Address> updateAddresses = new ArrayList<>();
	    List<Address> insertAddresses = new ArrayList<>();
	    
	    List<Address> addresses = new ArrayList<>(peopleInformation.getAddresses());
	    
	    splitInsertUpdateAddr(people, addresses, insertAddresses, updateAddresses);
	    
	    people.addAddresses(insertAddresses);
	    
	    if(peopleInformation.getMainAddress() != null) {
	    	Long addressId = peopleInformation.getMainAddress().getId();
	    	Address mainaddress = addressRepository.findById(addressId)
		            .orElseThrow(() -> new ResourceNotFoundException("Main Address not found with id " + addressId));
	    	people.setMainAddress(mainaddress);
	    }
	    updateAddress(updateAddresses);
	}
	
	private void copyPastePeoplePropertys(People people, People peopleInformation) {
		Set<String> NoPropertysSet = NullPropertys.getNullPropertyNames(peopleInformation);
	    NoPropertysSet.add("addresses");
	    NoPropertysSet.add("mainAddress");
	    
	    String[] NoPropertys = new String[NoPropertysSet.size()];
	    NoPropertysSet.toArray(NoPropertys);
	    
	    BeanUtils.copyProperties(peopleInformation, people, NoPropertys);
	}
	
	private List<Address> updateAddress(List<Address> addresses) {
		
		List<Long> ids = new ArrayList<>();
		for(Address address : addresses) {
			ids.add(address.getId());
		}
		Iterable<Long> addrIds = ids;
		
		List<Address> toUpdateAddresses = addressRepository.findAllById(addrIds);
	    
		copyAddressesToAddresses(addresses, toUpdateAddresses);
	    
		return addressRepository.saveAllAndFlush(toUpdateAddresses);
	}
	
	private People getPeopleById(Long id) {
		return peopleRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("People not found with id " + id));
	}
	
	private void copyAddressesToAddresses(List<Address> addresses_src, List<Address> addresses_dest) {
		
		for(int i = 0; i < addresses_src.size(); i++) {
	    	Set<String> NoPropertysSet = NullPropertys.getNullPropertyNames(addresses_src.get(i));
		    NoPropertysSet.add("id");
		    NoPropertysSet.add("people");
		    
		    String[] NoPropertys = new String[NoPropertysSet.size()];
		    NoPropertysSet.toArray(NoPropertys);
		    
	    	BeanUtils.copyProperties(addresses_src.get(i), addresses_dest.get(i), NoPropertys);
	    }
		
	}
	
	private void splitInsertUpdateAddr(People people, List<Address> addresses, List<Address> insertAddresses, List<Address> updateAddresses) {
		for(Address address : addresses) {
	    	address.setPeople(people);
	    	if(address.getId() == null) {
	    		insertAddresses.add(address);
	    	} else {
	    		updateAddresses.add(address);
	    	}
		}
	}

}