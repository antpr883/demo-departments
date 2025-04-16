package com.demo.departments.demoDepartments.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.model.Contact;
import com.demo.departments.demoDepartments.persistence.repository.ContactRepository;
import com.demo.departments.demoDepartments.service.ContactService;
import com.demo.departments.demoDepartments.service.dto.ContactDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.ContactMapper;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import com.demo.departments.demoDepartments.service.utils.mapping.GraphBuilderMapperService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of ContactService
 */
@Slf4j
@Service
@Transactional
public class ContactServiceImpl 
    extends AbstractBaseService<Contact, ContactDTO, ContactRepository, ContactMapper>
    implements ContactService {

    public ContactServiceImpl(ContactRepository repository, 
                             ContactMapper contactMapper,
                             GraphBuilderMapperService graphBuilderService) {
        super(repository, contactMapper, graphBuilderService);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contact> findEntityById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public ContactDTO save(ContactDTO contactDTO) {
        // Convert DTO to entity
        Contact contact = mapper.toEntity(contactDTO);
        
        // Save entity
        contact = repository.save(contact);
        
        // Return mapped entity as DTO
        return mapper.toDto(contact);
    }

    @Override
    @Transactional
    public ContactDTO update(Long id, ContactDTO contactDTO) {
        // Find existing entity
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
        
        // Update entity with DTO, ignoring null values
        mapper.partialUpdate(contact, contactDTO);
        
        // Save updated entity
        contact = repository.save(contact);
        
        // Return mapped entity as DTO
        return mapper.toDto(contact);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ContactDTO> findByPersonId(Long personId, boolean withAudit, Set<String> attributes) {
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
        List<Contact> contacts = repository.findByPersonId(personId, graph);
        
        // Map to DTOs with appropriate options
        MappingOptions options = MappingOptions.builder()
                .attributes(attributes)
                .withAudit(withAudit)
                .build();
        
        return mapper.toDtoListWithOptions(contacts, options);
    }
    
}