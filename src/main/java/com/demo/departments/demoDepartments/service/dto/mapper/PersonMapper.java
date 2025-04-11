package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
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
public interface PersonMapper extends EntityMapper<AddressDTO, Address>{

    @Mapping(target = "id", expression = "java(options.isMinimal() || options.isBasicOrAbove() ? entity.getId() : null)")
    @Mapping(target = "firstName", expression = "java(options.isBasicOrAbove() ? entity.getFirstName() : null)")
    @Mapping(target = "lastName", expression = "java(options.isBasicOrAbove() ? entity.getLastName() : null)")
    @Mapping(target = "birthDay", expression = "java(options.isBasicOrAbove() ? entity.birthDay() : null)")
    @Mapping(target = "roles", expression = "java(options.isSummaryOrAbove() ? roleMapper.toDtoSet(entity.getRoles(), options) : null)")
    @Mapping(target = "contacts", expression = "java(options.isComplete() ? contactMapper.toDtoSet(entity.getContacts(), options) : null)")
    @Mapping(target = "createdDate", expression = "java(options.isComplete() ? entity.getCreatedDate() : null)")
    @Mapping(target = "modifiedDate", expression = "java(options.isComplete() ? entity.getModifiedDate() : null)")
    @Mapping(target = "createdBy", expression = "java(options.isComplete() ? entity.getCreatedBy() : null)")
    @Mapping(target = "modifiedBy", expression = "java(options.isComplete() ? entity.getModifiedBy() : null)")
    PersonDTO toDto(Person entity, @Context MappingOptions options);

    @InheritInverseConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Address toEntity(AddressDTO dto, @Context MappingOptions options);


    @Override
    List<Address> toEntity(List<AddressDTO> dtoList);

    @Override
    List<AddressDTO> toDto(List<Address> entityList);

    @Override
    Address toEntity(AddressDTO dto);

    @Override
    AddressDTO toDto(Address entity);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Address entity, AddressDTO dto);
}
