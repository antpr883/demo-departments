package com.demo.departments.demoDepartments.service;


import com.demo.departments.demoDepartments.persistence.model.Person;

import java.util.Optional;


/**
 * Service interface for Person entity operations
 */
public interface PersonService extends BaseService<Person> {

    Optional<Person> findByFull();
    Optional<Person> findById(Long id);
}