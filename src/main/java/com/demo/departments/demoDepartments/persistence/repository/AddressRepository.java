package com.demo.departments.demoDepartments.persistence.repository;


import com.demo.departments.demoDepartments.persistence.model.Address;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressRepository extends BaseCustomJpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
}