package com.demo.departments.demoDepartments.persistence.repository.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphSimpleJpaRepository;
import com.demo.departments.demoDepartments.persistence.repository.BaseCustomJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseCustomJpaRepositoryImpl<T, ID extends Serializable> extends EntityGraphSimpleJpaRepository<T, ID> implements BaseCustomJpaRepository<T, ID> {

    private final JpaEntityInformation<T, ID> entityInformation;
    private final EntityManager entityManager;
    private final Class<T> domainClass;

    @Value("${hibernate.jdbc.batch_size:50}")
    private int batchSize;

    public BaseCustomJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
        this.domainClass = entityInformation.getJavaType();
    }

    @Override
    public void batchSave(List<T> entities) {
        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));

            if ((i + 1) % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

//    @Override
//    public T findByIdWithGraph(ID id, EntityGraph entityGraph) {
//        Optional<T> byId = super.findById(id, entityGraph);
//        return super.findById(id, entityGraph).get();
//    }

    public void deleteByIdCustom(ID id) {
        T entity = entityManager.find(domainClass, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
