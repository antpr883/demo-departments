package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.persistence.model.Contact;
import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import org.mapstruct.Named;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility methods for mappers to avoid circular references and handle
 * common mapping patterns consistently
 */
public class MapperUtils {

    /**
     * Extract IDs from a collection of entities
     */
    @Named("extractIds")
    public static <T extends PersistenceModel> Set<Long> extractIds(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptySet();
        }
        return entities.stream()
                .filter(Objects::nonNull)
                .map(PersistenceModel::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Check if an entity has a specific role in its ancestry
     * to prevent infinite recursion during mapping
     */
    @Named("hasAncestorOfType")
    public static boolean hasAncestorOfType(PersistenceModel entity, Class<?> type) {
        if (entity == null) {
            return false;
        }

        // Check based on entity type
        if (entity instanceof Address address) {
            return type == Person.class && address.getPerson() != null;
        } else if (entity instanceof Contact contact) {
            return type == Person.class && contact.getPerson() != null;
        } else if (entity instanceof Role role) {
            return type == Person.class && role.getPerson() != null;
        } else if (entity instanceof Permissions permissions) {
            if (type == Role.class && permissions.getRole() != null) {
                return true;
            }
            return type == Person.class && permissions.getRole() != null && permissions.getRole().getPerson() != null;
        }
        
        return false;
    }
    
    /**
     * Safe count of collection size
     */
    @Named("safeCount")
    public static <T> int safeCount(Collection<T> collection) {
        return collection != null ? collection.size() : 0;
    }

    /**
     * Maps null to empty set to avoid null pointer exceptions
     */
    @Named("emptyIfNull")
    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    /**
     * Checks if a collection is not empty
     */
    @Named("isNotEmpty")
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
    
    /**
     * Check if permissions should be included due to a nested path like "roles.permissions"
     */
    @Named("hasPermissionsInNestedPath")
    public static boolean hasPermissionsInNestedPath(MappingOptions options) {
        if (options.getAttributes() == null || options.getAttributes().isEmpty()) {
            return false;
        }
        
        for (String attr : options.getAttributes()) {
            // Check if there's any attribute with a path like "roles.permissions" or similar
            if (attr.contains(".permissions")) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if a field in a class is of collection type
     * This is a more robust approach than string-based heuristics
     */
    @Named("isFieldCollection")
    public static boolean isFieldCollection(Class<?> clazz, String fieldName) {
        try {
            Field field = getFieldRecursively(clazz, fieldName);
            if (field == null) {
                return false;
            }
            
            return Collection.class.isAssignableFrom(field.getType()) ||
                   Map.class.isAssignableFrom(field.getType()) ||
                   field.getType().isArray();
        } catch (Exception e) {
            // If we can't determine, fall back to naming convention
            return false;
        }
    }
    
    /**
     * Gets a field from a class or its superclasses
     */
    private static Field getFieldRecursively(Class<?> clazz, String fieldName) {
        if (clazz == null || clazz == Object.class) {
            return null;
        }
        
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // If not found, try the superclass
            return getFieldRecursively(clazz.getSuperclass(), fieldName);
        }
    }
    
    /**
     * Check if roles should be included due to a nested path like "roles.permissions"
     */
    @Named("hasNestedRolesPath")
    public static boolean hasNestedRolesPath(MappingOptions options) {
        if (options.getAttributes() == null || options.getAttributes().isEmpty()) {
            return false;
        }
        
        for (String attr : options.getAttributes()) {
            // Check if there's any attribute that starts with "roles."
            if (attr.startsWith("roles.")) {
                return true;
            }
        }
        
        return false;
    }
}