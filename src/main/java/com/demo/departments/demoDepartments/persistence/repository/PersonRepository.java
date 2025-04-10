package com.demo.departments.demoDepartments.persistence.repository;


import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PersonRepository extends BaseCustomJpaRepository<Person, Long> , JpaSpecificationExecutor<Person> {

     Optional<Person> findById(Long id , EntityGraph entityGraph);

}