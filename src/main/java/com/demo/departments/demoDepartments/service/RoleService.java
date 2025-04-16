package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * Find all roles for a specific person with configurable options
     * 
     * @param personId the ID of the person
     * @param withAudit whether to include audit information
     * @param attributes set of attributes to include
     * @return list of role DTOs for the specified person
     */
    List<RoleDTO> findByPersonId(Long personId, boolean withAudit, Set<String> attributes);
    
}