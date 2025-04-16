package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import org.mapstruct.*;

import java.util.Collections;

/**
 * MapStruct-based mapper for Person entity
 */
@Mapper(componentModel = "spring", 
        uses = {AddressMapper.class, ContactMapper.class, RoleMapper.class, MapperUtils.class}, 
        config = MapStructConfig.class)
public interface PersonMapper extends EntityMapper<Person, PersonDTO> {

    @Override
    @Named("toDto")
    @Mapping(target = "addressIds", expression = "java(MapperUtils.extractIds(person.getAddresses()))")
    @Mapping(target = "contactIds", expression = "java(MapperUtils.extractIds(person.getContacts()))") 
    @Mapping(target = "roleIds", expression = "java(MapperUtils.extractIds(person.getRoles()))")
    @Mapping(target = "addresses", source = "addresses", qualifiedByName = "toDto")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "toDto")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toDto")
    @Mapping(target = "addressCount", expression = "java(MapperUtils.safeCount(person.getAddresses()))")
    @Mapping(target = "contactCount", expression = "java(MapperUtils.safeCount(person.getContacts()))")
    @Mapping(target = "roleCount", expression = "java(MapperUtils.safeCount(person.getRoles()))")
    PersonDTO toDto(Person person);

    @Override
    @Named("toDtoWithOptions")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "addresses", source = "addresses", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.includesPath(\"addresses\"))")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.includesPath(\"contacts\"))")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.includesPath(\"roles\") || MapperUtils.hasNestedRolesPath(options))")
    @Mapping(target = "password", source = "password",
            conditionExpression = "java(options.includes(\"password\"))")
    PersonDTO toDtoWithOptions(Person person, @Context MappingOptions options);

    /**
     * Special method to process DTO based on mapping options
     */
    @AfterMapping
    default void processFieldsBasedOnOptions(@MappingTarget PersonDTO dto, Person person, @Context MappingOptions options) {
        // Handle audit fields
        if (!options.includeAudit()) {
            // If audit information is disabled, clear all audit fields except ID
            Long id = dto.getId(); // Save the ID
            dto.setCreatedDate(null);
            dto.setModifiedDate(null);
            dto.setCreatedBy(null);
            dto.setModifiedBy(null);
            dto.setId(id); // Restore the ID
        }
        
        // Handle password field - only include for certain conditions
        // For security, always exclude password unless explicitly requested
        if (!options.includes("password")) {
            dto.setPassword(null);
        }
        
        // Handle collection entities based on attributes
        // Addresses
        if (options.includesPath("addresses")) {
            if (person.getAddresses() != null && !person.getAddresses().isEmpty()) {
                // Already mapped in the main mapper method
                dto.setAddressIds(MapperUtils.extractIds(person.getAddresses()));
                dto.setAddressCount(person.getAddresses().size());
            }
        } else {
            dto.setAddresses(Collections.emptySet());
            dto.setAddressIds(Collections.emptySet());
            dto.setAddressCount(0);
        }
        
        // Contacts
        if (options.includesPath("contacts")) {
            if (person.getContacts() != null && !person.getContacts().isEmpty()) {
                dto.setContactIds(MapperUtils.extractIds(person.getContacts()));
                dto.setContactCount(person.getContacts().size());
            }
        } else {
            dto.setContacts(Collections.emptySet());
            dto.setContactIds(Collections.emptySet());
            dto.setContactCount(0);
        }
        
        // Roles
        boolean includeRoles = options.includesPath("roles") || MapperUtils.hasNestedRolesPath(options);
        
        if (includeRoles) {
            if (person.getRoles() != null && !person.getRoles().isEmpty()) {
                dto.setRoleIds(MapperUtils.extractIds(person.getRoles()));
                dto.setRoleCount(person.getRoles().size());
            }
        } else {
            dto.setRoles(Collections.emptySet());
            dto.setRoleIds(Collections.emptySet());
            dto.setRoleCount(0);
        }
    }

    @Override
    @Named("toEntity")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Person toEntity(PersonDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void partialUpdate(@MappingTarget Person entity, PersonDTO dto);

    /**
     * After mapping, handle collections and bidirectional relationships
     */
    @AfterMapping
    default void handleCollectionsForEntity(@MappingTarget Person person, PersonDTO dto) {
        // Implementation would handle bidirectional relationships and collections
        // This would be where we'd set up relationships with addresses, contacts, and roles
    }
}