package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.dto.mapper.PersonMapper;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of PersonService that demonstrates the flexible DTO mapping system
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;
    private final GraphBuilderMapperService graphBuilderMappingService;

    @Override
    public void deleteById(Long id) {
     personRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDTO findByIdFull(Long id, Map<String, String> params) {
        Set<String> dtoFields = parse(params.get("dto_fields"));
        Set<String> graphFields = parse(params.get("graph_fields"));

        EntityGraph graph = graphBuilderMappingService.getGraphWithAttributes(Person.class, graphFields);
        Person person = personRepository.findById(id, graph)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        MappingOptions options = MappingOptions.builder()
                .fields(dtoFields)
                .build();

        return personMapper.toDto(person, options);
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

    private Set<String> parse(String paramValue) {
        if (paramValue == null || paramValue.isBlank()) return Set.of();
        return Arrays.stream(paramValue.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}