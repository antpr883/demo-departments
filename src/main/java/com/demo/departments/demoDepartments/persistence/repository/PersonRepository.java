package com.demo.departments.demoDepartments.persistence.repository;


import com.demo.departments.demoDepartments.persistence.model.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonRepository extends BaseCustomJpaRepository<Person, Long> , JpaSpecificationExecutor<Person> {
}