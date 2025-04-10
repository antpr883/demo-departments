package com.demo.departments.demoDepartments.service.dto.security;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends BaseDTO {
    
    private String role;
    
    // Collections with simple IDs
    @Builder.Default
    private Set<Long> permissionIds = new HashSet<>();
    
    // For responses that need full permission details
    @Builder.Default
    private Set<PermissionsDTO> permissions = new HashSet<>();
    
    // Reference to parent only by ID to avoid circular references
    private Long personId;
}