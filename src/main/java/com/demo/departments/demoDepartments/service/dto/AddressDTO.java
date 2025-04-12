package com.demo.departments.demoDepartments.service.dto;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import com.demo.departments.demoDepartments.persistence.model.AddressType;
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
public class AddressDTO extends BaseDTO {
    
    private AddressType type;
    private String street;
    private String postZipCode;
    private String province;
    private String city;
    private String country;

    // Reference to parent only by ID to avoid circular references
    private Long personId;
}