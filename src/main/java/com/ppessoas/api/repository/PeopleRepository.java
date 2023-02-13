package com.ppessoas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppessoas.api.model.People;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {

}
