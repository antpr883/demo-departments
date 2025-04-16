package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.Contact;
import com.demo.departments.demoDepartments.service.dto.ContactDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * Find all contacts for a specific person with configurable options
     * 
     * @param personId the ID of the person
     * @param withAudit whether to include audit information
     * @param attributes set of attributes to include
     * @return list of contact DTOs for the specified person
     */
    List<ContactDTO> findByPersonId(Long personId, boolean withAudit, Set<String> attributes);
    
}