package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import com.demo.departments.demoDepartments.persistence.repository.RoleRepository;
import com.demo.departments.demoDepartments.service.RoleService;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.dto.mapper.RoleMapper;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of RoleService
 */
@Slf4j
@Service
@Transactional
public class RoleServiceImpl 
    extends AbstractBaseService<Role, RoleDTO, RoleRepository, RoleMapper>
    implements RoleService {

    public RoleServiceImpl(RoleRepository repository, 
                          RoleMapper roleMapper,
                          GraphBuilderMapperService graphBuilderService) {
        super(repository, roleMapper, graphBuilderService);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findEntityById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public RoleDTO save(RoleDTO roleDTO) {
        // Convert DTO to entity
        Role role = mapper.toEntity(roleDTO);
        
        // Save entity
        role = repository.save(role);
        
        // Return mapped entity as DTO
        return mapper.toDto(role);
    }

    @Override
    @Transactional
    public RoleDTO update(Long id, RoleDTO roleDTO) {
        // Find existing entity
        Role role = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        
        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(role, roleDTO);
        
        // Save updated entity
        role = repository.save(role);
        
        // Return mapped entity as DTO
        return mapper.toDto(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findByPersonId(Long personId, boolean withAudit, Set<String> attributes) {
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
        List<Role> roles = repository.findByPersonId(personId, graph);
        
        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .withAudit(withAudit)
                .build();
        
        return mapper.toDtoListWithOptions(roles, options);
    }
    
}