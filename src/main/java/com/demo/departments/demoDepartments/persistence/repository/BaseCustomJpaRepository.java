package com.demo.departments.demoDepartments.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

//@NoRepositoryBean каже Spring, що цей інтерфейс — лише база для інших репозиторіїв і не повинен бути створений як окремий бін.
@NoRepositoryBean // не створювати як окремий бін , а встановити як базу для інших репозіторієв
public interface BaseCustomJpaRepository <T, ID extends Serializable> extends EntityGraphJpaRepository<T, ID> {

    void batchSave(List<T> entities);

    void deleteByIdCustom(ID id);

    Optional<T> findByIdWithGraph(ID id , EntityGraph entityGraph);

}
