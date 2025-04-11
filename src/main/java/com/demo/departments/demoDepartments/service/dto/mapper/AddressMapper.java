package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import com.demo.departments.demoDepartments.persistence.model.Address;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = CommonMapperConfig.class)
public interface AddressMapper {

    @Mapping(target = "id", expression = "java(entity.getId())")
    @Mapping(target = "country", expression = "java(options.isBasicOrAbove() ? entity.getCountry() : null)")
    @Mapping(target = "city", expression = "java(options.isBasicOrAbove() ? entity.getCity() : null)")
    @Mapping(target = "street", expression = "java(options.isBasicOrAbove() ? entity.getStreet() : null)")
    @Mapping(target = "createdBy", expression = "java(options.isComplete() ? entity.getCreatedBy() : null)")
    @Mapping(target = "createdDate", expression = "java(options.isComplete() ? entity.getCreatedDate() : null)")
    AddressDTO toDto(Address entity, @Context MappingOptions options);

    @InheritInverseConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Address toEntity(AddressDTO dto, @Context MappingOptions options);

    default Set<AddressDTO> toDtoSet(Set<Address> entities, @Context MappingOptions options) {
        return entities == null ? null : entities.stream().map(e -> toDto(e, options)).collect(Collectors.toSet());
    }

    default Set<Address> toEntitySet(Set<AddressDTO> dtos, @Context MappingOptions options) {
        return dtos == null ? null : dtos.stream().map(d -> toEntity(d, options)).collect(Collectors.toSet());
    }
}
