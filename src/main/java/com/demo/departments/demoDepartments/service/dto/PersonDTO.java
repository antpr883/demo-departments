package com.demo.departments.demoDepartments.service.dto;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import com.demo.departments.demoDepartments.service.dto.security.RoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    private LocalDate birthday;
    
    // Simple collections with IDs only (for create/update operations)
    private Set<Long> addressIds = new HashSet<>();
    private Set<Long> contactIds = new HashSet<>(); 
    private Set<Long> roleIds = new HashSet<>();
    
    // Full collections with nested DTOs (for read operations)
    private Set<AddressDTO> addresses = new HashSet<>();
    private Set<ContactDTO> contacts = new HashSet<>();
    private Set<RoleDTO> roles = new HashSet<>();
}