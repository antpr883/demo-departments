package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Contact;
import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * MapStruct-based mapper for Contact entity
 */
@Mapper(componentModel = "spring", uses = {MapperUtils.class}, config = MapStructConfig.class)
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {

    @Override
    @Named("toDto")
    @Mapping(target = "personId", source = "person.id")
    ContactDTO toDto(Contact entity);

    @Override
    @Mapping(target = "person", ignore = true)
    Contact toEntity(ContactDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "person", ignore = true)
    void partialUpdate(@MappingTarget Contact entity, ContactDTO dto);

    /**
     * Maps contact with options to control property inclusion
     */
    @Named("toDtoWithOptions")
    @Mapping(target = "personId", source = "person.id", 
            conditionExpression = "java(options.levelOrIncludes(\"personId\", MappingLevel.SUMMARY) || MapperUtils.hasAncestorOfType(contact, Person.class))")
    ContactDTO toDtoWithOptions(Contact contact, @Context MappingOptions options);

    /**
     * Simplified DTO with minimal fields for list views
     */
    @Named("toSimpleDto") 
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    ContactDTO toSimpleDto(Contact contact);
}