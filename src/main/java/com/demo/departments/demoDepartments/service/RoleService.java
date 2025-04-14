package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Role entity operations
 */
public interface RoleService extends BaseService<RoleDTO> {
    
    /**
     * Find a role entity by ID without mapping to DTO
     */
    Optional<Role> findEntityById(Long id);
    
    /**
     * Save a new or update an existing role
     */
    RoleDTO save(RoleDTO roleDTO);
    
    /**
     * Partially update a role
     */
    RoleDTO update(Long id, RoleDTO roleDTO);
    
    /**
     * Find all roles for a specific person
     * 
     * @param personId the ID of the person
     * @param level the mapping level to apply
     * @return list of role DTOs for the specified person
     */
    List<RoleDTO> findByPersonId(Long personId, MappingLevel level);
}