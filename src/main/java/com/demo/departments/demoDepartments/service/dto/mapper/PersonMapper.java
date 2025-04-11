package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.PersonCreateUpdateDTO;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.PersonSummaryDTO;
import com.demo.departments.demoDepartments.persistence.model.Person;
import org.mapstruct.*;

import java.util.List;
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

    @Mapping(target = "id", expression = "java(options.isMinimal() || options.isBasicOrAbove() ? entity.getId() : null)")
    @Mapping(target = "fullName", expression = "java(options.isBasicOrAbove() ? entity.getFullName() : null)")
    @Mapping(target = "email", expression = "java(options.isBasicOrAbove() ? entity.getEmail() : null)")
    @Mapping(target = "birthDate", expression = "java(options.isBasicOrAbove() ? entity.getBirthDate() : null)")
    @Mapping(target = "roles", expression = "java(options.isSummaryOrAbove() ? roleMapper.toDtoSet(entity.getRoles(), options) : null)")
    @Mapping(target = "contacts", expression = "java(options.isComplete() ? contactMapper.toDtoSet(entity.getContacts(), options) : null)")
    @Mapping(target = "createdDate", expression = "java(options.isComplete() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.isComplete() ? entity.getModifiedDate() : null)")
    @Mapping(target = "createdBy", expression = "java(options.isComplete() ? entity.getCreatedBy() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.isComplete() ? entity.getModifiedBy() : null)")
    PersonDTO toDto(Person entity, @Context MappingOptions options);

    @InheritInverseConfiguration
    Person toEntity(PersonDTO dto, @Context MappingOptions options);

    default Set<PersonDTO> toDtoSet(Set<Person> entities, @Context MappingOptions options) {
        return entities == null ? null : entities.stream().map(e -> toDto(e, options)).collect(Collectors.toSet());
    }

    default Set<Person> toEntitySet(Set<PersonDTO> dtos, @Context MappingOptions options) {
        return dtos == null ? null : dtos.stream().map(d -> toEntity(d, options)).collect(Collectors.toSet());
    }
}
