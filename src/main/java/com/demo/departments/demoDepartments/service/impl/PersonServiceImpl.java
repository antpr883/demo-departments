package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import com.demo.departments.demoDepartments.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of PersonService that demonstrates the flexible DTO mapping system
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {


    private final PersonRepository personRepository;

    @Override
    public void deleteById(Long id) {
     personRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Person findByIdFull(Long id, String params) {
        EntityGraph entityGraph = DynamicEntityGraph.fetching().addPath("addresses").addPath("contacts").addPath("roles.permissions").build();
        Person person = personRepository.findById(id, entityGraph)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        return person;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Person> findByFull(){
        EntityGraph  entityGraph = DynamicEntityGraph.fetching().addPath("addresses").addPath("contacts").build();
        Optional<Person> byId = personRepository.findById(1L);
        return byId;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Person> findById(Long id) {
        return personRepository.findById(1L);
    }
}