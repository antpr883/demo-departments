package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;

import java.util.List;
import java.util.Optional;

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
     * Find all addresses for a specific person
     * 
     * @param personId the ID of the person
     * @param level the mapping level to apply
     * @return list of address DTOs for the specified person
     */
    List<AddressDTO> findByPersonId(Long personId, MappingLevel level);
}