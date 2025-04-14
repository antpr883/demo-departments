package com.demo.departments.demoDepartments.service.impl;

import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.PersonMapper;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of PersonService
 */
@Slf4j
@Service
@Transactional
public class PersonServiceImpl 
    extends AbstractBaseService<Person, PersonDTO, PersonRepository, PersonMapper>
    implements PersonService {

    public PersonServiceImpl(PersonRepository repository, 
                             PersonMapper personMapper,
                             GraphBuilderMapperService graphBuilderService) {
        super(repository, personMapper, graphBuilderService);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findEntityById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public PersonDTO save(PersonDTO personDTO) {
        // Convert DTO to entity
        Person person = mapper.toEntity(personDTO);
        
        // Save entity
        person = repository.save(person);
        
        // Return mapped entity as DTO
        return mapper.toDto(person);
    }

    @Override
    @Transactional
    public PersonDTO update(Long id, PersonDTO personDTO) {
        // Find existing entity
        Person person = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        
        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(person, personDTO);
        
        // Save updated entity
        person = repository.save(person);
        
        // Return mapped entity as DTO
        return mapper.toDto(person);
    }
}