package com.demo.departments.demoDepartments.service;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Base service interface that defines common CRUD operations with flexible mapping options
 *
 * @param <T> The DTO type
 */
public interface BaseService<T extends BaseDTO> {

    T save(@Valid T dto);

    T update(@Valid T dto, Long id);

    T patch(@Valid T dto, Long id);

    void deleteById(Long id);

    Optional<T> findById(Long id);

    Optional<T> findById(Long id, EntityGraph entityGraph);

    List<T> findByIds(Collection<Long> ids, EntityGraph entityGraph);

    List<T> getAll();

    long count();

    boolean existsById(Long id);
}
