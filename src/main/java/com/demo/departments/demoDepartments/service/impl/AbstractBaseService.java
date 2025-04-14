package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;
import com.demo.departments.demoDepartments.persistence.repository.BaseCustomJpaRepository;
import com.demo.departments.demoDepartments.service.BaseService;
import com.demo.departments.demoDepartments.service.dto.mapper.EntityMapper;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Abstract base service implementation that provides common CRUD operations with flexible mapping
 *
 * @param <E> Entity type
 * @param <D> DTO type
 * @param <R> Repository type
 * @param <M> Mapper type
 */
@Transactional
@RequiredArgsConstructor
public abstract class AbstractBaseService<
        E extends PersistenceModel,
        D,
        R extends BaseCustomJpaRepository<E, Long>,
        M extends EntityMapper<E, D>> implements BaseService<D> {

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
        // Create an appropriate entity graph based on mapping level
        EntityGraph graph = null;
        
        // Use different fetch strategies based on mapping level
        if (level == MappingLevel.COMPLETE) {
            // For COMPLETE level, use a full entity graph with all attributes
            graph = graphBuilderService.getCompleteEntityGraph(entityClass);
        } else if (level == MappingLevel.SUMMARY) {
            // For SUMMARY level, use a specialized graph that fetches just what's needed for IDs and counts
            graph = createSummaryLevelGraph();
        }
        
        // Fetch entity with or without graph
        E entity;
        if (graph != null) {
            entity = repository.findById(id, graph)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        } else {
            entity = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        }

        // Map to DTO with appropriate options
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();

        return mapper.toDtoWithOptions(entity, options);
    }
    
    /**
     * Creates an entity graph optimized for SUMMARY level mapping
     * - Includes just enough to get IDs and counts in a single query
     * - Designed to minimize database roundtrips
     */
    protected EntityGraph createSummaryLevelGraph() {
        DynamicEntityGraph.Builder builder = DynamicEntityGraph.fetching();
        
        // Analyze the entity class and add paths for all @OneToMany and @ManyToMany relations
        // This ensures we can get IDs and counts without additional queries
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(OneToMany.class) || 
                field.isAnnotationPresent(ManyToMany.class) ||
                field.isAnnotationPresent(ManyToOne.class)) {
                builder.addPath(field.getName());
            }
        }
        
        return builder.build();
    }

    /**
     * Helper method to create mapping options based on attribute set
     */
    protected MappingOptions createMappingOptions(Set<String> attributeSet) {
        if (attributeSet.isEmpty()) {
            // If no attributes specified, return complete mapping
            return MappingOptions.builder()
                    .level(MappingLevel.COMPLETE)
                    .build();
        } else {
            // Otherwise return only requested attributes with SUMMARY level
            return MappingOptions.builder()
                    .fields(attributeSet)
                    .level(MappingLevel.SUMMARY)
                    .build();
        }
    }

    /**
     * Helper method to create entity graph based on attribute set
     */
    protected EntityGraph createEntityGraph(Set<String> attributeSet) {
        if (attributeSet.isEmpty()) {
            // If no attributes specified, get full entity graph
            return graphBuilderService.getCompleteEntityGraph(entityClass);
        } else {
            // Otherwise get graph just for requested attributes
            return graphBuilderService.getGraphWithAttributes(entityClass, attributeSet);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public D findByIdFull(Long id, List<String> attributes) {
        // Convert list to set for faster lookups
        Set<String> attributeSet = attributes != null ? new HashSet<>(attributes) : Collections.emptySet();

        // Generate appropriate entity graph for efficient loading
        EntityGraph graph;
        if (attributeSet.isEmpty()) {
            // If no specific attributes requested, use the summary graph to ensure efficient loading
            graph = createSummaryLevelGraph();
        } else {
            // Otherwise create a targeted graph for the requested attributes
            graph = createEntityGraph(attributeSet);
        }

        // Fetch entity with the dynamic graph
        E entity = repository.findById(id, graph)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

        // Create mapping options
        MappingOptions options = createMappingOptions(attributeSet);

        // Map entity to DTO with options
        return mapper.toDtoWithOptions(entity, options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> findAll(MappingLevel level) {
        // Create an appropriate entity graph based on mapping level
        EntityGraph graph = null;
        
        // Use different fetch strategies based on mapping level
        if (level == MappingLevel.COMPLETE) {
            // For COMPLETE level, use a full entity graph with all attributes
            graph = graphBuilderService.getCompleteEntityGraph(entityClass);
        } else if (level == MappingLevel.SUMMARY) {
            // For SUMMARY level, use a specialized graph that fetches just what's needed for IDs and counts
            graph = createSummaryLevelGraph();
        }
        
        // Fetch entities with or without graph
        List<E> entities;
        if (graph != null) {
            Iterable<E> entitiesIterable = repository.findAll(graph);
            entities = new ArrayList<>();
            entitiesIterable.forEach(entities::add);
        } else {
            entities = repository.findAll();
        }

        // Map to DTOs with appropriate options
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

        // Generate appropriate entity graph for efficient loading
        EntityGraph graph;
        if (attributeSet.isEmpty()) {
            // If no specific attributes requested, use the summary graph to ensure efficient loading
            graph = createSummaryLevelGraph();
        } else {
            // Otherwise create a targeted graph for the requested attributes
            graph = createEntityGraph(attributeSet);
        }

        // Fetch entities with the dynamic graph
        Iterable<E> entitiesIterable = repository.findAll(graph);
        List<E> entities = new ArrayList<>();
        entitiesIterable.forEach(entities::add);

        // Create mapping options
        MappingOptions options = createMappingOptions(attributeSet);

        // Map entities to DTOs with options
        return mapper.toDtoListWithOptions(entities, options);
    }
}