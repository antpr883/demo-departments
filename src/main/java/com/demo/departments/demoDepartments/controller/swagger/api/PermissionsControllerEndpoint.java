package com.demo.departments.demoDepartments.controller.swagger.api;

import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
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
 * API interface for Permissions management
 */
@Tag(name = "Permissions", description = "Permissions management APIs")
public interface PermissionsControllerEndpoint {

    /**
     * GET /api/permissions : Get all permissions with configurable options
     *
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
     */
    @Operation(
        summary = "Get all permissions with configurable options",
        description = "Returns all permissions with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PermissionsDTO.class)
            )
        )
    })
    @GetMapping
    ResponseEntity<List<PermissionsDTO>> getAllPermissions(
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/permissions/:id : Get a permission by ID with configurable options
     *
     * @param id the id of the permission to retrieve
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the permission in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a permission by ID with configurable options",
        description = "Retrieves a permission by its ID with optional audit information and specified attributes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionsDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Permission not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<PermissionsDTO> getPermissions(
            @Parameter(description = "ID of the permission to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/permissions/role/:roleId : Get all permissions for a role with configurable options
     *
     * @param roleId the id of the role
     * @param withAudit If true, include audit information (createdDate, modifiedDate, createdBy, modifiedBy)
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
     */
    @Operation(
        summary = "Get all permissions for a role with configurable options",
        description = "Returns all permissions for a role with optional audit information and specified attributes"
    )
    @GetMapping("/role/{roleId}")
    ResponseEntity<List<PermissionsDTO>> getPermissionsByRoleId(
            @Parameter(description = "ID of the role", required = true)
            @PathVariable @NotNull @Min(1) Long roleId,
            @Parameter(description = "Include audit information (createdDate, modifiedDate, createdBy, modifiedBy)")
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * POST /api/permissions : Create a new permissions
     *
     * @param permissionsDTO the permissions to create
     * @return the ResponseEntity with status 201 (Created) and the new permissions in body
     */
    @Operation(
        summary = "Create a new permission",
        description = "Creates a new permission entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Permission created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionsDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<PermissionsDTO> createPermissions(
            @Parameter(description = "Permission data", required = true)
            @Valid @RequestBody PermissionsDTO permissionsDTO);

    /**
     * PUT /api/permissions/:id : Update an existing permissions
     *
     * @param id the id of the permissions to update
     * @param permissionsDTO the permissions to update
     * @return the ResponseEntity with status 200 (OK) and the updated permissions in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Update an existing permission",
        description = "Updates a permission entity with the provided data"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Permission updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionsDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Permission not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<PermissionsDTO> updatePermissions(
            @Parameter(description = "ID of the permission to update", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Permission data", required = true)
            @Valid @RequestBody PermissionsDTO permissionsDTO);

    /**
     * DELETE /api/permissions/:id : Delete the permissions with the specified id
     *
     * @param id the id of the permissions to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Operation(
        summary = "Delete a permission",
        description = "Deletes a permission by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Permission deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Permission not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePermissions(
            @Parameter(description = "ID of the permission to delete", required = true)
            @PathVariable @NotNull @Min(1) Long id);
}