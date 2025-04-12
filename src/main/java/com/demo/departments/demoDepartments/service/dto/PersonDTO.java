package com.demo.departments.demoDepartments.service.dto;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonDTO extends BaseDTO {
    
    // Setting for write-only password field (never returned in responses)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    
    // Simple collections with IDs only (for create/update operations)
    @Builder.Default
    private Set<Long> addressIds = new HashSet<>();
    @Builder.Default
    private Set<Long> contactIds = new HashSet<>();
    @Builder.Default
    private Set<Long> roleIds = new HashSet<>();
    
    // Full collections with nested DTOs (for read operations)
    @Builder.Default
    private Set<AddressDTO> addresses = new HashSet<>();
    @Builder.Default
    private Set<ContactDTO> contacts = new HashSet<>();
    @Builder.Default
    private Set<RoleDTO> roles = new HashSet<>();
}