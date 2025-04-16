package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.controller.swagger.api.ContactControllerEndpoint;
import com.demo.departments.demoDepartments.service.ContactService;
import com.demo.departments.demoDepartments.service.dto.ContactDTO;
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
 * REST controller for managing Contact entities
 */
@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Validated
public class ContactController implements ContactControllerEndpoint {

    private final ContactService contactService;

    /**
     * GET /api/contacts : Get all contacts with configurable options
     *
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts(
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<ContactDTO> contacts = contactService.findAll(withAudit, attributeSet);
        return ResponseEntity.ok(contacts);
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

    /**
     * GET /api/contacts/:id : Get a contact by ID with configurable options
     *
     * @param id the id of the contact to retrieve
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the contact in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContact(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        ContactDTO contact = contactService.findById(id, withAudit, attributeSet);
        return ResponseEntity.ok(contact);
    }


    /**
     * GET /api/contacts/person/:personId : Get all contacts for a person with configurable options
     *
     * @param personId the id of the person
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Override
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<ContactDTO>> getContactsByPersonId(
            @PathVariable @NotNull @Min(1) Long personId,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<ContactDTO> contacts = contactService.findByPersonId(personId, withAudit, attributeSet);
        return ResponseEntity.ok(contacts);
    }

    /**
     * POST /api/contacts : Create a new contact
     *
     * @param contactDTO the contact to create
     * @return the ResponseEntity with status 201 (Created) and the new contact in body
     */
    @Override
    @PostMapping
    public ResponseEntity<ContactDTO> createContact(
            @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO result = contactService.save(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/contacts/:id : Update an existing contact
     *
     * @param id the id of the contact to update
     * @param contactDTO the contact to update
     * @return the ResponseEntity with status 200 (OK) and the updated contact in body,
     * or with status 404 (Not Found)
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(
            @PathVariable @NotNull @Min(1) Long id,
            @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO result = contactService.update(id, contactDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/contacts/:id : Delete the contact with the specified id
     *
     * @param id the id of the contact to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable @NotNull @Min(1) Long id) {
        contactService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}