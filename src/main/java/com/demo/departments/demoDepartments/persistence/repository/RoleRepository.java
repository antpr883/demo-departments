package com.demo.departments.demoDepartments.persistence.repository;


import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleRepository extends BaseCustomJpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    
    /**
     * Find all roles for a person by person ID
     * 
     * @param personId the ID of the person
     * @return list of roles for the specified person
     */
    List<Role> findByPersonId(Long personId);
    
    /**
     * Find all roles for a person by person ID with entity graph
     * 
     * @param personId the ID of the person
     * @param entityGraph the entity graph to apply
     * @return list of roles for the specified person
     */
    List<Role> findByPersonId(Long personId, EntityGraph entityGraph);
}