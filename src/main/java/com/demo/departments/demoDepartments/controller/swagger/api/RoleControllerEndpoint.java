package com.demo.departments.demoDepartments.controller.swagger.api;

import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
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
 * API interface for Role management
 */
@Tag(name = "Role", description = "Role management APIs")
public interface RoleControllerEndpoint {

    /**
     * GET /api/roles : Get all roles with configurable options
     *
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @Operation(
        summary = "Get all roles with configurable options",
        description = "Returns all roles with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RoleDTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<RoleDTO>> getAllRoles(
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/roles/:id : Get a role by ID with configurable options
     *
     * @param id the id of the role to retrieve
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the role in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a role by ID with configurable options",
        description = "Retrieves a role by its ID with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<RoleDTO> getRole(
            @Parameter(description = "ID of the role to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/roles/person/:personId : Get all roles for a person with configurable options
     *
     * @param personId the id of the person
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @Operation(
        summary = "Get all roles for a person with configurable options",
        description = "Returns all roles for a person with optional audit information and specified attributes"
    )
    @GetMapping("/person/{personId}")
    ResponseEntity<List<RoleDTO>> getRolesByPersonId(
            @Parameter(description = "ID of the person", required = true)
            @PathVariable @NotNull @Min(1) Long personId,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * POST /api/roles : Create a new role
     *
     * @param roleDTO the role to create
     * @return the ResponseEntity with status 201 (Created) and the new role in body
     */
    @Operation(
        summary = "Create a new role",
        description = "Creates a new role entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Role created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<RoleDTO> createRole(
            @Parameter(description = "Role data", required = true)
            @Valid @RequestBody RoleDTO roleDTO);

    /**
     * PUT /api/roles/:id : Update an existing role
     *
     * @param id the id of the role to update
     * @param roleDTO the role to update
     * @return the ResponseEntity with status 200 (OK) and the updated role in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Update an existing role",
        description = "Updates a role entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Role updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<RoleDTO> updateRole(
            @Parameter(description = "ID of the role to update", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Role data", required = true)
            @Valid @RequestBody RoleDTO roleDTO);

    /**
     * DELETE /api/roles/:id : Delete the role with the specified id
     *
     * @param id the id of the role to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Operation(
        summary = "Delete a role",
        description = "Deletes a role by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Role deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID of the role to delete", required = true)
            @PathVariable @NotNull @Min(1) Long id);
}