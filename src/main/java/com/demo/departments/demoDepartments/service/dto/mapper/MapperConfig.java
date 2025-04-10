package com.demo.departments.demoDepartments.service.dto.mapper;

import org.mapstruct.ReportingPolicy;

/**
 * Common configuration for all MapStruct mappers
 */
@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapperConfig {
}