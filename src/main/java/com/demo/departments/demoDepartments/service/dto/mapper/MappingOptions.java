package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.service.dto.MappingLevel;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MappingOptions {

    private MappingLevel level;

    public boolean isMinimal() {
        return MappingLevel.MINIMAL.equals(level);
    }

    public boolean isBasic() {
        return MappingLevel.BASIC.equals(level);
    }

    public boolean isSummary() {
        return MappingLevel.SUMMARY.equals(level);
    }

    public boolean isComplete() {
        return MappingLevel.COMPLETE.equals(level);
    }

    public boolean isBasicOrAbove() {
        return level.ordinal() >= MappingLevel.BASIC.ordinal();
    }

    public boolean isSummaryOrAbove() {
        return level.ordinal() >= MappingLevel.SUMMARY.ordinal();
    }

    public boolean isCompleteOrAbove() {
        return level.ordinal() >= MappingLevel.COMPLETE.ordinal();
    }

    public static MappingOptions of(MappingLevel level) {
        return MappingOptions.builder().level(level).build();
    }

    public static MappingOptions minimal() {
        return of(MappingLevel.MINIMAL);
    }

    public static MappingOptions basic() {
        return of(MappingLevel.BASIC);
    }

    public static MappingOptions summary() {
        return of(MappingLevel.SUMMARY);
    }

    public static MappingOptions complete() {
        return of(MappingLevel.COMPLETE);
    }
}
