package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonCreateUpdateDTO;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.PersonSummaryDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for Person entity with examples of flexible DTO mapping
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Get all persons with default mapping
     */
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        return ResponseEntity.ok(personService.findAll());
    }

    /**
     * Get all persons as minimal summary DTOs
     */
    @GetMapping("/summaries")
    public ResponseEntity<List<PersonSummaryDTO>> getAllPersonSummaries() {
        return ResponseEntity.ok(personService.findAllSummaries());
    }

    /**
     * Get persons with pagination
     */
    @GetMapping("/paged")
    public ResponseEntity<Page<PersonDTO>> getPagedPersons(Pageable pageable) {
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    /**
     * Get a person by ID with default mapping options
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getById(id));
    }

    /**
     * Get a person by ID with complete details (includes all related data)
     */
    @GetMapping("/{id}/complete")
    public ResponseEntity<PersonDTO> getPersonWithAllDetails(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonWithAllDetails(id));
    }

    /**
     * Get a person by ID with minimal details (excludes collections)
     */
    @GetMapping("/{id}/minimal") 
    public ResponseEntity<PersonDTO> getPersonMinimal(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getById(id, MappingOptions.minimal()));
    }

    /**
     * Example of using specific mapping options directly in the controller
     */
    @GetMapping("/{id}/custom")
    public ResponseEntity<PersonDTO> getPersonWithCustomMapping(
            @PathVariable Long id,
            @RequestParam(defaultValue = "true") boolean includeId,
            @RequestParam(defaultValue = "true") boolean includeAddresses,
            @RequestParam(defaultValue = "true") boolean includeContacts,
            @RequestParam(defaultValue = "false") boolean includeRoles,
            @RequestParam(defaultValue = "false") boolean includeAudit) {
        
        // Create mapping options based on request parameters
        MappingOptions options = MappingOptions.builder()
                .includeId(includeId)
                .includeAddresses(includeAddresses)
                .includeContacts(includeContacts)
                .includeRoles(includeRoles)
                .includeAuditFields(includeAudit)
                .build();
        
        return ResponseEntity.ok(personService.getById(id, options));
    }

    /**
     * Create a new person using PersonCreateUpdateDTO for validation
     */
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonCreateUpdateDTO createDTO) {
        return ResponseEntity.ok(personService.createPerson(createDTO));
    }
    
    /**
     * Create a new person with custom mapping options
     */
    @PostMapping("/custom")
    public ResponseEntity<PersonDTO> createPersonWithOptions(
            @Valid @RequestBody PersonCreateUpdateDTO createDTO,
            @RequestParam(defaultValue = "true") boolean includeAddresses,
            @RequestParam(defaultValue = "true") boolean includeContacts) {
        
        MappingOptions options = MappingOptions.builder()
                .includeAddresses(includeAddresses)
                .includeContacts(includeContacts)
                .build();
        
        return ResponseEntity.ok(personService.createPerson(createDTO, options));
    }

    /**
     * Update an existing person using PersonCreateUpdateDTO for validation
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable Long id, 
            @Valid @RequestBody PersonCreateUpdateDTO updateDTO) {
        return ResponseEntity.ok(personService.updatePerson(id, updateDTO));
    }
    
    /**
     * Update an existing person with custom mapping options
     */
    @PutMapping("/{id}/custom")
    public ResponseEntity<PersonDTO> updatePersonWithOptions(
            @PathVariable Long id,
            @Valid @RequestBody PersonCreateUpdateDTO updateDTO,
            @RequestParam(defaultValue = "true") boolean includeAddresses,
            @RequestParam(defaultValue = "true") boolean includeContacts) {
        
        MappingOptions options = MappingOptions.builder()
                .includeAddresses(includeAddresses)
                .includeContacts(includeContacts)
                .build();
        
        return ResponseEntity.ok(personService.updatePerson(id, updateDTO, options));
    }

    /**
     * Delete a person
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}