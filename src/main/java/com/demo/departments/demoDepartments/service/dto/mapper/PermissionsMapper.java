package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
import org.mapstruct.*;

/**
 * MapStruct-based mapper for Permissions entity
 */
@Mapper(componentModel = "spring", uses = {MapperUtils.class}, config = MapStructConfig.class)
public interface PermissionsMapper extends EntityMapper<PermissionsDTO, Permissions> {

    @Override
    @Named("toDto")
    @Mapping(target = "roleId", source = "role.id")
    PermissionsDTO toDto(Permissions entity);

    @Override
    @Mapping(target = "role", ignore = true)
    Permissions toEntity(PermissionsDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    void partialUpdate(@MappingTarget Permissions entity, PermissionsDTO dto);

    /**
     * Maps permissions with options to control property inclusion
     */
    @Named("toDtoWithOptions")
    @Mapping(target = "roleId", source = "role.id",
            conditionExpression = "java(options.levelOrIncludes(\"roleId\", MappingLevel.SUMMARY) || MapperUtils.hasAncestorOfType(permissions, Role.class) || MapperUtils.hasAncestorOfType(permissions, Person.class))")
    PermissionsDTO toDtoWithOptions(Permissions permissions, @Context MappingOptions options);

    /**
     * Simplified DTO with minimal fields for list views
     */
    @Named("toSimpleDto")
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    PermissionsDTO toSimpleDto(Permissions permissions);
}