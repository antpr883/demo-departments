package com.demo.departments.demoDepartments.service.dto.mapper;

import lombok.*;

import java.util.Set;

@Builder
@Data
public class MappingOptions {

    private MappingLevel level;
    private Set<String> fields;

    public boolean includes(String field) {
        return fields == null || fields.contains(field);
    }

    public boolean isMinimal() {
        return level != null && level == MappingLevel.MINIMAL;
    }

    public boolean isBasic() {
        return level != null && level == MappingLevel.BASIC;
    }

    public boolean isSummary() {
        return level != null && level == MappingLevel.SUMMARY;
    }

    public boolean isComplete() {
        return level != null && level == MappingLevel.COMPLETE;
    }

    public boolean isBasicOrAbove() {
        return level != null && level.ordinal() >= MappingLevel.BASIC.ordinal();
    }

    public boolean isSummaryOrAbove() {
        return level != null && level.ordinal() >= MappingLevel.SUMMARY.ordinal();
    }

    public boolean isCompleteOrAbove() {
        return level != null && level.ordinal() >= MappingLevel.COMPLETE.ordinal();
    }

    public boolean levelOrIncludes(String field, MappingLevel requiredLevel) {
        return (level != null && level.ordinal() >= requiredLevel.ordinal()) || includes(field);
    }
}