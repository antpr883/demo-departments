package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.controller.swagger.api.RoleControllerEndpoint;
import com.demo.departments.demoDepartments.service.RoleService;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
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
 * REST controller for managing Role entities
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
public class RoleController implements RoleControllerEndpoint {

    private final RoleService roleService;

    /**
     * GET /api/roles/:id/full : Get the role with specified attributes
     *
     * @param id the id of the role to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the role in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}/full")
    public ResponseEntity<RoleDTO> getRoleWithAttributes(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "attributes", required = false) String attributes) {
        List<String> attributeList = attributes != null
                ? Arrays.asList(attributes.split(","))
                : List.of();
        RoleDTO role = roleService.findByIdFull(id, attributeList);
        return ResponseEntity.ok(role);
    }

    @Override
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<RoleDTO>> getRolesByPersonId(
            @PathVariable @NotNull @Min(1) Long personId,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<RoleDTO> roles = roleService.findByPersonId(personId, mappingLevel);
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /api/roles : Get all roles with the specified mapping level
     *
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @Override
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles(
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<RoleDTO> roles = roleService.findAll(mappingLevel);
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /api/roles/:id : Get the role with the specified id and mapping level
     *
     * @param id the id of the role to retrieve
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the role in body,
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        RoleDTO role = roleService.findById(id, mappingLevel);
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