package com.demo.departments.demoDepartments.service.dto.mapper;

import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base mapper interface with advanced mapping options for flexible DTO mapping
 *
 * @param <E> Entity type
 * @param <D> Full DTO type with all properties
 * @param <S> Summary DTO type with essential properties
 */
public interface GenericMapper<E, D, S> {

    /**
     * Maps entity to full DTO
     */
    @Named("toDto")
    D toDto(E entity);

    /**
     * Maps entity to summary DTO
     */
    @Named("toSummaryDto")
    S toSummaryDto(E entity);

    /**
     * Maps entity to DTO with specified mapping options
     */
    @Named("toDtoWithOptions")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    D toDtoWithOptions(E entity, @Context MappingOptions options);

    /**
     * Maps entity list to full DTO list
     */
    List<D> toDtoList(List<E> entities);

    /**
     * Maps entity list to summary DTO list
     */
    List<S> toSummaryDtoList(List<E> entities);

    /**
     * Maps entity list to DTO list with options
     */
    @Named("toDtoListWithOptions")
    default List<D> toDtoListWithOptions(List<E> entities, @Context MappingOptions options) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(entity -> toDtoWithOptions(entity, options))
                .collect(Collectors.toList());
    }

    /**
     * Maps entity list to DTO list by level
     */
    @Named("toDtoListByLevel")
    default List<D> toDtoListByLevel(List<E> entities, MappingLevel level) {
        if (entities == null) return Collections.emptyList();
        
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
                
        return toDtoListWithOptions(entities, options);
    }

    /**
     * Maps entity set to full DTO set
     */
    Set<D> toDtoSet(Set<E> entities);

    /**
     * Maps entity set to summary DTO set
     */
    Set<S> toSummaryDtoSet(Set<E> entities);
    
    /**
     * Maps entity set to DTO set with options
     */
    @Named("toDtoSetWithOptions")
    default Set<D> toDtoSetWithOptions(Set<E> entities, @Context MappingOptions options) {
        if (entities == null) return Collections.emptySet();
        return entities.stream()
                .map(entity -> toDtoWithOptions(entity, options))
                .collect(Collectors.toSet());
    }

    /**
     * Maps entity set to DTO set by level
     */
    @Named("toDtoSetByLevel")
    default Set<D> toDtoSetByLevel(Set<E> entities, MappingLevel level) {
        if (entities == null) return Collections.emptySet();
        
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
                
        return toDtoSetWithOptions(entities, options);
    }

    /**
     * Maps entity to DTO based on mapping level
     */
    @Named("toDtoByLevel")
    default D toDtoByLevel(E entity, MappingLevel level) {
        if (entity == null) return null;
        
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
                
        return toDtoWithOptions(entity, options);
    }
    

    /**
     * Maps entity to appropriate DTO based on mapping level
     */
    @Named("toAppropriateDto")
    default Object toAppropriateDto(E entity, MappingLevel level) {
        if (entity == null) return null;
        
        if (level == MappingLevel.MINIMAL || level == MappingLevel.BASIC) {
            return toSummaryDto(entity);
        } else {
            return toDto(entity);
        }
    }

    /**
     * Maps DTO to entity
     */
    @Named("toEntity")
    E toEntity(D dto);

    /**
     * Updates entity from DTO, ignoring null values
     */
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);

    /**
     * Maps DTO list to entity list
     */
    List<E> toEntityList(List<D> dtos);

    /**
     * Maps DTO set to entity set
     */
    Set<E> toEntitySet(Set<D> dtos);

    /**
     * Utility method to map a set of IDs to their presence in a set of entities
     */
    default boolean containsId(Set<Long> idSet, Long id) {
        return idSet != null && id != null && idSet.contains(id);
    }
}