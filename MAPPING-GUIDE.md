# Entity-DTO Mapping Guide

## Overview

This project implements a simple but effective entity-to-DTO mapping system with two distinct approaches:

1. **Level-based mapping** - Standard mapping using predefined levels
2. **Attribute-based mapping** - Dynamic, precise control with entity graphs

Both approaches are available for both single entity and collection mapping operations.

## Two Mapping Approaches

### 1. Level-Based Mapping (No Entity Graphs)

The standard approach uses predefined mapping levels without entity graphs:

- **MINIMAL**: Only entity fields without BaseDTO fields, no associations
- **BASIC**: MINIMAL + BaseDTO fields (id, audit fields, etc.)
- **SUMMARY**: BASIC + IDs of related entities + count of associations
- **COMPLETE**: Full entity representation with nested associations

This works with already loaded entities (no special fetching):

```java
// Single entity with different levels
PersonDTO minimalPerson = personService.findById(1L, MappingLevel.MINIMAL);
PersonDTO basicPerson = personService.findById(1L, MappingLevel.BASIC);
PersonDTO summaryPerson = personService.findById(1L, MappingLevel.SUMMARY);
PersonDTO completePerson = personService.findById(1L, MappingLevel.COMPLETE);

// Collections with different levels
List<PersonDTO> minimalPersons = personService.findAll(MappingLevel.MINIMAL);
List<PersonDTO> basicPersons = personService.findAll(MappingLevel.BASIC);
List<PersonDTO> summaryPersons = personService.findAll(MappingLevel.SUMMARY);
List<PersonDTO> completePersons = personService.findAll(MappingLevel.COMPLETE);
```

### 2. Attribute-Based Mapping (With Entity Graphs)

For precise control over which relationships to load and map, use attribute-based mapping. If no attributes are specified, all attributes are included by default:

```java
// Single entity with different attribute sets
PersonDTO fullPerson = personService.findByIdFull(1L, null);
PersonDTO personWithAddresses = personService.findByIdFull(1L, List.of("addresses"));
PersonDTO personWithSecurity = personService.findByIdFull(1L, List.of("roles.permissions"));
PersonDTO customPerson = personService.findByIdFull(1L, List.of("addresses", "contacts", "roles"));

// Collections with different attribute sets
List<PersonDTO> allPersons = personService.findAllFull(null);
List<PersonDTO> personsWithAddresses = personService.findAllFull(List.of("addresses"));
List<PersonDTO> personsWithSecurity = personService.findAllFull(List.of("roles.permissions"));
List<PersonDTO> customPersons = personService.findAllFull(List.of("addresses", "contacts", "roles"));
```

## Mapper Architecture

The system is built around a layered structure of mapping interfaces:

1. **EntityMapper**: Base mapping interface for DTOs with option-based mapping
2. **Entity-specific mappers**: Concrete implementations for Person, Address, etc.

Collection mapping is fully supported:

```java
// Collection mapping methods in EntityMapper
List<D> toDtoList(List<E> entities);
List<D> toDtoListWithOptions(List<E> entities, @Context MappingOptions options);
List<D> toDtoListByLevel(List<E> entities, MappingLevel level);

Set<D> toDtoSet(Set<E> entities);
Set<D> toDtoSetWithOptions(Set<E> entities, @Context MappingOptions options);
Set<D> toDtoSetByLevel(Set<E> entities, MappingLevel level);
```

## Unified DTO Approach

Instead of having separate DTOs for different mapping levels (e.g., PersonDTO and PersonSummaryDTO), we now use a single unified DTO class that adapts to different mapping levels by selectively populating fields:

```java
public class PersonDTO extends BaseDTO {
    // Basic fields (always populated)
    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    
    // Summary counts (populated at SUMMARY level)
    private Integer addressCount;
    private Integer contactCount;
    private Integer roleCount;
    
    // IDs collections (populated at SUMMARY level)
    private Set<Long> addressIds = new HashSet<>();
    private Set<Long> contactIds = new HashSet<>();
    private Set<Long> roleIds = new HashSet<>();
    
    // Full collections (populated at COMPLETE level)
    private Set<AddressDTO> addresses = new HashSet<>();
    private Set<ContactDTO> contacts = new HashSet<>();
    private Set<RoleDTO> roles = new HashSet<>();
}
```

## Key Differences

| Feature | Level-Based | Attribute-Based |
|---------|-------------|----------------|
| **Entity Graph Usage** | No | Yes |
| **Fetch Strategy** | Standard JPA fetch | Dynamic entity graph |
| **Performance** | May have N+1 issues | Optimized fetching |
| **Flexibility** | Fixed predefined levels | Custom attribute selection |
| **Use Case** | Simple standard needs | Specific relational data needs |

## Implementation Details

### Level-Based Mapping

```java
// Single entity mapping
public D findById(Long id, MappingLevel level) {
    // Simple fetch without graph
    E entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    
    // Map according to level
    MappingOptions options = MappingOptions.builder()
            .level(level)
            .build();
    
    return mapper.toDtoWithOptions(entity, options);
}

// Collection mapping
public List<D> findAll(MappingLevel level) {
    // Simple fetch without graph
    List<E> entities = repository.findAll();
    
    // Map according to level
    MappingOptions options = MappingOptions.builder()
            .level(level)
            .build();
    
    return mapper.toDtoListWithOptions(entities, options);
}
```

### Attribute-Based Mapping

```java
// Single entity mapping
public D findByIdFull(Long id, List<String> attributes) {
    // Convert list to set for faster lookups
    Set<String> attributeSet = attributes != null ? new HashSet<>(attributes) : Collections.emptySet();
    
    // Generate appropriate entity graph
    EntityGraph graph;
    if (attributeSet.isEmpty()) {
        // If no attributes specified, get full entity graph
        graph = graphBuilderService.getCompleteEntityGraph(entityClass);
    } else {
        // Otherwise get graph just for requested attributes
        graph = graphBuilderService.getGraphWithAttributes(entityClass, attributeSet);
    }

    // Fetch entity with the dynamic graph
    E entity = repository.findById(id, graph)
            .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

    // Create mapping options
    MappingOptions options;
    if (attributeSet.isEmpty()) {
        // If no attributes specified, return complete mapping
        options = MappingOptions.builder()
                .level(MappingLevel.COMPLETE)
                .build();
    } else {
        // Otherwise return only requested attributes with SUMMARY level
        options = MappingOptions.builder()
                .fields(attributeSet)
                .level(MappingLevel.SUMMARY)
                .build();
    }

    // Map entity to DTO with options
    return mapper.toDtoWithOptions(entity, options);
}

// Collection mapping
public List<D> findAllFull(List<String> attributes) {
    // Similar implementation with entity graph
    // Fetch entities with the dynamic attribute graph
    List<E> entities = repository.findAll(graph);
    
    // Map entities to DTOs with options
    return mapper.toDtoListWithOptions(entities, options);
}
```

## Architecture Overview

The mapping system is built on several layers:

1. **Service Layer**:
   - `BaseService` - Common interface with level and attribute-based methods
   - `AbstractBaseService` - Reusable implementation of all mapping methods
   - Entity-specific services extend this base

2. **Mapper Layer**:
   - `EntityMapper` - Advanced interface with options and collection mapping
   - Entity-specific mappers with custom mapping logic

3. **Graph Building Layer**:
   - `GraphBuilderMapperService` - Builds entity graphs from attribute paths
   - Handles both specific attributes and complete entity relationships

## Best Practices

1. **For data tables**: Use level-based approach with MINIMAL or BASIC level
   ```java
   // Efficient for displaying many rows
   List<PersonDTO> tableData = personService.findAll(MappingLevel.MINIMAL);
   ```

2. **For detail views**: Use attribute-based mapping with the exact fields needed
   ```java
   // Precise loading for entity detail page
   PersonDTO details = personService.findByIdFull(id, 
       List.of("addresses", "contacts", "roles.permissions"));
   ```

3. **For autocomplete/dropdowns**: Use SUMMARY level for display and ID reference
   ```java
   // Good for selects that show name + ID
   List<PersonDTO> options = personService.findAll(MappingLevel.SUMMARY);
   ```

4. **For full data export**: Use COMPLETE level or empty attributes list
   ```java
   // Gets everything in the most efficient way
   List<PersonDTO> exportData = personService.findAllFull(null);
   ```

This comprehensive approach gives you both simplicity and flexibility - standard mapping levels for common scenarios and precise attribute-based loading when you need exact control.