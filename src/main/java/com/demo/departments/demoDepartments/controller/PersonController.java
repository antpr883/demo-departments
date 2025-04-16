package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.controller.swagger.api.PersonControllerEndpoint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * REST controller for managing Person entities
 */
@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
@Validated
public class PersonController implements PersonControllerEndpoint {

    private final PersonService personService;

    /**
     * GET /api/persons : Get all persons with configurable options
     * 
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return ResponseEntity with status 200 (OK) and the list of persons in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons(
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<PersonDTO> persons = personService.findAll(withAudit, attributeSet);
        return ResponseEntity.ok(persons);
    }

    /**
     * GET /api/persons/:id : Get a person by ID with configurable options
     * 
     * @param id The ID of the person to retrieve
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return ResponseEntity with status 200 (OK) and the person in body
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPerson(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        PersonDTO person = personService.findById(id, withAudit, attributeSet);
        return ResponseEntity.ok(person);
    }

    /**
     * POST /api/persons : Create a new person
     * 
     * @param personDTO The person to create
     * @return ResponseEntity with status 201 (Created) and the new person in body
     */
    @Override
    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        PersonDTO result = personService.save(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/persons/:id : Update an existing person
     * 
     * @param id The ID of the person to update
     * @param personDTO The person to update
     * @return ResponseEntity with status 200 (OK) and the updated person in body
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable @NotNull @Min(1) Long id, 
            @Valid @RequestBody PersonDTO personDTO) {
        PersonDTO result = personService.update(id, personDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/persons/:id : Delete a person
     * 
     * @param id The ID of the person to delete
     * @return ResponseEntity with status 204 (No Content)
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable @NotNull @Min(1) Long id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Helper method to parse comma-separated attributes into a Set
     */
    private Set<String> parseAttributesParam(String attributes) {
        if (attributes == null || attributes.trim().isEmpty()) {
            return null;
        }
        return new HashSet<>(Arrays.asList(attributes.split(",")));
    }
}