package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import org.mapstruct.*;

/**
 * MapStruct-based mapper for Role entity
 */
@Mapper(componentModel = "spring", uses = {PermissionsMapper.class, MapperUtils.class}, config = MapStructConfig.class)
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

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
    @Named("toDtoWithOptions")
    @Mapping(target = "personId", source = "person.id",
            conditionExpression = "java(options.levelOrIncludes(\"personId\", MappingLevel.BASIC) || MapperUtils.hasAncestorOfType(role, Person.class))")
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "toDtoWithOptions",
            conditionExpression = "java(options.levelOrIncludes(\"permissions\", MappingLevel.COMPLETE))")
    @Mapping(target = "permissionIds", ignore = true)
    RoleDTO toDtoWithOptions(Role role, @Context MappingOptions options);

    /**
     * Process permission IDs based on options after mapping
     */
    @AfterMapping
    default void processPermissionIdsBasedOnOptions(@MappingTarget RoleDTO dto, Role role, @Context MappingOptions options) {
        if (options.isBasicOrAbove() && role.getPermissions() != null) {
            dto.setPermissionIds(MapperUtils.extractIds(role.getPermissions()));
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