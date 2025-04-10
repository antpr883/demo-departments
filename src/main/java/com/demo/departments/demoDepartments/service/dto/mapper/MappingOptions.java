package com.demo.departments.demoDepartments.service.dto.mapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MappingOptions {
    
    @Builder.Default
    private boolean includeId = true;
    
    @Builder.Default
    private boolean includeBasicProperties = true;
    
    @Builder.Default
    private boolean includeAuditFields = false;
    
    @Builder.Default
    private boolean includeAddresses = true;
    
    @Builder.Default
    private boolean includeContacts = true;
    
    @Builder.Default
    private boolean includePermissions = false;
    
    @Builder.Default
    private boolean includeRoles = false;
    
    public static MappingOptions defaultOptions() {
        return MappingOptions.builder().build();
    }
    
    public static MappingOptions minimal() {
        return MappingOptions.builder()
                .includeAddresses(false)
                .includeContacts(false)
                .build();
    }
    
    public static MappingOptions summary() {
        return MappingOptions.builder()
                .includeAddresses(false)
                .includeContacts(false)
                .build();
    }
    
    public static MappingOptions complete() {
        return MappingOptions.builder()
                .includeAuditFields(true)
                .includePermissions(true)
                .includeRoles(true)
                .build();
    }
}