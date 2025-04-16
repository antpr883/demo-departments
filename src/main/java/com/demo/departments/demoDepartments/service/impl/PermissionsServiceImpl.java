package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.repository.PermissionsRepository;
import com.demo.departments.demoDepartments.service.PermissionsService;
import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.dto.mapper.PermissionsMapper;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of PermissionsService
 */
@Slf4j
@Service
@Transactional
public class PermissionsServiceImpl 
    extends AbstractBaseService<Permissions, PermissionsDTO, PermissionsRepository, PermissionsMapper>
    implements PermissionsService {

    public PermissionsServiceImpl(PermissionsRepository repository, 
                                 PermissionsMapper permissionsMapper,
                                 GraphBuilderMapperService graphBuilderService) {
        super(repository, permissionsMapper, graphBuilderService);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permissions> findEntityById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public PermissionsDTO save(PermissionsDTO permissionsDTO) {
        // Convert DTO to entity
        Permissions permissions = mapper.toEntity(permissionsDTO);
        
        // Save entity
        permissions = repository.save(permissions);
        
        // Return mapped entity as DTO
        return mapper.toDto(permissions);
    }

    @Override
    @Transactional
    public PermissionsDTO update(Long id, PermissionsDTO permissionsDTO) {
        // Find existing entity
        Permissions permissions = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissions not found with id: " + id));
        
        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(permissions, permissionsDTO);
        
        // Save updated entity
        permissions = repository.save(permissions);
        
        // Return mapped entity as DTO
        return mapper.toDto(permissions);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PermissionsDTO> findByRoleId(Long roleId, boolean withAudit, Set<String> attributes) {
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
        List<Permissions> permissions = repository.findByRoleId(roleId, graph);
        
        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .withAudit(withAudit)
                .build();
        
        return mapper.toDtoListWithOptions(permissions, options);
    }
    
}