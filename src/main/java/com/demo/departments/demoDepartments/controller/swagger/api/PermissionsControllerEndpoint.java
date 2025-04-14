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
     * GET /api/permissions : Get all permissions with the specified mapping level
     *
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
     */
    @Operation(
        summary = "Get all permissions",
        description = "Returns all permissions with the specified mapping level"
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
            @Parameter(description = "Mapping level: MINIMAL, BASIC, SUMMARY, COMPLETE", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/permissions/:id : Get the permissions with the specified id and mapping level
     *
     * @param id the id of the permissions to retrieve
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the permissions in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a permission by ID",
        description = "Retrieves a permission by its ID with the specified mapping level"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionsDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ID or mapping level",
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
            @Parameter(description = "Mapping level", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

    /**
     * GET /api/permissions/:id/full : Get the permissions with specified attributes
     *
     * @param id the id of the permissions to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the permissions in body,
     * or with status 404 (Not Found)
     */
    @Operation(
        summary = "Get a permission with specified attributes",
        description = "Retrieves a permission by its ID with specified attributes included"
    )
    @GetMapping("/{id}/full")
    ResponseEntity<PermissionsDTO> getPermissionsWithAttributes(
            @Parameter(description = "ID of the permission to retrieve", required = true)
            @PathVariable @NotNull @Min(1) Long id,
            @Parameter(description = "Comma-separated list of attributes to include")
            @RequestParam(name = "attributes", required = false) String attributes);

    /**
     * GET /api/permissions/role/:roleId : Get all permissions for a role
     *
     * @param roleId the id of the role
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
     */
    @Operation(
        summary = "Get all permissions for a role",
        description = "Returns all permissions for a role with the specified mapping level"
    )
    @GetMapping("/role/{roleId}")
    ResponseEntity<List<PermissionsDTO>> getPermissionsByRoleId(
            @Parameter(description = "ID of the role", required = true)
            @PathVariable @NotNull @Min(1) Long roleId,
            @Parameter(description = "Mapping level", schema = @Schema(allowableValues = {"MINIMAL", "BASIC", "SUMMARY", "COMPLETE"}))
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level);

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