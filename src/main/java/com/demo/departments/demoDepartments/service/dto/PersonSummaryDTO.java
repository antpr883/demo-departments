package com.demo.departments.demoDepartments.service.dto;

import com.demo.departments.demoDepartments.service.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Lightweight DTO for Person entity that includes only essential information
 * Used for list views and search results where full details aren't needed
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonSummaryDTO extends BaseDTO {
    
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    
    // Summary counts of related entities
    private int addressCount;
    private int contactCount;
    private int roleCount;
}