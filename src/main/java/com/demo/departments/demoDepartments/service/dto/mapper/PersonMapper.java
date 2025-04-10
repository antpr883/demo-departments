package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.PersonCreateUpdateDTO;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.PersonSummaryDTO;
import com.demo.departments.demoDepartments.persistence.model.Person;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between Person entity and its DTOs
 */
@Mapper(
    config = MapperConfig.class,
    uses = {AddressMapper.class, ContactMapper.class, RoleMapper.class}
)
public interface PersonMapper {
    
    // Standard entity to DTO conversions
    PersonDTO toDto(Person entity);
    
    // Flexible entity to DTO mapping with options
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? entity.getId() : null)")
    @Mapping(target = "addresses", expression = "java(options.isIncludeAddresses() ? addressMapper.toDtoList(entity.getAddresses()) : null)")
    @Mapping(target = "contacts", expression = "java(options.isIncludeContacts() ? contactMapper.toDtoList(entity.getContacts()) : null)")
    @Mapping(target = "roles", expression = "java(options.isIncludeRoles() ? roleMapper.toDtoList(entity.getRoles()) : null)")
    @Mapping(target = "createdDate", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedDate() : null)")
    @Mapping(target = "createdBy", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedBy() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedBy() : null)")
    PersonDTO toDto(Person entity, MappingOptions options);
    
    // Standard summary conversion
    PersonSummaryDTO toSummaryDto(Person entity);
    
    // Flexible summary conversion with options
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? entity.getId() : null)")
    @Mapping(target = "createdDate", expression = "java(options.isIncludeAuditFields() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.isIncludeAuditFields() ? entity.getModifiedDate() : null)")
    PersonSummaryDTO toSummaryDto(Person entity, MappingOptions options);
    
    // Standard list conversions
    List<PersonDTO> toDtoList(List<Person> entities);
    List<PersonSummaryDTO> toSummaryDtoList(List<Person> entities);
    
    // Flexible list conversions with options
    default List<PersonDTO> toDtoList(List<Person> entities, MappingOptions options) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toDto(entity, options))
                .toList();
    }
    
    default List<PersonSummaryDTO> toSummaryDtoList(List<Person> entities, MappingOptions options) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(entity -> toSummaryDto(entity, options))
                .toList();
    }
    
    // Standard Create/Update DTO to entity
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Person toEntity(PersonCreateUpdateDTO dto);
    
    // Flexible Create/Update DTO to entity with options
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "id", expression = "java(options.isIncludeId() ? dto.getId() : null)")
    @Mapping(target = "addresses", expression = "java(options.isIncludeAddresses() ? addressMapper.toEntityList(dto.getAddresses()) : null)")
    @Mapping(target = "contacts", expression = "java(options.isIncludeContacts() ? contactMapper.toEntityList(dto.getContacts()) : null)")
    @Mapping(target = "roles", expression = "java(options.isIncludeRoles() ? roleMapper.toEntityList(dto.getRoles()) : null)")
    Person toEntity(PersonCreateUpdateDTO dto, MappingOptions options);
    
    // Standard update existing entity with DTO values
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PersonCreateUpdateDTO dto, @MappingTarget Person entity);
    
    // Flexible update with options
    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "addresses", expression = "java(options.isIncludeAddresses() ? addressMapper.toEntityList(dto.getAddresses()) : entity.getAddresses())")
    @Mapping(target = "contacts", expression = "java(options.isIncludeContacts() ? contactMapper.toEntityList(dto.getContacts()) : entity.getContacts())")
    @Mapping(target = "roles", expression = "java(options.isIncludeRoles() ? roleMapper.toEntityList(dto.getRoles()) : entity.getRoles())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PersonCreateUpdateDTO dto, @MappingTarget Person entity, MappingOptions options);
    
    // Custom mapping for related collections count in summary
    @AfterMapping
    default void setCollectionCounts(@MappingTarget PersonSummaryDTO dto, Person entity) {
        dto.setAddressCount(entity.getAddresses() != null ? entity.getAddresses().size() : 0);
        dto.setContactCount(entity.getContacts() != null ? entity.getContacts().size() : 0);
        dto.setRoleCount(entity.getRoles() != null ? entity.getRoles().size() : 0);
    }
}