package io.cui.test.valueobjects.objects;

import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.PropertySupport;
import io.cui.tools.collect.MapBuilder;
import io.cui.tools.string.Joiner;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Aggregates all information necessary to dynamically create Objects. In addition it makes some
 * sanity checks. It provides some convenience methods for accessing certain views on the
 * properties.
 *
 * @author Oliver Wolff
 */
@EqualsAndHashCode(of = "allProperties")
public class RuntimeProperties {

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties}.
     * May be empty
     */
    @Getter
    private final List<PropertyMetadata> allProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties}
     * that are required: {@link PropertyMetadata#isRequired()}. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> requiredProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties}
     * that are <em>NOT</em> {@link PropertyMetadata#isRequired()}. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> additionalProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties}
     * that provide a {@link PropertyMetadata#isDefaultValue()}. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> defaultProperties;

    /**
     * All {@link PropertyMetadata} contained by this {@link RuntimeProperties}
     * where the properties can be written. May be an empty list.
     */
    @Getter
    private final List<PropertyMetadata> writableProperties;

    /**
     * Constructor.
     *
     * @param properties may be null
     */
    public RuntimeProperties(
            final List<? extends PropertyMetadata> properties) {
        super();
        if (null == properties) {
            allProperties = Collections.emptyList();
        } else {
            allProperties = immutableList(properties);
        }
        requiredProperties = allProperties.stream()
                .filter(PropertyMetadata::isRequired)
                .collect(Collectors.toList());

        additionalProperties = allProperties.stream()
                .filter(metadata -> !metadata.isRequired())
                .collect(Collectors.toList());

        defaultProperties = allProperties.stream()
                .filter(PropertyMetadata::isDefaultValue)
                .collect(Collectors.toList());

        writableProperties = allProperties.stream()
                .filter(metadata -> metadata.getPropertyReadWrite().isWriteable())
                .collect(Collectors.toList());
    }

    /**
     * @param properties
     */
    public RuntimeProperties(final SortedSet<PropertyMetadata> properties) {
        this(immutableList(properties));
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} of the given
     * {@link Collection}
     *
     * @param propertyMetadata if null or empty an empty list will be returned
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @return the newly created mutable {@link List}
     */
    public static List<PropertySupport> mapToPropertySupport(
            final Collection<PropertyMetadata> propertyMetadata, final boolean generateTestValue) {
        final List<PropertySupport> list = new ArrayList<>();
        if (null == propertyMetadata || propertyMetadata.isEmpty()) {
            return list;
        }
        propertyMetadata.forEach(p -> list.add(new PropertySupport(p)));
        if (generateTestValue) {
            list.forEach(PropertySupport::generateTestValue);
        }
        return list;
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getAllProperties()}
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAllAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getAllProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getAllProperties()} but filtered according to the given names.
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @param filter containing the names to be filtered, must not be null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAllAsPropertySupport(final boolean generateTestValue,
            final Collection<String> filter) {
        requireNonNull(filter);
        return getAllAsPropertySupport(generateTestValue).stream()
                .filter(s -> filter.contains(s.getName())).collect(Collectors.toList());
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getRequiredProperties()}
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getRequiredAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getRequiredProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getRequiredProperties()} but filtered according to the given names.
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @param filter containing the names to be filtered, must not be null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getRequiredAsPropertySupport(final boolean generateTestValue,
            final Collection<String> filter) {
        requireNonNull(filter);
        return getRequiredAsPropertySupport(generateTestValue).stream()
                .filter(s -> filter.contains(s.getName())).collect(Collectors.toList());
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getDefaultProperties()}
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getDefaultAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getDefaultProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getDefaultProperties()} but filtered according to the given names.
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @param filter containing the names to be filtered, must not be null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getDefaultAsPropertySupport(final boolean generateTestValue,
            final Collection<String> filter) {
        requireNonNull(filter);
        return getDefaultAsPropertySupport(generateTestValue).stream()
                .filter(s -> filter.contains(s.getName())).collect(Collectors.toList());
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getAdditionalProperties()}
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAdditionalAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getAdditionalProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getAdditionalProperties()}
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @param filter containing the names to be filtered, must not be null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getAdditionalAsPropertySupport(final boolean generateTestValue,
            final Collection<String> filter) {
        requireNonNull(filter);
        return getAdditionalAsPropertySupport(generateTestValue).stream()
                .filter(s -> filter.contains(s.getName())).collect(Collectors.toList());
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getWritableProperties()}
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getWritableAsPropertySupport(final boolean generateTestValue) {
        return mapToPropertySupport(getWritableProperties(), generateTestValue);
    }

    /**
     * Creates a list of {@link PropertySupport} for each {@link PropertyMetadata} out of
     * {@link #getWritableProperties()}
     *
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @param filter containing the names to be filtered, must not be null
     * @return the newly created mutable {@link List}
     */
    public List<PropertySupport> getWritableAsPropertySupport(final boolean generateTestValue,
            final Collection<String> filter) {
        requireNonNull(filter);
        return getWritableAsPropertySupport(generateTestValue).stream()
                .filter(s -> filter.contains(s.getName())).collect(Collectors.toList());
    }

    /**
     * @param generateTestValue boolean indicating whether to call
     *            {@link PropertySupport#generateTestValue()} on each created element
     * @return a map view on all {@link PropertyMetadata} as {@link PropertySupport} with the
     *         property names as key
     */
    public Map<String, PropertySupport> asMapView(final boolean generateTestValue) {
        var builder = new MapBuilder<String, PropertySupport>();
        getAllAsPropertySupport(generateTestValue).forEach(p -> builder.put(p.getName(), p));
        return builder.toImmutableMap();
    }

    /**
     * Extracts the names of a given {@link Collection} of {@link PropertyMetadata}
     *
     * @param metadata if it is null or empty an empty {@link Set} will be returned
     * @return a set of the extracted names.
     */
    public static final Set<String> extractNames(final Collection<PropertyMetadata> metadata) {
        if (null == metadata || metadata.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<String> builder = new HashSet<>();
        metadata.forEach(m -> builder.add(m.getName()));
        return builder;
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder(getClass().getName());
        builder.append("\nRequired properties: ").append(getPropertyNames(requiredProperties));
        builder.append("\nAdditional properties: ")
                .append(getPropertyNames(additionalProperties));
        builder.append("\nDefault valued properties: ")
                .append(getPropertyNames(defaultProperties));
        builder.append("\nWritable properties: ")
                .append(getPropertyNames(writableProperties));
        return builder.toString();
    }

    private static String getPropertyNames(final List<PropertyMetadata> properties) {
        if (null == properties || properties.isEmpty()) {
            return "-";
        }
        final List<String> names = new ArrayList<>();
        properties.forEach(p -> names.add(p.getName()));
        return Joiner.on(", ").join(names);
    }
}
