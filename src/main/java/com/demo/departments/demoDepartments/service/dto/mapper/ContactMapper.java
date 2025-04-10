package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.ContactDTO;
import com.demo.departments.demoDepartments.persistence.model.Contact;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Mapper interface for converting between Contact entity and its DTOs
 */
@Mapper(config = MapperConfig.class)
public abstract class ContactMapper {
    
    @Autowired
    protected PersonRepository personRepository;
    
    // Standard entity to DTO
    @Mapping(target = "personId", source = "person.id")
    public abstract ContactDTO toDto(Contact entity);
    
    // Flexible entity to DTO with options
    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? entity.getId() : null)")
    @Mapping(target = "createdDate", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedDate() : null)")
    @Mapping(target = "createdBy", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedBy() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedBy() : null)")
    public abstract ContactDTO toDto(Contact entity, MappingOptions options);
    
    // Standard list conversions
    public abstract List<ContactDTO> toDtoList(List<Contact> entities);
    
    // Flexible list with options
    public List<ContactDTO> toDtoList(List<Contact> entities, MappingOptions options) {
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
    public abstract Contact toEntity(ContactDTO dto);
    
    // Flexible DTO to Entity with options
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? dto.getId() : null)")
    public abstract Contact toEntity(ContactDTO dto, MappingOptions options);
    
    // List conversion
    public abstract List<Contact> toEntityList(List<ContactDTO> dtos);
    
    // Flexible list with options
    public List<Contact> toEntityList(List<ContactDTO> dtos, MappingOptions options) {
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
    public abstract void updateEntityFromDto(ContactDTO dto, @MappingTarget Contact entity);
    
    // Flexible update with options
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDto(ContactDTO dto, @MappingTarget Contact entity, MappingOptions options);
    
    // Handle person reference by ID
    @AfterMapping
    protected void setPerson(ContactDTO dto, @MappingTarget Contact entity) {
        if (dto.getPersonId() != null) {
            personRepository.findById(dto.getPersonId())
                .ifPresent(entity::addPerson);
        }
    }
}