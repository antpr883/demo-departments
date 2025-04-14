package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.controller.swagger.api.PermissionsControllerEndpoint;
import com.demo.departments.demoDepartments.service.PermissionsService;
import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
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
 * REST controller for managing Permissions entities
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Validated
public class PermissionsController implements PermissionsControllerEndpoint {

    private final PermissionsService permissionsService;

    /**
     * GET /api/permissions/:id/full : Get the permissions with specified attributes
     *
     * @param id the id of the permissions to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the permissions in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}/full")
    public ResponseEntity<PermissionsDTO> getPermissionsWithAttributes(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "attributes", required = false) String attributes) {
        List<String> attributeList = attributes != null
                ? Arrays.asList(attributes.split(","))
                : List.of();
        PermissionsDTO permissions = permissionsService.findByIdFull(id, attributeList);
        return ResponseEntity.ok(permissions);
    }

    @Override
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<PermissionsDTO>> getPermissionsByRoleId(
            @PathVariable @NotNull @Min(1) Long roleId,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<PermissionsDTO> permissions = permissionsService.findByRoleId(roleId, mappingLevel);
        return ResponseEntity.ok(permissions);
    }

    /**
     * GET /api/permissions : Get all permissions with the specified mapping level
     *
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<PermissionsDTO>> getAllPermissions(
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<PermissionsDTO> permissions = permissionsService.findAll(mappingLevel);
        return ResponseEntity.ok(permissions);
    }

    /**
     * GET /api/permissions/:id : Get the permissions with the specified id and mapping level
     *
     * @param id the id of the permissions to retrieve
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the permissions in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PermissionsDTO> getPermissions(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        PermissionsDTO permissions = permissionsService.findById(id, mappingLevel);
        return ResponseEntity.ok(permissions);
    }

    /**
     * POST /api/permissions : Create a new permission
     *
     * @param permissionsDTO the permission to create
     * @return the ResponseEntity with status 201 (Created) and the new permission in body
     */
    @Override
    @PostMapping
    public ResponseEntity<PermissionsDTO> createPermissions(
            @Valid @RequestBody PermissionsDTO permissionsDTO) {
        PermissionsDTO result = permissionsService.save(permissionsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/permissions/:id : Update an existing permissions
     *
     * @param id the id of the permissions to update
     * @param permissionsDTO the permissions to update
     * @return the ResponseEntity with status 200 (OK) and the updated permissions in body,
     * or with status 404 (Not Found)
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PermissionsDTO> updatePermissions(
            @PathVariable @NotNull @Min(1) Long id,
            @Valid @RequestBody PermissionsDTO permissionsDTO) {
        PermissionsDTO result = permissionsService.update(id, permissionsDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/permissions/:id : Delete the permissions with the specified id
     *
     * @param id the id of the permissions to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermissions(
            @PathVariable @NotNull @Min(1) Long id) {
        permissionsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}