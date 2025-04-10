package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.repository.RoleRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Mapper for converting between Permissions entity and its DTOs
 */
@Mapper(config = MapperConfig.class)
public abstract class PermissionsMapper {

    @Autowired
    protected RoleRepository roleRepository;
    
    // Entity to DTO conversion
    @Mapping(target = "roleId", source = "role.id")
    public abstract PermissionsDTO toDto(Permissions entity);
    
    public abstract List<PermissionsDTO> toDtoList(List<Permissions> entities);
    
    // DTO to Entity conversion
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract Permissions toEntity(PermissionsDTO dto);
    
    // Update existing entity with DTO values
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDto(PermissionsDTO dto, @MappingTarget Permissions entity);
    
    // After mapping, set up role reference
    @AfterMapping
    protected void setRole(PermissionsDTO dto, @MappingTarget Permissions entity) {
        if (dto.getRoleId() != null) {
            roleRepository.findById(dto.getRoleId())
                .ifPresent(entity::setRole);
        }
    }
}