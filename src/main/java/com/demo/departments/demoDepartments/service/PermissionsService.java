package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for Permissions entity operations
 */
public interface PermissionsService extends BaseService<PermissionsDTO> {
    
    /**
     * Find a permissions entity by ID without mapping to DTO
     */
    Optional<Permissions> findEntityById(Long id);
    
    /**
     * Save a new or update existing permissions
     */
    PermissionsDTO save(PermissionsDTO permissionsDTO);
    
    /**
     * Partially update permissions
     */
    PermissionsDTO update(Long id, PermissionsDTO permissionsDTO);
    
    /**
     * Find all permissions for a specific role with configurable options
     * 
     * @param roleId the ID of the role
     * @param withAudit whether to include audit information
     * @param attributes set of attributes to include
     * @return list of permissions DTOs for the specified role
     */
    List<PermissionsDTO> findByRoleId(Long roleId, boolean withAudit, Set<String> attributes);
    
}