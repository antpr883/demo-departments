package com.demo.departments.demoDepartments.service.dto.mapper;

/**
 * Defines mapping detail levels for DTOs
 * 
 * MINIMAL - Only entity fields, no associations
 * BASIC - Entity fields plus IDs of related entities
 * SUMMARY - Entity fields plus essential fields from associations
 * COMPLETE - Full entity with all nested associations
 */
public enum MappingLevel {
    MINIMAL,
    BASIC,
    SUMMARY,
    COMPLETE
}