package com.demo.departments.demoDepartments.service.utils.mapping;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.demo.departments.demoDepartments.persistence.utils.mapping.MappingAttribute;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GraphBuilderMapperService {
    public EntityGraph getGraphWithAttributes(Class<?> rootClass, Collection<String> attributes) {
        Set<String> reducedPaths = reduceGraphPaths(attributes);
        DynamicEntityGraph.Builder builder = DynamicEntityGraph.fetching();

        for (String attributePath : reducedPaths) {
            addPathIfValid(builder, rootClass, attributePath);
        }

        return builder.build();
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
