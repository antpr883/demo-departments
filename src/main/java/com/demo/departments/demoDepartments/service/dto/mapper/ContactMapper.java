package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.ContactDTO;
import com.demo.departments.demoDepartments.persistence.model.Contact;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Contact entity and its DTOs
 */
@Mapper(config = CommonMapperConfig.class)
public interface ContactMapper {

    @Mapping(target = "id", expression = "java(options.includes(\"contacts\") ? entity.getId() : null)")
    @Mapping(target = "contactType", expression = "java(options.includes(\"contacts\") ? entity.getContactType() : null)")
    @Mapping(target = "phoneNumber", expression = "java(options.includes(\"contacts\") ? entity.getPhoneNumber() : null)")
    @Mapping(target = "email", expression = "java(options.includes(\"contacts\") ? entity.getEmail() : null)")
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "createdBy", expression = "java(options.includes(\"contacts\") ? entity.getCreatedBy() : null)")
    @Mapping(target = "createdDate", expression = "java(options.includes(\"contacts\") ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.includes(\"contacts\") ? entity.getModifiedBy() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.includes(\"contacts\") ? entity.getModifiedDate() : null)")
    ContactDTO toDto(Contact entity, @Context MappingOptions options);

    @InheritInverseConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Contact toEntity(ContactDTO dto, @Context MappingOptions options);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Contact entity, ContactDTO dto);

    default Set<ContactDTO> toDtoSet(Set<Contact> entities, @Context MappingOptions options) {
        return entities == null ? null : entities.stream().map(e -> toDto(e, options)).collect(Collectors.toSet());
    }

    default Set<Contact> toEntitySet(Set<ContactDTO> dtos, @Context MappingOptions options) {
        return dtos == null ? null : dtos.stream().map(d -> toEntity(d, options)).collect(Collectors.toSet());
    }
}
