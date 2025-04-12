package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.persistence.model.Person;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Person entity and its DTOs
 */
@Mapper(
        config = CommonMapperConfig.class,
        uses = {AddressMapper.class, ContactMapper.class, RoleMapper.class}
)
public interface PersonMapper {

    @Mapping(target = "id", expression = "java(options.includes(\"id\") || options.isMinimal() || options.isBasicOrAbove() ? entity.getId() : null)")
    @Mapping(target = "firstName", expression = "java(options.includes(\"firstName\") || options.isBasicOrAbove() ? entity.getFirstName() : null)")
    @Mapping(target = "lastName", expression = "java(options.includes(\"lastName\") || options.isBasicOrAbove() ? entity.getLastName() : null)")
    @Mapping(target = "birthDay", expression = "java(options.includes(\"birthDay\") || options.isBasicOrAbove() ? entity.getBirthDay() : null)")
    @Mapping(target = "roles", expression = "java(options.includes(\"roles\") || options.isSummaryOrAbove() ? roleMapper.toDtoSet(entity.getRoles(), options) : null)")
    @Mapping(target = "contacts", expression = "java(options.includes(\"contacts\") || options.isComplete() ? contactMapper.toDtoSet(entity.getContacts(), options) : null)")
    @Mapping(target = "addresses", expression = "java(options.includes(\"addresses\") || options.isComplete() ? addressMapper.toDtoSet(entity.getAddresses(), options) : null)")
    @Mapping(target = "createdDate", expression = "java(options.includes(\"createdDate\") || options.isComplete() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.includes(\"modifiedDate\") || options.isComplete() ? entity.getModifiedDate() : null)")
    @Mapping(target = "createdBy", expression = "java(options.includes(\"createdBy\") || options.isComplete() ? entity.getCreatedBy() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.includes(\"modifiedBy\") || options.isComplete() ? entity.getModifiedBy() : null)")
    PersonDTO toDto(Person entity, @Context MappingOptions options);

    @InheritInverseConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Person toEntity(PersonDTO dto, @Context MappingOptions options);


    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Person entity, PersonDTO dto);

    default Set<PersonDTO> toDtoSet(Set<Person> entities, @Context MappingOptions options) {
        return entities == null ? null : entities.stream().map(e -> toDto(e, options)).collect(Collectors.toSet());
    }

    default Set<Person> toEntitySet(Set<PersonDTO> dtos, @Context MappingOptions options) {
        return dtos == null ? null : dtos.stream().map(d -> toEntity(d, options)).collect(Collectors.toSet());
    }
}