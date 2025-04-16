package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.persistence.repository.AddressRepository;
import com.demo.departments.demoDepartments.service.AddressService;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.AddressMapper;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public List<AddressDTO> findByPersonId(Long personId, boolean withAudit, Set<String> attributes) {
        // Create an appropriate entity graph based on attributes
        EntityGraph graph;
        
        if (attributes == null || attributes.isEmpty()) {
            // Default behavior - lightweight graph for efficient loading
            graph = createDefaultEntityGraph();
        } else {
            // Generate targeted graph based on requested attributes
            graph = createEntityGraph(attributes);
        }
        
        // Fetch entities with graph
        List<Address> addresses = repository.findByPersonId(personId, graph);
        
        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .withAudit(withAudit)
                .build();
        
        return mapper.toDtoListWithOptions(addresses, options);
    }
    
}