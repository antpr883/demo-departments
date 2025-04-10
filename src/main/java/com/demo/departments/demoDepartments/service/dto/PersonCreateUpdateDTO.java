package com.demo.departments.demoDepartments.service.dto;

import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Specialized DTO for creating and updating Person entities
 * Includes validation annotations and only fields that can be modified
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateUpdateDTO {
    
    // ID is only used for updates, not for creation
    private Long id;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;
    
    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;
    
    // Include nested objects for create/update
    @Builder.Default
    private Set<AddressDTO> addresses = new HashSet<>();
    @Builder.Default
    private Set<ContactDTO> contacts = new HashSet<>();
    @Builder.Default
    private Set<RoleDTO> roles = new HashSet<>();
}