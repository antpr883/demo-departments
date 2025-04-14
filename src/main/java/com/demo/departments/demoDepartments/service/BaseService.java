package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;

import java.util.List;

/**
 * Base service interface that defines common CRUD operations with flexible mapping options
 *
 * @param <T> The DTO type
 */
public interface BaseService<T> {

    /**
     * Delete entity by ID
     * 
     * @param id Entity ID
     */
    void deleteById(Long id);

    /**
     * Find entity by ID with mapping determined by level
     * 
     * @param id Entity ID
     * @param level Mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return DTO with level-appropriate mapping
     */
    T findById(Long id, MappingLevel level);

    /**
     * Find entity by ID with full data based on specified attributes
     * This is the core dynamic mapping method that loads only requested associations
     * 
     * @param id Entity ID
     * @param attributes List of attributes to include (like "addresses", "contacts", "roles.permissions")
     * @return DTO with requested associations
     */
    T findByIdFull(Long id, List<String> attributes);
    
    /**
     * Find all entities with mapping determined by level
     * 
     * @param level Mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return List of DTOs with level-appropriate mapping
     */
    List<T> findAll(MappingLevel level);
    
    /**
     * Find all entities with full data based on requested attributes
     * 
     * @param attributes List of attributes to include
     * @return List of DTOs with attribute-based mapping
     */
    List<T> findAllFull(List<String> attributes);
}