package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.repository.RoleRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Permissions entity and its DTOs
 */
@Mapper(config = CommonMapperConfig.class)
public interface PermissionsMapper {

   @Mapping(target = "id", expression = "java(options.includes(\"permissions\") ? entity.getId() : null)")
    @Mapping(target = "permission", expression = "java(options.includes(\"permissions\") ? entity.getPermission() : null)")
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdBy", expression = "java(options.includes(\"permissions\") ? entity.getCreatedBy() : null)")
    @Mapping(target = "createdDate", expression = "java(options.includes(\"permissions\") ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.includes(\"permissions\") ? entity.getModifiedBy() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.includes(\"permissions\") ? entity.getModifiedDate() : null)")
    PermissionsDTO toDto(Permissions entity, @Context MappingOptions options);

    @InheritInverseConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Permissions toEntity(PermissionsDTO dto, @Context MappingOptions options);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Permissions entity, PermissionsDTO dto);

    default Set<PermissionsDTO> toDtoSet(Set<Permissions> entities, @Context MappingOptions options) {
        return entities == null ? null : entities.stream().map(e -> toDto(e, options)).collect(Collectors.toSet());
    }

    default Set<Permissions> toEntitySet(Set<PermissionsDTO> dtos, @Context MappingOptions options) {
        return dtos == null ? null : dtos.stream().map(d -> toEntity(d, options)).collect(Collectors.toSet());
    }
}