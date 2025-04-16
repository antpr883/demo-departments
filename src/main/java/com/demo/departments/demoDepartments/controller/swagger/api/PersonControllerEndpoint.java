package com.demo.departments.demoDepartments.controller.swagger.api;

import com.demo.departments.demoDepartments.service.dto.PersonDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API interface for Person management
 */
@Tag(name = "Person", description = "Person management APIs")
public interface PersonControllerEndpoint {

    /**
     * GET /api/persons : Get all persons with configurable options
     *
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body
     */
    @Operation(
        summary = "Get all persons with configurable options",
        description = "Returns all persons with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PersonDTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<PersonDTO>> getAllPersons(
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include (e.g., \"contacts,roles.permissions,addresses\")")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/persons/:id : Get the person with configurable options
     *
     * @param id The ID of the person to retrieve
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include (e.g., "contacts,roles.permissions,addresses")
     * @return the ResponseEntity with status 200 (OK) and the person in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a person by ID with configurable options",
        description = "Retrieves a person by its ID with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<PersonDTO> getPerson(
            @Parameter(description = "ID of the person to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include (e.g., \"contacts,roles.permissions,addresses\")")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * POST /api/persons : Create a new person
     *
     * @param personDTO the person to create
     * @return the ResponseEntity with status 201 (Created) and the new person in body
     */
    @Operation(
        summary = "Create a new person",
        description = "Creates a new person entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Person created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<PersonDTO> createPerson(
            @Parameter(description = "Person data", required = true)
            @Valid @RequestBody PersonDTO personDTO);

    /**
     * PUT /api/persons/:id : Update an existing person
     *
     * @param id the id of the person to update
     * @param personDTO the person to update
     * @return the ResponseEntity with status 200 (OK) and the updated person in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Update an existing person",
        description = "Updates a person entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Person updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<PersonDTO> updatePerson(
            @Parameter(description = "ID of the person to update", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Person data", required = true)
            @Valid @RequestBody PersonDTO personDTO);

    /**
     * DELETE /api/persons/:id : Delete the person with the specified id
     *
     * @param id the id of the person to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Operation(
        summary = "Delete a person",
        description = "Deletes a person by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Person deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePerson(
            @Parameter(description = "ID of the person to delete", required = true)
            @PathVariable @NotNull @Min(1) Long id);
}