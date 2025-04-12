package com.demo.departments.demoDepartments.service;


import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;

import java.util.Optional;


/**
 * Service interface for Person entity operations
 */
public interface PersonService extends BaseService<PersonDTO> {

    Optional<Person> findByFull();
    Optional<Person> findById(Long id);
}