package com.demo.departments.demoDepartments.persistence.repository;


import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionsRepository extends BaseCustomJpaRepository<Permissions, Long>, JpaSpecificationExecutor<Permissions> {
    
    /**
     * Find all permissions for a role by role ID
     * 
     * @param roleId the ID of the role
     * @return list of permissions for the specified role
     */
    List<Permissions> findByRoleId(Long roleId);
    
    /**
     * Find all permissions for a role by role ID with entity graph
     * 
     * @param roleId the ID of the role
     * @param entityGraph the entity graph to apply
     * @return list of permissions for the specified role
     */
    List<Permissions> findByRoleId(Long roleId, EntityGraph entityGraph);
}