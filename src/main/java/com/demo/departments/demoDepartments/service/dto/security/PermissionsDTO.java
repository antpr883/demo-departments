package com.demo.departments.demoDepartments.service.dto.security;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
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
public class PermissionsDTO extends BaseDTO {
    
    private String permission;
    
    // Reference to parent only by ID to avoid circular references
    private Long roleId;
}