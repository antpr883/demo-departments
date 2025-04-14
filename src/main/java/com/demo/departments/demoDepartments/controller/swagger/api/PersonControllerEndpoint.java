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
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API interface for Person management
 */
@Tag(name = "Person", description = "Person management APIs")
public interface PersonControllerEndpoint {

    /**
     * GET /api/persons : Get all persons with the specified mapping level
     *
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body
     */
    @Operation(
        summary = "Get all persons",
        description = "Returns all persons with the specified mapping level"
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
            @Parameter(description = "Mapping level: MINIMAL, BASIC, SUMMARY, COMPLETE", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/persons/minimal : Get all persons with minimal information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with minimal fields
     */
    @Operation(
        summary = "Get all persons with minimal information",
        description = "Returns all persons with minimal fields only"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        )
    })
    @GetMapping("/minimal")
    ResponseEntity<List<PersonDTO>> getAllPersonsMinimal();

    /**
     * GET /api/persons/basic : Get all persons with basic information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with basic fields
     */
    @Operation(
        summary = "Get all persons with basic information",
        description = "Returns all persons with basic fields only"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        )
    })
    @GetMapping("/basic")
    ResponseEntity<List<PersonDTO>> getAllPersonsBasic();

    /**
     * GET /api/persons/summary : Get all persons with summary information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with summary fields
     */
    @Operation(
        summary = "Get all persons with summary information",
        description = "Returns all persons with summary information"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        )
    })
    @GetMapping("/summary")
    ResponseEntity<List<PersonDTO>> getAllPersonsSummary();
    
    /**
     * GET /api/persons/complete : Get all persons with complete information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with all fields
     */
    @Operation(
        summary = "Get all persons with complete information",
        description = "Returns all persons with all available fields"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        )
    })
    @GetMapping("/complete")
    ResponseEntity<List<PersonDTO>> getAllPersonsComplete();

    /**
     * GET /api/persons/:id : Get the person with the specified id and mapping level
     *
     * @param id the id of the person to retrieve
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the person in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a person by ID",
        description = "Retrieves a person by its ID with the specified mapping level"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID or mapping level",
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
            @Parameter(description = "Mapping level", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/persons/:id/full : Get the person with specified attributes
     *
     * @param id the id of the person to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the person in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a person with specified attributes",
        description = "Retrieves a person by its ID with specified attributes included"
    )
    @GetMapping("/{id}/full")
    ResponseEntity<PersonDTO> getPersonWithAttributes(
            @Parameter(description = "ID of the person to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);
            
    /**
     * GET /api/persons/:id/minimal : Get the person with MINIMAL mapping level
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with minimal fields
     */
    @Operation(
        summary = "Get a person with minimal information",
        description = "Retrieves a person by its ID with minimal fields only"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}/minimal")
    ResponseEntity<PersonDTO> getPersonMinimal(
            @Parameter(description = "ID of the person to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id);
            
    /**
     * GET /api/persons/:id/basic : Get the person with BASIC mapping level
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with basic fields
     */
    @Operation(
        summary = "Get a person with basic information",
        description = "Retrieves a person by its ID with basic fields only"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}/basic")
    ResponseEntity<PersonDTO> getPersonBasic(
            @Parameter(description = "ID of the person to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id);
            
    /**
     * GET /api/persons/:id/summary : Get the person with SUMMARY mapping level
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with summary fields
     */
    @Operation(
        summary = "Get a person with summary information",
        description = "Retrieves a person by its ID with summary fields"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}/summary")
    ResponseEntity<PersonDTO> getPersonSummary(
            @Parameter(description = "ID of the person to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id);
            
    /**
     * GET /api/persons/:id/complete : Get the person with COMPLETE mapping level
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with all fields
     */
    @Operation(
        summary = "Get a person with complete information",
        description = "Retrieves a person by its ID with all available fields"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}/complete")
    ResponseEntity<PersonDTO> getPersonComplete(
            @Parameter(description = "ID of the person to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id);

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