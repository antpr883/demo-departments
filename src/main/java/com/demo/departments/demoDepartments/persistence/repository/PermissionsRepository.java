package com.demo.departments.demoDepartments.persistence.repository;


import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionsRepository extends BaseCustomJpaRepository<Permissions, Long>, JpaSpecificationExecutor<Permissions> {
}