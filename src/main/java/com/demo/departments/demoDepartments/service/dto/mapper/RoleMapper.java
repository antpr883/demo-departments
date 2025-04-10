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
public abstract class RoleMapper {
    
    @Autowired
    protected PersonRepository personRepository;
    
    // Standard entity to DTO conversion
    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "permissionIds", ignore = true)
    public abstract RoleDTO toDto(Role entity);
    
    // Flexible entity to DTO with options
    @Mapping(target = "personId", source = "entity.person.id")
    @Mapping(target = "permissionIds", ignore = true)
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? entity.getId() : null)")
    @Mapping(target = "createdDate", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedDate() : null)")
    @Mapping(target = "createdBy", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedBy() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedBy() : null)")
    public abstract RoleDTO toDto(Role entity, MappingOptions options);
    
    // Standard list conversions
    public abstract List<RoleDTO> toDtoList(List<Role> entities);
    public abstract Set<RoleDTO> toDtoList(Set<Role> entities);
    
    // Flexible list with options
    public List<RoleDTO> toDtoList(List<Role> entities, MappingOptions options) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toDto(entity, options))
                .collect(Collectors.toList());
    }
    
    public Set<RoleDTO> toDtoList(Set<Role> entities, MappingOptions options) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toDto(entity, options))
                .collect(Collectors.toSet());
    }
    
    // Standard DTO to Entity conversion
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract Role toEntity(RoleDTO dto);
    
    // Flexible DTO to Entity with options
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? dto.getId() : null)")
    public abstract Role toEntity(RoleDTO dto, MappingOptions options);
    
    // List conversion
    public abstract List<Role> toEntityList(List<RoleDTO> dtos);
    public abstract Set<Role> toEntityList(Set<RoleDTO> dtos);
    
    // Flexible list with options
    public List<Role> toEntityList(List<RoleDTO> dtos, MappingOptions options) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(dto -> toEntity(dto, options))
                .collect(Collectors.toList());
    }
    
    public Set<Role> toEntityList(Set<RoleDTO> dtos, MappingOptions options) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(dto -> toEntity(dto, options))
                .collect(Collectors.toSet());
    }
    
    // Standard update existing entity with DTO values
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDto(RoleDTO dto, @MappingTarget Role entity);
    
    // Flexible update with options
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDto(RoleDTO dto, @MappingTarget Role entity, MappingOptions options);
    
    // After mapping, set up the permission IDs for the DTO
    @AfterMapping
    protected void setPermissionIds(Role entity, @MappingTarget RoleDTO dto) {
        if (entity.getPermissions() != null && !entity.getPermissions().isEmpty()) {
            dto.setPermissionIds(entity.getPermissions().stream()
                .map(permission -> permission.getId())
                .collect(Collectors.toSet()));
        }
    }
    
    // After mapping, set up person reference
    @AfterMapping
    protected void setPerson(RoleDTO dto, @MappingTarget Role entity) {
        if (dto.getPersonId() != null) {
            personRepository.findById(dto.getPersonId())
                .ifPresent(entity::setPerson);
        }
    }
}