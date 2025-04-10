package com.demo.departments.demoDepartments.service;

import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;


/**
 * Base service interface that defines common CRUD operations with flexible mapping options
 *
 * @param <T> The DTO type
 */
public interface BaseService<T extends PersistenceModel> {

//    T save(@Valid T dto);
//
//    T update(@Valid T dto, Long id);
//
//    T patch(@Valid T dto, Long id);
//
     void deleteById(Long id);
//
//    Optional<T> findById(Long id);
//
//    Optional<T> findById(Long id, EntityGraph entityGraph);
//
    T findByIdFull(Long id, String params);

//    List<T> findByIds(Collection<Long> ids, EntityGraph entityGraph);
//
//    List<T> getAll();
//
//    long count();
//
//    boolean existsById(Long id);
}
