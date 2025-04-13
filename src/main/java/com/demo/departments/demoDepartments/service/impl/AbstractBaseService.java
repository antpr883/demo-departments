package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;
import com.demo.departments.demoDepartments.persistence.repository.BaseCustomJpaRepository;
import com.demo.departments.demoDepartments.service.BaseService;
import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.GenericMapper;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Abstract base service implementation that provides common CRUD operations with flexible mapping
 *
 * @param <E> Entity type
 * @param <D> DTO type
 * @param <S> Summary DTO type
 * @param <R> Repository type
 * @param <M> Mapper type
 */
@Transactional
@RequiredArgsConstructor
public abstract class AbstractBaseService<
        E extends PersistenceModel,
        D extends BaseDTO,
        S extends BaseDTO,
        R extends BaseCustomJpaRepository<E, Long>,
        M extends GenericMapper<E, D, S>> implements BaseService<D> {

    protected final R repository;
    protected final M mapper;
    protected final GraphBuilderMapperService graphBuilderService;
    protected final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    protected AbstractBaseService(R repository, M mapper, GraphBuilderMapperService graphBuilderService) {
        this.repository = repository;
        this.mapper = mapper;
        this.graphBuilderService = graphBuilderService;
        
        // Extract the entity class type using reflection
        this.entityClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public D findById(Long id, MappingLevel level) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
        
        return mapper.toDtoWithOptions(entity, options);
    }

    @Override
    @Transactional(readOnly = true)
    public D findByIdFull(Long id, List<String> attributes) {
        // Convert list to set for faster lookups
        Set<String> attributeSet = attributes != null ? new HashSet<>(attributes) : Collections.emptySet();
        
        // Generate appropriate entity graph
        EntityGraph graph;
        if (attributeSet.isEmpty()) {
            // If no attributes specified, get full entity graph
            graph = graphBuilderService.getCompleteEntityGraph(entityClass);
        } else {
            // Otherwise get graph just for requested attributes
            graph = graphBuilderService.getGraphWithAttributes(entityClass, attributeSet);
        }

        // Fetch entity with the dynamic graph
        E entity = repository.findById(id, graph)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

        // Create mapping options
        MappingOptions options;
        if (attributeSet.isEmpty()) {
            // If no attributes specified, return complete mapping
            options = MappingOptions.builder()
                    .level(MappingLevel.COMPLETE)
                    .build();
        } else {
            // Otherwise return only requested attributes with SUMMARY level
            options = MappingOptions.builder()
                    .fields(attributeSet)
                    .level(MappingLevel.SUMMARY)
                    .build();
        }

        // Map entity to DTO with options
        return mapper.toDtoWithOptions(entity, options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> findAll(MappingLevel level) {
        List<E> entities = repository.findAll();
        
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
        
        return mapper.toDtoListWithOptions(entities, options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> findAllFull(List<String> attributes) {
        // Convert list to set for faster lookups
        Set<String> attributeSet = attributes != null ? new HashSet<>(attributes) : Collections.emptySet();
        
        // Generate appropriate entity graph
        EntityGraph graph;
        if (attributeSet.isEmpty()) {
            // If no attributes specified, get full entity graph
            graph = graphBuilderService.getCompleteEntityGraph(entityClass);
        } else {
            // Otherwise get graph just for requested attributes
            graph = graphBuilderService.getGraphWithAttributes(entityClass, attributeSet);
        }

        // Fetch entities with the dynamic graph
        Iterable<E> entitiesIterable = repository.findAll(graph);
        List<E> entities = new ArrayList<>();
        entitiesIterable.forEach(entities::add);

        // Create mapping options
        MappingOptions options;
        if (attributeSet.isEmpty()) {
            // If no attributes specified, return complete mapping
            options = MappingOptions.builder()
                    .level(MappingLevel.COMPLETE)
                    .build();
        } else {
            // Otherwise return only requested attributes with SUMMARY level
            options = MappingOptions.builder()
                    .fields(attributeSet)
                    .level(MappingLevel.SUMMARY)
                    .build();
        }

        // Map entities to DTOs with options
        return mapper.toDtoListWithOptions(entities, options);
    }
}