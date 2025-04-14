package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;

import java.util.List;
import java.util.Optional;

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
     * Find all permissions for a specific role
     * 
     * @param roleId the ID of the role
     * @param level the mapping level to apply
     * @return list of permissions DTOs for the specified role
     */
    List<PermissionsDTO> findByRoleId(Long roleId, MappingLevel level);
}