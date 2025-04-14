package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.Contact;
import com.demo.departments.demoDepartments.service.dto.ContactDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Contact entity operations
 */
public interface ContactService extends BaseService<ContactDTO> {
    
    /**
     * Find a contact entity by ID without mapping to DTO
     */
    Optional<Contact> findEntityById(Long id);
    
    /**
     * Save a new or update an existing contact
     */
    ContactDTO save(ContactDTO contactDTO);
    
    /**
     * Partially update a contact
     */
    ContactDTO update(Long id, ContactDTO contactDTO);
    
    /**
     * Find all contacts for a specific person
     * 
     * @param personId the ID of the person
     * @param level the mapping level to apply
     * @return list of contact DTOs for the specified person
     */
    List<ContactDTO> findByPersonId(Long personId, MappingLevel level);
}