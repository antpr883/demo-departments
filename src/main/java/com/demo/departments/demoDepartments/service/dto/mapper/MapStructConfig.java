package com.demo.departments.demoDepartments.service.dto.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Configuration for all MapStruct mappers
 */
@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {
        com.demo.departments.demoDepartments.persistence.model.Person.class,
        com.demo.departments.demoDepartments.persistence.model.security.Role.class
    }
)
public interface MapStructConfig {
}