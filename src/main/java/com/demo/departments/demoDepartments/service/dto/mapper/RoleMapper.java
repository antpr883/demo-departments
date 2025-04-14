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
    @Mapping(target = "permissionIds", expression = "java(MapperUtils.extractIds(entity.getPermissions()))")
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
            conditionExpression = "java(options.isSummaryOrAbove() || MapperUtils.hasAncestorOfType(entity, Person.class))")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "toDtoWithOptions",
            conditionExpression = "java(options.isCompleteOrAbove() && options.levelOrIncludes(\"permissions\", MappingLevel.COMPLETE))")
    RoleDTO toDtoWithOptions(Role entity, @Context MappingOptions options);

    /**
     * Process fields based on mapping options
     */
    @AfterMapping
    default void processFieldsBasedOnOptions(@MappingTarget RoleDTO dto, Role role, @Context MappingOptions options) {
        // Handle BaseDTO fields - only include for BASIC level or above
        if (!options.isBasicOrAbove()) {
            // For MINIMAL level, clear all BaseDTO fields except ID
            Long id = dto.getId(); // Save the ID
            dto.setCreatedDate(null);
            dto.setModifiedDate(null);
            dto.setCreatedBy(null);
            dto.setModifiedBy(null);
            dto.setId(id); // Restore the ID
        }
        
        // Handle permissions collections
        if (options.isSummaryOrAbove() && role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            dto.setPermissionIds(MapperUtils.extractIds(role.getPermissions()));
        } else if (!options.isSummaryOrAbove()) {
            // For MINIMAL and BASIC levels, explicitly set collections to empty
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