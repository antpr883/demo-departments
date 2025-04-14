package com.demo.departments.demoDepartments.controller.swagger.api;

import com.demo.departments.demoDepartments.service.dto.AddressDTO;
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
 * API interface for Address management
 */
@Tag(name = "Address", description = "Address management APIs")
public interface AddressControllerEndpoint {

    /**
     * GET /api/addresses : Get all addresses with the specified mapping level
     *
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @Operation(
        summary = "Get all addresses",
        description = "Returns all addresses with the specified mapping level"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AddressDTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<AddressDTO>> getAllAddresses(
            @Parameter(description = "Mapping level: MINIMAL, BASIC, SUMMARY, COMPLETE", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/addresses/:id : Get the address with the specified id and mapping level
     *
     * @param id the id of the address to retrieve
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the address in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get an address by ID",
        description = "Retrieves an address by its ID with the specified mapping level"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID or mapping level",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Address not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<AddressDTO> getAddress(
            @Parameter(description = "ID of the address to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Mapping level", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/addresses/:id/full : Get the address with specified attributes
     *
     * @param id the id of the address to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the address in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get an address with specified attributes",
        description = "Retrieves an address by its ID with specified attributes included"
    )
    @GetMapping("/{id}/full")
    ResponseEntity<AddressDTO> getAddressWithAttributes(
            @Parameter(description = "ID of the address to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/addresses/person/:personId : Get all addresses for a person
     *
     * @param personId the id of the person
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @Operation(
        summary = "Get all addresses for a person",
        description = "Returns all addresses for a person with the specified mapping level"
    )
    @GetMapping("/person/{personId}")
    ResponseEntity<List<AddressDTO>> getAddressesByPersonId(
            @Parameter(description = "ID of the person", required = true)
            @PathVariable @NotNull @Min(1) Long personId,
            @Parameter(description = "Mapping level", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * POST /api/addresses : Create a new address
     *
     * @param addressDTO the address to create
     * @return the ResponseEntity with status 201 (Created) and the new address in body
     */
    @Operation(
        summary = "Create a new address",
        description = "Creates a new address entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Address created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<AddressDTO> createAddress(
            @Parameter(description = "Address data", required = true)
            @Valid @RequestBody AddressDTO addressDTO);

    /**
     * PUT /api/addresses/:id : Update an existing address
     *
     * @param id the id of the address to update
     * @param addressDTO the address to update
     * @return the ResponseEntity with status 200 (OK) and the updated address in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Update an existing address",
        description = "Updates an address entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Address updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Address not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<AddressDTO> updateAddress(
            @Parameter(description = "ID of the address to update", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Address data", required = true)
            @Valid @RequestBody AddressDTO addressDTO);

    /**
     * DELETE /api/addresses/:id : Delete the address with the specified id
     *
     * @param id the id of the address to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Operation(
        summary = "Delete an address",
        description = "Deletes an address by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Address deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Address not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAddress(
            @Parameter(description = "ID of the address to delete", required = true)
            @PathVariable @NotNull @Min(1) Long id);
}