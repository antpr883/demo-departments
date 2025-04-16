package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for Address entity operations
 */
public interface AddressService extends BaseService<AddressDTO> {
    
    /**
     * Find an address entity by ID without mapping to DTO
     */
    Optional<Address> findEntityById(Long id);
    
    /**
     * Save a new or update an existing address
     */
    AddressDTO save(AddressDTO addressDTO);
    
    /**
     * Partially update an address
     */
    AddressDTO update(Long id, AddressDTO addressDTO);
    
    /**
     * Find all addresses for a specific person with configurable options
     * 
     * @param personId the ID of the person
     * @param withAudit whether to include audit information
     * @param attributes set of attributes to include
     * @return list of address DTOs for the specified person
     */
    List<AddressDTO> findByPersonId(Long personId, boolean withAudit, Set<String> attributes);
    
}