package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Person entity operations
 */
public interface PersonService extends BaseService<PersonDTO> {

    /**
     * Find a person by ID with mapping determined by level
     * 
     * @param id Person ID
     * @param level Mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return PersonDTO with level-appropriate mapping
     */
    PersonDTO findById(Long id, MappingLevel level);
    
    /**
     * Find a person entity by ID without mapping to DTO
     */
    Optional<Person> findEntityById(Long id);
    
    /**
     * Find all persons with mapping determined by level
     * 
     * @param level Mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return List of PersonDTOs with level-appropriate mapping
     */
    List<PersonDTO> findAll(MappingLevel level);
    
    /**
     * Find a person by ID with full data based on requested attributes
     * 
     * @param id Person ID
     * @param attributes List of specific attributes to include
     * @return PersonDTO with attribute-based mapping
     */
    PersonDTO findByIdFull(Long id, List<String> attributes);
    
    /**
     * Find all persons with full data based on requested attributes
     * 
     * @param attributes List of specific attributes to include
     * @return List of PersonDTOs with attribute-based mapping
     */
    List<PersonDTO> findAllFull(List<String> attributes);
    
    /**
     * Save a new or update an existing person
     */
    PersonDTO save(PersonDTO personDTO);
    
    /**
     * Partially update a person
     */
    PersonDTO update(Long id, PersonDTO personDTO);
}