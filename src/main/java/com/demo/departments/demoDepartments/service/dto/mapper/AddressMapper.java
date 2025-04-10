package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Mapper interface for converting between Address entity and its DTOs
 */
@Mapper(config = CommonMapperConfig.class)
public abstract class AddressMapper {
    
    @Autowired
    protected PersonRepository personRepository;
    
    // Standard entity to DTO
    @Mapping(target = "personId", source = "person.id")
    public abstract AddressDTO toDto(Address entity);
    
    // Flexible entity to DTO with options
    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? entity.getId() : null)")
    @Mapping(target = "createdDate", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedDate() : null)")
    @Mapping(target = "createdBy", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedBy() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedBy() : null)")
    public abstract AddressDTO toDto(Address entity, MappingOptions options);
    
    // Standard list conversions
    public abstract List<AddressDTO> toDtoList(List<Address> entities);
    
    // Flexible list with options
    public List<AddressDTO> toDtoList(List<Address> entities, MappingOptions options) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toDto(entity, options))
                .toList();
    }
    
    // Standard DTO to Entity
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract Address toEntity(AddressDTO dto);
    
    // Flexible DTO to Entity with options
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? dto.getId() : null)")
    public abstract Address toEntity(AddressDTO dto, MappingOptions options);
    
    // List conversion
    public abstract List<Address> toEntityList(List<AddressDTO> dtos);
    
    // Flexible list with options
    public List<Address> toEntityList(List<AddressDTO> dtos, MappingOptions options) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(dto -> toEntity(dto, options))
                .toList();
    }
    
    // Standard update existing entity with DTO values
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDto(AddressDTO dto, @MappingTarget Address entity);
    
    // Flexible update with options
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDto(AddressDTO dto, @MappingTarget Address entity, MappingOptions options);
    
    // Handle person reference by ID
    @AfterMapping
    protected void setPerson(AddressDTO dto, @MappingTarget Address entity) {
        if (dto.getPersonId() != null) {
            personRepository.findById(dto.getPersonId())
                .ifPresent(entity::addPerson);
        }
    }
}