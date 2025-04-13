package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.dto.mapper.PersonMapper;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of PersonService
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;
    private final GraphBuilderMapperService graphBuilderMappingService;

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findEntityById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    /**
     * Find a person by ID with full data based on requested attributes
     * This is the only method that uses entity graphs for dynamic attribute loading
     * If no attributes specified, returns all attributes (COMPLETE level)
     */
    @Override
    @Transactional(readOnly = true)
    public PersonDTO findByIdFull(Long id, List<String> attributes) {
        // Convert list to set for faster lookups
        Set<String> attributeSet = attributes != null ? new HashSet<>(attributes) : Collections.emptySet();
        
        // Generate appropriate entity graph
        EntityGraph graph;
        if (attributeSet.isEmpty()) {
            // If no attributes specified, get full entity graph
            graph = graphBuilderMappingService.getCompleteEntityGraph(Person.class);
        } else {
            // Otherwise get graph just for requested attributes
            graph = graphBuilderMappingService.getGraphWithAttributes(Person.class, attributeSet);
        }

        // Fetch entity with the dynamic graph
        Person person = personRepository.findById(id, graph)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));

        // Create mapping options
        MappingOptions options;
        if (attributeSet.isEmpty()) {
            // If no attributes specified, return complete mapping
            options = MappingOptions.builder()
                    .level(MappingLevel.COMPLETE)
                    .build();
        } else {
            // Otherwise return only requested attributes with COMPLETE level
            options = MappingOptions.builder()
                    .fields(attributeSet)
                    .level(MappingLevel.SUMMARY) // Use SUMMARY level for attribute-based mapping
                    .build();
        }

        // Map entity to DTO with options
        return personMapper.toDtoWithOptions(person, options);
    }

    /**
     * Find a person by ID with level-based mapping
     * No entity graphs used here - just fetch the entity and apply the appropriate mapping level
     */
    @Override
    @Transactional(readOnly = true)
    public PersonDTO findById(Long id, MappingLevel level) {
        // Simple entity fetch without any graph
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        
        // Create mapping options with level
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
        
        // Map entity to DTO with level-based options
        return personMapper.toDtoWithOptions(person, options);
    }

    /**
     * Find all persons with level-based mapping
     * No entity graphs used here - just fetch entities and apply the appropriate mapping level
     */
    @Override
    @Transactional(readOnly = true)
    public List<PersonDTO> findAll(MappingLevel level) {
        // Simple entities fetch without any graph
        List<Person> persons = personRepository.findAll();
        
        // Create mapping options with level
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
        
        // Map entities to DTOs with level-based options
        return persons.stream()
                .map(person -> personMapper.toDtoWithOptions(person, options))
                .collect(Collectors.toList());
    }

    /**
     * Save a new or update an existing person
     */
    @Override
    @Transactional
    public PersonDTO save(PersonDTO personDTO) {
        // Convert DTO to entity
        Person person = personMapper.toEntity(personDTO);
        
        // Save entity
        person = personRepository.save(person);
        
        // Return mapped entity as DTO
        return personMapper.toDto(person);
    }

    /**
     * Partially update a person
     */
    @Override
    @Transactional
    public PersonDTO update(Long id, PersonDTO personDTO) {
        // Find existing entity
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        
        // Update entity with DTO, ignoring null values
        personMapper.partialUpdate(person, personDTO);
        
        // Save updated entity
        person = personRepository.save(person);
        
        // Return mapped entity as DTO
        return personMapper.toDto(person);
    }
}