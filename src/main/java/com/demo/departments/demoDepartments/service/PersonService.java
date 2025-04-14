package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;

import java.util.Optional;

/**
 * Service interface for Person entity operations
 */
public interface PersonService extends BaseService<PersonDTO> {
    
    /**
     * Find a person entity by ID without mapping to DTO
     */
    Optional<Person> findEntityById(Long id);
    
    /**
     * Save a new or update an existing person
     */
    PersonDTO save(PersonDTO personDTO);
    
    /**
     * Partially update a person
     */
    PersonDTO update(Long id, PersonDTO personDTO);
}