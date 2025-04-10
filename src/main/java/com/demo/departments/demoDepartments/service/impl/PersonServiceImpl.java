package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.repository.PersonRepository;
import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of PersonService that demonstrates the flexible DTO mapping system
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private  PersonRepository personRepository;



    @Override
    public PersonDTO save(PersonDTO dto) {
        return null;
    }

    @Override
    public PersonDTO update(PersonDTO dto, Long id) {
        return null;
    }

    @Override
    public PersonDTO patch(PersonDTO dto, Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<PersonDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<PersonDTO> findById(Long id, EntityGraph entityGraph) {
        return Optional.empty();
    }

    @Override
    public List<PersonDTO> findByIds(Collection<Long> ids, EntityGraph entityGraph) {
        return List.of();
    }

    @Override
    public List<PersonDTO> getAll() {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}