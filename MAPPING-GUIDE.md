# Entity-DTO Mapping Guide

## Overview

This project implements a simple but effective entity-to-DTO mapping system with two distinct approaches:

1. **Level-based mapping** - Standard mapping using predefined levels
2. **Attribute-based mapping** - Dynamic, precise control with entity graphs

## Two Mapping Approaches

### 1. Level-Based Mapping (No Entity Graphs)

The standard approach uses predefined mapping levels without entity graphs:

- **MINIMAL**: Only entity fields without BaseDTO fields, no associations
- **BASIC**: MINIMAL + BaseDTO fields (id, audit fields, etc.)
- **SUMMARY**: BASIC + IDs of related entities
- **COMPLETE**: Full entity representation with nested associations

This works with already loaded entities (no special fetching):

```java
// Minimal person data (just business fields)
PersonDTO minimalPerson = personService.findById(1L, MappingLevel.MINIMAL);

// Basic person data (with audit fields)
PersonDTO basicPerson = personService.findById(1L, MappingLevel.BASIC);

// Summary person data (with related entity IDs)
PersonDTO summaryPerson = personService.findById(1L, MappingLevel.SUMMARY);

// Complete person data (still fetches only the person entity)
PersonDTO completePerson = personService.findById(1L, MappingLevel.COMPLETE);
```

### 2. Attribute-Based Mapping (With Entity Graphs)

For precise control over which relationships to load and map, use attribute-based mapping. If no attributes are specified, all attributes are included by default:

```java
// Load all attributes (default behavior)
PersonDTO fullPerson = personService.findByIdFull(1L, null);
// OR
PersonDTO fullPerson = personService.findByIdFull(1L, Collections.emptyList());

// Only load addresses (uses entity graph to fetch them)
List<String> justAddresses = List.of("addresses");
PersonDTO personWithAddresses = personService.findByIdFull(1L, justAddresses);

// Load nested relationships (uses entity graph to fetch the relationship chain)
List<String> securityInfo = List.of("roles.permissions");
PersonDTO personWithSecurity = personService.findByIdFull(1L, securityInfo);

// Load multiple specific relationships
List<String> customAttributes = List.of("addresses", "contacts", "roles.permissions");
PersonDTO customPerson = personService.findByIdFull(1L, customAttributes);
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
public PersonDTO findById(Long id, MappingLevel level) {
    // Simple fetch without graph
    Person person = personRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    
    // Map according to level
    MappingOptions options = MappingOptions.builder()
            .level(level)
            .build();
    
    return personMapper.toDtoWithOptions(person, options);
}
```

### Attribute-Based Mapping

```java
public PersonDTO findByIdFull(Long id, List<String> attributes) {
    // Create dynamic entity graph based on attributes
    EntityGraph graph = graphBuilderMappingService.getGraphWithAttributes(
            Person.class, attributes);
    
    // Fetch with the precise graph
    Person person = personRepository.findById(id, graph)
            .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    
    // Map only requested fields
    MappingOptions options = MappingOptions.builder()
            .fields(attributes)
            .level(MappingLevel.COMPLETE)
            .build();
    
    return personMapper.toDtoWithOptions(person, options);
}
```

## Best Practices

1. **For standard use cases**: Use level-based mapping
   ```java
   personService.findById(id, MappingLevel.BASIC);
   ```

2. **For specific relationship needs**: Use attribute-based mapping
   ```java
   personService.findByIdFull(id, List.of("addresses", "roles.permissions"));
   ```

3. **For performance-critical lists**: Use level-based mapping with MINIMAL or BASIC level
   ```java
   personService.findAll(MappingLevel.BASIC);
   ```

4. **For complex UI components**: Use attribute-based mapping with just the needed relations
   ```java
   personService.findByIdFull(id, List.of("contacts"));
   ```

This dual approach gives you the simplicity of standard mapping levels while enabling precise control when needed for specific scenarios.