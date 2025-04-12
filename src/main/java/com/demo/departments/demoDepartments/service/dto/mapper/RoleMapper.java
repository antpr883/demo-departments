package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Role entity and its DTOs
 */
@Mapper(config = CommonMapperConfig.class, uses = {PermissionsMapper.class})
public interface RoleMapper {

    @Mapping(target = "id", expression = "java(options.includes(\"roles\") ? entity.getId() : null)")
    @Mapping(target = "role", expression = "java(options.includes(\"roles\") ? entity.getRole() : null)")
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "permissions", expression = "java(options.includes(\"roles\") ? permissionsMapper.toDtoSet(entity.getPermissions(), options) : null)")
    @Mapping(target = "createdBy", expression = "java(options.includes(\"roles\") ? entity.getCreatedBy() : null)")
    @Mapping(target = "createdDate", expression = "java(options.includes(\"roles\") ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.includes(\"roles\") ? entity.getModifiedBy() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.includes(\"roles\") ? entity.getModifiedDate() : null)")
    RoleDTO toDto(Role entity, @Context MappingOptions options);

    @InheritInverseConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role toEntity(RoleDTO dto, @Context MappingOptions options);


    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Role entity, RoleDTO dto);

    default Set<RoleDTO> toDtoSet(Set<Role> entities, @Context MappingOptions options) {
        return entities == null ? null : entities.stream().map(e -> toDto(e, options)).collect(Collectors.toSet());
    }

    default Set<Role> toEntitySet(Set<RoleDTO> dtos, @Context MappingOptions options) {
        return dtos == null ? null : dtos.stream().map(d -> toEntity(d, options)).collect(Collectors.toSet());
    }
}