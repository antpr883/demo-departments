package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.controller.swagger.api.RoleControllerEndpoint;
import com.demo.departments.demoDepartments.service.RoleService;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
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
 * REST controller for managing Role entities
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
public class RoleController implements RoleControllerEndpoint {

    private final RoleService roleService;

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
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<RoleDTO>> getRolesByPersonId(
            @PathVariable @NotNull @Min(1) Long personId,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<RoleDTO> roles = roleService.findByPersonId(personId, withAudit, attributeSet);
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /api/roles : Get all roles with configurable options
     *
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles(
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<RoleDTO> roles = roleService.findAll(withAudit, attributeSet);
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /api/roles/:id : Get a role by ID with configurable options
     *
     * @param id the id of the role to retrieve
     * @param withAudit If true, include audit information
     * @param attributes Comma-separated list of attributes to include
     * @return the ResponseEntity with status 200 (OK) and the role in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        RoleDTO role = roleService.findById(id, withAudit, attributeSet);
        return ResponseEntity.ok(role);
    }

    /**
     * POST /api/roles : Create a new role
     *
     * @param roleDTO the role to create
     * @return the ResponseEntity with status 201 (Created) and the new role in body
     */
    @Override
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(
            @Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO result = roleService.save(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/roles/:id : Update an existing role
     *
     * @param id the id of the role to update
     * @param roleDTO the role to update
     * @return the ResponseEntity with status 200 (OK) and the updated role in body,
     * or with status 404 (Not Found)
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable @NotNull @Min(1) Long id,
            @Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO result = roleService.update(id, roleDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/roles/:id : Delete the role with the specified id
     *
     * @param id the id of the role to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable @NotNull @Min(1) Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}