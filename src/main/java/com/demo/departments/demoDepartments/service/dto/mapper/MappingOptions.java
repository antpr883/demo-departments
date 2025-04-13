package com.demo.departments.demoDepartments.service.dto.mapper;

import lombok.*;

import java.util.Set;

@Builder
@Data
public class MappingOptions {

    private MappingLevel level;
    private Set<String> fields;

    /**
     * Check if a field is included in the mapping
     * When fields is null/empty, all fields are included
     */
    public boolean includes(String field) {
        return fields == null || fields.isEmpty() || fields.contains(field);
    }

    /**
     * Check if mapping level is MINIMAL
     * (Only entity fields without BaseDTO fields)
     */
    public boolean isMinimal() {
        return level != null && level == MappingLevel.MINIMAL;
    }

    /**
     * Check if mapping level is BASIC
     * (MINIMAL + BaseDTO fields)
     */
    public boolean isBasic() {
        return level != null && level == MappingLevel.BASIC;
    }

    /**
     * Check if mapping level is SUMMARY
     * (BASIC + IDs of related entities)
     */
    public boolean isSummary() {
        return level != null && level == MappingLevel.SUMMARY;
    }

    /**
     * Check if mapping level is COMPLETE
     * (Full entity with nested associations)
     */
    public boolean isComplete() {
        return level != null && level == MappingLevel.COMPLETE;
    }

    /**
     * Check if mapping level is BASIC or higher
     */
    public boolean isBasicOrAbove() {
        return level != null && level.ordinal() >= MappingLevel.BASIC.ordinal();
    }

    /**
     * Check if mapping level is SUMMARY or higher
     */
    public boolean isSummaryOrAbove() {
        return level != null && level.ordinal() >= MappingLevel.SUMMARY.ordinal();
    }

    /**
     * Check if mapping level is COMPLETE
     */
    public boolean isCompleteOrAbove() {
        return level != null && level.ordinal() >= MappingLevel.COMPLETE.ordinal();
    }

    /**
     * Check if mapping level is at least the required level OR the field is explicitly included
     */
    public boolean levelOrIncludes(String field, MappingLevel requiredLevel) {
        return (level != null && level.ordinal() >= requiredLevel.ordinal()) || includes(field);
    }
}