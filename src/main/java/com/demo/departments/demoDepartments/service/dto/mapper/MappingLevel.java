package com.demo.departments.demoDepartments.service.dto.mapper;

/**
 * Defines mapping detail levels for DTOs
 * 
 * MINIMAL - Only entity fields without BaseDTO fields, no associations
 * BASIC - MINIMAL + BaseDTO fields
 * SUMMARY - BASIC + IDs of related entities
 * COMPLETE - Full entity with all nested associations
 */
public enum MappingLevel {
    MINIMAL,
    BASIC,
    SUMMARY,
    COMPLETE
}