package com.demo.departments.demoDepartments.service.dto.security;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Permissions data transfer object")
public class PermissionsDTO extends BaseDTO {
    
    @NotBlank(message = "Permission is required")
    @Size(min = 3, max = 50, message = "Permission must be between 3 and 50 characters")
    @Schema(description = "Permission name", required = true)
    private String permission;
    
    // Reference to parent only by ID to avoid circular references
    @Schema(description = "ID of the role this permission belongs to")
    private Long roleId;
}