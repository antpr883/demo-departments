package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.persistence.repository.AddressRepository;
import com.demo.departments.demoDepartments.service.AddressService;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.AddressMapper;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of AddressService
 */
@Slf4j
@Service
@Transactional
public class AddressServiceImpl 
    extends AbstractBaseService<Address, AddressDTO, AddressRepository, AddressMapper>
    implements AddressService {

    public AddressServiceImpl(AddressRepository repository, 
                             AddressMapper addressMapper,
                             GraphBuilderMapperService graphBuilderService) {
        super(repository, addressMapper, graphBuilderService);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findEntityById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public AddressDTO save(AddressDTO addressDTO) {
        // Convert DTO to entity
        Address address = mapper.toEntity(addressDTO);
        
        // Save entity
        address = repository.save(address);
        
        // Return mapped entity as DTO
        return mapper.toDto(address);
    }

    @Override
    @Transactional
    public AddressDTO update(Long id, AddressDTO addressDTO) {
        // Find existing entity
        Address address = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + id));
        
        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(address, addressDTO);
        
        // Save updated entity
        address = repository.save(address);
        
        // Return mapped entity as DTO
        return mapper.toDto(address);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> findByPersonId(Long personId, MappingLevel level) {
        // Create an appropriate entity graph based on mapping level
        EntityGraph graph = null;
        
        // Use different fetch strategies based on mapping level
        if (level == MappingLevel.COMPLETE) {
            // For COMPLETE level, use a full entity graph with all attributes
            graph = graphBuilderService.getCompleteEntityGraph(entityClass);
        } else if (level == MappingLevel.SUMMARY) {
            // For SUMMARY level, use a specialized graph that fetches just what's needed for IDs and counts
            graph = createSummaryLevelGraph();
        }
        
        // Fetch entities with or without graph
        List<Address> addresses;
        if (graph != null) {
            addresses = repository.findByPersonId(personId, graph);
        } else {
            addresses = repository.findByPersonId(personId);
        }
        
        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .level(level)
                .build();
        
        return mapper.toDtoListWithOptions(addresses, options);
    }
}