package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.controller.swagger.api.ContactControllerEndpoint;
import com.demo.departments.demoDepartments.service.ContactService;
import com.demo.departments.demoDepartments.service.dto.ContactDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
     * GET /api/contacts : Get all contacts with the specified mapping level
     *
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts(
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<ContactDTO> contacts = contactService.findAll(mappingLevel);
        return ResponseEntity.ok(contacts);
    }

    /**
     * GET /api/contacts/:id : Get the contact with the specified id and mapping level
     *
     * @param id the id of the contact to retrieve
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the contact in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContact(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        ContactDTO contact = contactService.findById(id, mappingLevel);
        return ResponseEntity.ok(contact);
    }

    /**
     * GET /api/contacts/:id/full : Get the contact with specified attributes
     *
     * @param id the id of the contact to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the contact in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}/full")
    public ResponseEntity<ContactDTO> getContactWithAttributes(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "attributes", required = false) String attributes) {
        List<String> attributeList = attributes != null
                ? Arrays.asList(attributes.split(","))
                : List.of();
        ContactDTO contact = contactService.findByIdFull(id, attributeList);
        return ResponseEntity.ok(contact);
    }

    /**
     * GET /api/contacts/person/:personId : Get all contacts for a person
     *
     * @param personId the id of the person
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE) 
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Override
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<ContactDTO>> getContactsByPersonId(
            @PathVariable @NotNull @Min(1) Long personId,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<ContactDTO> contacts = contactService.findByPersonId(personId, mappingLevel);
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