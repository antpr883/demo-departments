package com.demo.departments.demoDepartments.persistence.repository;


import com.demo.departments.demoDepartments.persistence.model.security.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends BaseCustomJpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
}