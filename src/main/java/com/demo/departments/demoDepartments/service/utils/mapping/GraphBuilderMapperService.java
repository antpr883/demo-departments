package com.demo.departments.demoDepartments.service.utils.mapping;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.utils.mapping.MappingAttribute;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GraphBuilderMapperService {
    /**
     * Create an entity graph for the specified attributes
     */
    public EntityGraph getGraphWithAttributes(Class<?> rootClass, Collection<String> attributes) {
        Set<String> reducedPaths = reduceGraphPaths(attributes);
        DynamicEntityGraph.Builder builder = DynamicEntityGraph.fetching();

        for (String attributePath : reducedPaths) {
            addPathIfValid(builder, rootClass, attributePath);
        }

        return builder.build();
    }
    
    /**
     * Create a complete entity graph with all mappable attributes
     */
    public EntityGraph getCompleteEntityGraph(Class<?> rootClass) {
        DynamicEntityGraph.Builder builder = DynamicEntityGraph.fetching();
        Set<String> allPaths = getAllMappableAttributes(rootClass);
        
        for (String attributePath : allPaths) {
            builder.addPath(attributePath);
        }
        
        return builder.build();
    }
    
    /**
     * Get all mappable attributes for a class and its children
     */
    private Set<String> getAllMappableAttributes(Class<?> clazz) {
        Set<String> paths = new HashSet<>();
        collectMappableAttributes(clazz, "", paths, new HashSet<>());
        return paths;
    }
    
    /**
     * Recursively collect all mappable attributes
     */
    private void collectMappableAttributes(Class<?> clazz, String basePath, Set<String> paths, Set<Class<?>> visited) {
        // Avoid circular references
        if (visited.contains(clazz)) {
            return;
        }
        
        visited.add(clazz);
        
        for (Field field : clazz.getDeclaredFields()) {
            MappingAttribute mapping = field.getAnnotation(MappingAttribute.class);
            if (mapping == null) continue;
            
            String path = basePath.isEmpty() ? field.getName() : basePath + "." + field.getName();
            paths.add(path);
            
            // If it's a collection, check the generic type
            if (Collection.class.isAssignableFrom(field.getType())) {
                try {
                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    Class<?> genericClass = (Class<?>) type.getActualTypeArguments()[0];
                    collectMappableAttributes(genericClass, path, paths, new HashSet<>(visited));
                } catch (Exception e) {
                    // Skip if we can't determine the generic type
                }
            } 
            // If it's a regular class, recurse
            else if (!field.getType().isPrimitive() && !field.getType().getPackage().getName().startsWith("java.")) {
                collectMappableAttributes(field.getType(), path, paths, new HashSet<>(visited));
            }
        }
    }

    private Set<String> reduceGraphPaths(Collection<String> inputPaths) {
        return inputPaths.stream()
                .filter(path ->
                        inputPaths.stream().noneMatch(other ->
                                !path.equals(other) && path.startsWith(other + ".")
                        )
                )
                .collect(Collectors.toSet());
    }

    private void addPathIfValid(DynamicEntityGraph.Builder builder, Class<?> clazz, String attributePath) {
        String[] parts = attributePath.split("\\.");
        Class<?> currentClass = clazz;

        for (String fieldName : parts) {
            try {
                Field field = currentClass.getDeclaredField(fieldName);
                MappingAttribute mapping = field.getAnnotation(MappingAttribute.class);
                if (mapping == null) return;

                if (Collection.class.isAssignableFrom(field.getType())) {
                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    currentClass = (Class<?>) type.getActualTypeArguments()[0];
                } else {
                    currentClass = field.getType();
                }

            } catch (NoSuchFieldException e) {
                return;
            }
        }

        builder.addPath(attributePath);
    }
}
