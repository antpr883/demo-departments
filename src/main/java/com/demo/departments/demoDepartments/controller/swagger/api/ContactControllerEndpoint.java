package com.demo.departments.demoDepartments.controller.swagger.api;

import com.demo.departments.demoDepartments.service.dto.ContactDTO;
import com.demo.departments.demoDepartments.controller.swagger.model.ErrorResponse;
import com.demo.departments.demoDepartments.controller.swagger.model.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API interface for Contact management
 */
@Tag(name = "Contact", description = "Contact management APIs")
public interface ContactControllerEndpoint {

    /**
     * GET /api/contacts : Get all contacts with the specified mapping level
     *
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Operation(
        summary = "Get all contacts",
        description = "Returns all contacts with the specified mapping level"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContactDTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<ContactDTO>> getAllContacts(
            @Parameter(description = "Mapping level: MINIMAL, BASIC, SUMMARY, COMPLETE", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/contacts/:id : Get the contact with the specified id and mapping level
     *
     * @param id the id of the contact to retrieve
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the contact in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a contact by ID",
        description = "Retrieves a contact by its ID with the specified mapping level"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID or mapping level",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contact not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<ContactDTO> getContact(
            @Parameter(description = "ID of the contact to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Mapping level", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/contacts/:id/full : Get the contact with specified attributes
     *
     * @param id the id of the contact to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the contact in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a contact with specified attributes",
        description = "Retrieves a contact by its ID with specified attributes included"
    )
    @GetMapping("/{id}/full")
    ResponseEntity<ContactDTO> getContactWithAttributes(
            @Parameter(description = "ID of the contact to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/contacts/person/:personId : Get all contacts for a person
     *
     * @param personId the id of the person
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @Operation(
        summary = "Get all contacts for a person",
        description = "Returns all contacts for a person with the specified mapping level"
    )
    @GetMapping("/person/{personId}")
    ResponseEntity<List<ContactDTO>> getContactsByPersonId(
            @Parameter(description = "ID of the person", required = true)
            @PathVariable @NotNull @Min(1) Long personId,
            @Parameter(description = "Mapping level", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * POST /api/contacts : Create a new contact
     *
     * @param contactDTO the contact to create
     * @return the ResponseEntity with status 201 (Created) and the new contact in body
     */
    @Operation(
        summary = "Create a new contact",
        description = "Creates a new contact entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Contact created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<ContactDTO> createContact(
            @Parameter(description = "Contact data", required = true)
            @Valid @RequestBody ContactDTO contactDTO);

    /**
     * PUT /api/contacts/:id : Update an existing contact
     *
     * @param id the id of the contact to update
     * @param contactDTO the contact to update
     * @return the ResponseEntity with status 200 (OK) and the updated contact in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Update an existing contact",
        description = "Updates a contact entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Contact updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contact not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<ContactDTO> updateContact(
            @Parameter(description = "ID of the contact to update", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Contact data", required = true)
            @Valid @RequestBody ContactDTO contactDTO);

    /**
     * DELETE /api/contacts/:id : Delete the contact with the specified id
     *
     * @param id the id of the contact to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Operation(
        summary = "Delete a contact",
        description = "Deletes a contact by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Contact deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contact not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteContact(
            @Parameter(description = "ID of the contact to delete", required = true)
            @PathVariable @NotNull @Min(1) Long id);
}