package com.demo.departments.demoDepartments.service;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.service.dto.PersonCreateUpdateDTO;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.PersonSummaryDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;

import java.util.List;

/**
 * Service interface for Person entity operations
 */
public interface PersonService extends BaseService<PersonDTO> {

}