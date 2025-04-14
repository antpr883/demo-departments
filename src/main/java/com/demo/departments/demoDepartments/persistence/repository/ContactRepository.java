package com.demo.departments.demoDepartments.persistence.repository;


import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Contact;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ContactRepository extends BaseCustomJpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    
    /**
     * Find all contacts for a person by person ID
     * 
     * @param personId the ID of the person
     * @return list of contacts for the specified person
     */
    List<Contact> findByPersonId(Long personId);
    
    /**
     * Find all contacts for a person by person ID with entity graph
     * 
     * @param personId the ID of the person
     * @param entityGraph the entity graph to apply
     * @return list of contacts for the specified person
     */
    List<Contact> findByPersonId(Long personId, EntityGraph entityGraph);
}