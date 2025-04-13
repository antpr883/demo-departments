package com.demo.departments.demoDepartments.service.dto.mapper;

import com.demo.departments.demoDepartments.persistence.model.Address;
import com.demo.departments.demoDepartments.persistence.model.Contact;
import com.demo.departments.demoDepartments.persistence.model.Person;
import com.demo.departments.demoDepartments.persistence.model.base.PersistenceModel;
import com.demo.departments.demoDepartments.persistence.model.security.Permissions;
import com.demo.departments.demoDepartments.persistence.model.security.Role;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.Collections;
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
}