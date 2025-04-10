package com.demo.departments.demoDepartments.service.dto;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import com.demo.departments.demoDepartments.persistence.model.ContactType;
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
public class ContactDTO extends BaseDTO {
    
    private ContactType contactType;
    private String phoneNumber;
    private String email;
    
    // Reference to parent only by ID to avoid circular references
    private Long personId;
}