package com.ppessoas.api.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "people")
public class People {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Nonnull
	private String name;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Nonnull
	private Date dateOfBirth;

	@OneToMany(cascade = CascadeType.ALL, targetEntity = Address.class, mappedBy = "people", fetch = FetchType.EAGER)
	@Nonnull
	@JsonManagedReference
	private Set<Address> addresses;

	@JoinColumn(name = "mainAddress_id")
	@OneToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
	@Nullable
	private Address mainAddress;
	
	@Version
	@JsonIgnore
	private Integer version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Address getMainAddress() {
		return mainAddress;
	}

	public void setMainAddress(Address mainAddress) {
		this.mainAddress = mainAddress;
	}
	
	public void addAddresses(Set<Address> addresses) {
		this.addresses.addAll(addresses);
	}
	
	public void addAddresses(List<Address> addresses) {
		this.addresses.addAll(addresses);
	}
    
    public void removeAddress(Address address){
        if(addresses.contains(address)) {
        	addresses.remove(address);
        }
        if(address.equals(mainAddress)) {
        	if(addresses.size() > 0) {
        		mainAddress = addresses.iterator().next();
        	} else {
        		mainAddress = null;
        	}
        }
    }
    
    @PreRemove
    private void removePeopleFromAddresses() {
        for(Address address : addresses) {
        	address.removePeople();
        }
        addresses.clear();
    }
    
    
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		People other = (People) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}