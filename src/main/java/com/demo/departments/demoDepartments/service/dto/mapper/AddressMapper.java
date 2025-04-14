package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * MapStruct-based mapper for Address entity
 */
@Mapper(componentModel = "spring", uses = {MapperUtils.class}, config = MapStructConfig.class)
public interface AddressMapper extends EntityMapper<Address, AddressDTO> {

    @Override
    @Named("toDto")
    @Mapping(target = "personId", source = "person.id")
    AddressDTO toDto(Address entity);

    @Override
    @Mapping(target = "person", ignore = true)
    Address toEntity(AddressDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "person", ignore = true)
    void partialUpdate(@MappingTarget Address entity, AddressDTO dto);

    /**
     * Maps address with options to control property inclusion
     */
    @Override
    @Named("toDtoWithOptions")
    @Mapping(target = "personId", source = "person.id", 
            conditionExpression = "java(options.isSummaryOrAbove() || MapperUtils.hasAncestorOfType(entity, Person.class))")
    AddressDTO toDtoWithOptions(Address entity, @Context MappingOptions options);
    
    /**
     * Process BaseDTO fields based on options
     */
    @AfterMapping
    default void processBaseDtoFields(@MappingTarget AddressDTO dto, Address entity, @Context MappingOptions options) {
        // Handle BaseDTO fields - only include for BASIC level or above
        if (!options.isBasicOrAbove()) {
            // For MINIMAL level, clear all BaseDTO fields except ID
            Long id = dto.getId(); // Save the ID
            dto.setCreatedDate(null);
            dto.setModifiedDate(null);
            dto.setCreatedBy(null);
            dto.setModifiedBy(null);
            dto.setId(id); // Restore the ID
        }
    }

    /**
     * Simplified DTO with minimal fields for list views
     */
    @Named("toSimpleDto")
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    AddressDTO toSimpleDto(Address address);
}