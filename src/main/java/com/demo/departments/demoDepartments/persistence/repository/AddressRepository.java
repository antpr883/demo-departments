package com.demo.departments.demoDepartments.persistence.repository;


import com.demo.departments.demoDepartments.persistence.model.Address;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AddressRepository extends BaseCustomJpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    
    /**
     * Find all addresses for a person by person ID
     * 
     * @param personId the ID of the person
     * @return list of addresses for the specified person
     */
    List<Address> findByPersonId(Long personId);
    
    /**
     * Find all addresses for a person by person ID with entity graph
     * 
     * @param personId the ID of the person
     * @param entityGraph the entity graph to apply
     * @return list of addresses for the specified person
     */
    List<Address> findByPersonId(Long personId, EntityGraph entityGraph);
}