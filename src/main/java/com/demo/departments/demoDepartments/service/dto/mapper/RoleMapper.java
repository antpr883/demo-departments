package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import org.mapstruct.*;

import java.util.Collections;

/**
 * MapStruct-based mapper for Role entity
 */
@Mapper(componentModel = "spring", uses = {PermissionsMapper.class, MapperUtils.class}, config = MapStructConfig.class)
public interface RoleMapper extends EntityMapper<Role, RoleDTO> {

    @Override
    @Named("toDto")
    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "toDto")
    @Mapping(target = "permissionIds", expression = "java(entity.getPermissions() != null ? MapperUtils.extractIds(entity.getPermissions()) : java.util.Collections.emptySet())")
    RoleDTO toDto(Role entity);

    @Override
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void partialUpdate(@MappingTarget Role entity, RoleDTO dto);

    /**
     * Maps role with options to control property inclusion
     */
    @Override
    @Named("toDtoWithOptions")
    @Mapping(target = "personId", source = "person.id",
            conditionExpression = "java(options.includesPath(\"person\") || MapperUtils.hasAncestorOfType(entity, Person.class))")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "toDtoWithOptions",
            conditionExpression = "java(options.includesPath(\"permissions\") || MapperUtils.hasPermissionsInNestedPath(options))")
    RoleDTO toDtoWithOptions(Role entity, @Context MappingOptions options);

    /**
     * Process fields based on mapping options
     */
    @AfterMapping
    default void processFieldsBasedOnOptions(@MappingTarget RoleDTO dto, Role role, @Context MappingOptions options) {
        // Handle BaseDTO fields - only include audit information if requested
        if (!options.includeAudit()) {
            // If audit information is not requested, clear all audit fields except ID
            Long id = dto.getId(); // Save the ID
            dto.setCreatedDate(null);
            dto.setModifiedDate(null);
            dto.setCreatedBy(null);
            dto.setModifiedBy(null);
            dto.setId(id); // Restore the ID
        }
        
        // Handle permissions based on whether they were requested
        boolean includePermissions = options.includesPath("permissions") || MapperUtils.hasPermissionsInNestedPath(options);
        
        if (includePermissions) {
            // Include permissions if available and requested
            if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                // Only set the IDs if they haven't been set yet
                if (dto.getPermissionIds() == null || dto.getPermissionIds().isEmpty()) {
                    dto.setPermissionIds(MapperUtils.extractIds(role.getPermissions()));
                }
            }
        } else {
            // If permissions not requested, clear them to avoid extra data transfer
            dto.setPermissions(Collections.emptySet());
            dto.setPermissionIds(Collections.emptySet());
        }
    }

    /**
     * Simplified DTO with minimal fields for list views
     */
    @Named("toSimpleDto")
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "permissionIds", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    RoleDTO toSimpleDto(Role role);
}