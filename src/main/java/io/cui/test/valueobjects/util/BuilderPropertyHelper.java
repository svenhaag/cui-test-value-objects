package io.cui.test.valueobjects.util;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.cui.test.valueobjects.api.property.PropertyBuilderConfig;
import io.cui.test.valueobjects.api.property.PropertyBuilderConfigs;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.BuilderMetadata;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.tools.collect.CollectionBuilder;
import io.cui.tools.reflect.MoreReflection;
import lombok.experimental.UtilityClass;

/**
 * Utility class for dealing with and the {@link PropertyBuilderConfig}
 * and {@link PropertyBuilderConfigs} annotations.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class BuilderPropertyHelper {

    /**
     * Checks the given type for the annotation {@link PropertyBuilderConfig} and
     * {@link PropertyBuilderConfigs} and puts all found in the immutable {@link List} to be
     * returned
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @param givenMetadata must not be null
     * @return immutable {@link List} of found {@link PropertyMetadata} elements derived by the
     *         annotations.
     */
    public static List<PropertyMetadata> handleBuilderPropertyConfigAnnotations(
            final Class<?> annotated, final List<PropertyMetadata> givenMetadata) {
        requireNonNull(annotated);
        final CollectionBuilder<PropertyMetadata> builder = new CollectionBuilder<>();

        extractConfiguredPropertyBuilderConfigs(annotated).forEach(config -> builder
                .add(builderPropertyConfigToBuilderMetadata(config, givenMetadata)));

        return builder.toImmutableList();
    }

    /**
     * Checks the given type for the annotation {@link PropertyBuilderConfig} and
     * {@link PropertyBuilderConfigs} and puts all found in the immutable {@link Set} to be returned
     *
     * @param annotated the class that may or may not provide the annotations, must not be null
     * @return immutable {@link Set} of found {@link PropertyBuilderConfig} elements.
     */
    public static Set<PropertyBuilderConfig> extractConfiguredPropertyBuilderConfigs(
            final Class<?> annotated) {
        requireNonNull(annotated);
        final CollectionBuilder<PropertyBuilderConfig> builder = new CollectionBuilder<>();

        MoreReflection.extractAllAnnotations(annotated, PropertyBuilderConfigs.class)
                .forEach(contract -> builder.add(Arrays.asList(contract.value())));
        MoreReflection.extractAllAnnotations(annotated, PropertyBuilderConfig.class)
                .forEach(builder::add);

        return builder.toImmutableSet();
    }

    private static PropertyMetadata builderPropertyConfigToBuilderMetadata(
            final PropertyBuilderConfig config, final Collection<PropertyMetadata> givenMetadata) {

        final Map<String, PropertyMetadata> map = new HashMap<>();
        givenMetadata.forEach(meta -> map.put(meta.getName(), meta));
        PropertyHelper.assertPropertyExists(config.name(), map);

        final PropertyMetadataImpl metatada =
            PropertyMetadataImpl.builder(map.get(config.name()))
                    .propertyAccessStrategy(config.propertyAccessStrategy())
                    .build();

        return BuilderMetadata.builder().delegateMetadata(metatada)
                .builderMethodName(config.builderMethodName())
                .builderMethodPrefix(config.methodPrefix())
                .builderSingleAddMethodName(config.builderSingleAddMethodName()).build();
    }

}
