package com.ppessoas.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "address")
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String cep;
	private String address;
	private Integer number;
	private String city;
	
	@Version
	@JsonIgnore
	private Integer version;

	public void setId(Long id) {
		this.id = id;
	}
	
    public Long getId() {
		return id;
	}

	public String getCep() {
		return cep;
	}
    
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@JoinColumn(name = "people_id")
	@ManyToOne(targetEntity = People.class, fetch = FetchType.EAGER)
	@Nonnull
	@JsonBackReference
	private People people;
	
	public People getPeople() {
		return people;
	}

	public void setPeople(People people) {
		this.people = people;
	}
	
	@PreRemove
    private void removeAddressFromPeople() {
        people.removeAddress(this);
        people = null;
    }
	
	public void removePeople() {
		people = null;
	}
	
}
