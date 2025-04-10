package com.demo.departments.demoDepartments.persistence.repository;


import com.demo.departments.demoDepartments.persistence.model.Contact;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactRepository extends BaseCustomJpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
}