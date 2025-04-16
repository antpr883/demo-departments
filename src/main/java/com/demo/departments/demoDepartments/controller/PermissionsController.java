package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.controller.swagger.api.PermissionsControllerEndpoint;
import com.demo.departments.demoDepartments.service.PermissionsService;
import com.demo.departments.demoDepartments.service.dto.security.PermissionsDTO;
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
 * REST controller for managing Permissions entities
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Validated
public class PermissionsController implements PermissionsControllerEndpoint {

    private final PermissionsService permissionsService;

    /**
     * Helper method to parse comma-separated attributes into a Set
     */
    private Set<String> parseAttributesParam(String attributes) {
        if (attributes == null || attributes.trim().isEmpty()) {
            return null;
        }
        return new HashSet<>(Arrays.asList(attributes.split(",")));
    }


    @Override
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<PermissionsDTO>> getPermissionsByRoleId(
            @PathVariable @NotNull @Min(1) Long roleId,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<PermissionsDTO> permissions = permissionsService.findByRoleId(roleId, withAudit, attributeSet);
        return ResponseEntity.ok(permissions);
    }

    /**
     * GET /api/permissions : Get all permissions with configurable options
     *
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<PermissionsDTO>> getAllPermissions(
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<PermissionsDTO> permissions = permissionsService.findAll(withAudit, attributeSet);
        return ResponseEntity.ok(permissions);
    }

    /**
     * GET /api/permissions/:id : Get a permission by ID with configurable options
     *
     * @param id the id of the permission to retrieve
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the permission in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PermissionsDTO> getPermissions(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        PermissionsDTO permissions = permissionsService.findById(id, withAudit, attributeSet);
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