package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.PersonSummaryDTO;
import org.mapstruct.*;

/**
 * MapStruct-based mapper for Person entity
 */
@Mapper(componentModel = "spring", 
        uses = {AddressMapper.class, ContactMapper.class, RoleMapper.class, MapperUtils.class}, 
        config = MapStructConfig.class)
public interface PersonMapper extends GenericMapper<Person, PersonDTO, PersonSummaryDTO> {

    @Override
    @Named("toDto")
    @Mapping(target = "addressIds", expression = "java(MapperUtils.extractIds(person.getAddresses()))")
    @Mapping(target = "contactIds", expression = "java(MapperUtils.extractIds(person.getContacts()))") 
    @Mapping(target = "roleIds", expression = "java(MapperUtils.extractIds(person.getRoles()))")
    @Mapping(target = "addresses", source = "addresses", qualifiedByName = "toDto")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "toDto")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toDto")
    PersonDTO toDto(Person person);

    @Override
    @Named("toSummaryDto")
    @Mapping(target = "addressCount", expression = "java(MapperUtils.safeCount(person.getAddresses()))")
    @Mapping(target = "contactCount", expression = "java(MapperUtils.safeCount(person.getContacts()))")
    @Mapping(target = "roleCount", expression = "java(MapperUtils.safeCount(person.getRoles()))")
    @Mapping(target = "birthday", source = "birthDay")
    PersonSummaryDTO toSummaryDto(Person person);

    @Override
    @Named("toDtoWithOptions")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "addresses", source = "addresses", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.levelOrIncludes(\"addresses\", MappingLevel.COMPLETE))")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.levelOrIncludes(\"contacts\", MappingLevel.COMPLETE))")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toDtoWithOptions", 
            conditionExpression = "java(options.levelOrIncludes(\"roles\", MappingLevel.COMPLETE))")
    PersonDTO toDtoWithOptions(Person person, @Context MappingOptions options);

    /**
     * Special method to extract IDs based on options
     */
    @AfterMapping
    default void processIdsBasedOnOptions(@MappingTarget PersonDTO dto, Person person, @Context MappingOptions options) {
        // Only include IDs when BASIC or above
        if (options.isBasicOrAbove()) {
            dto.setAddressIds(MapperUtils.extractIds(person.getAddresses()));
            dto.setContactIds(MapperUtils.extractIds(person.getContacts()));
            dto.setRoleIds(MapperUtils.extractIds(person.getRoles()));
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