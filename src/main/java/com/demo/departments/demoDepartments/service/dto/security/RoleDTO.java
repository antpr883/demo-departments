package com.demo.departments.demoDepartments.service.dto.security;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Role data transfer object")
public class RoleDTO extends BaseDTO {
    
    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
    @Schema(description = "Role name", required = true)
    private String role;
    
    // Collections with simple IDs
    @Builder.Default
    @Schema(description = "Set of permission IDs assigned to this role")
    private Set<Long> permissionIds = new HashSet<>();
    
    // For responses that need full permission details
    @Builder.Default
    @Schema(description = "Set of permissions with full details")
    private Set<PermissionsDTO> permissions = new HashSet<>();
    
    // Reference to parent only by ID to avoid circular references
    @Schema(description = "ID of the person this role belongs to")
    private Long personId;
}