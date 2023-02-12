package io.cui.test.valueobjects.util;

import static io.cui.test.valueobjects.util.PropertyHelper.handlePrimitiveAsDefaults;
import static io.cui.test.valueobjects.util.PropertyHelper.handlePropertyConfigAnnotations;
import static io.cui.test.valueobjects.util.PropertyHelper.handleWhiteAndBlacklist;
import static io.cui.test.valueobjects.util.PropertyHelper.handleWhiteAndBlacklistAsList;
import static io.cui.test.valueobjects.util.PropertyHelper.toMapView;
import static io.cui.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.property.impl.PropertyMetadataImpl;
import io.cui.test.valueobjects.testbeans.property.PropertyConfigMinimal;
import io.cui.test.valueobjects.testbeans.property.PropertyConfigMultiple;
import io.cui.test.valueobjects.testbeans.property.PropertyConfigPropertyClassAndGenerator;
import io.cui.test.valueobjects.testbeans.veto.ClassWithOneVeto;

class PropertyHelperTest {

    private static final String NAME_ATTRIBUTE = "name";

    @Test
    void shouldHandlePrimitives() {
        TypedGeneratorRegistry.registerBasicTypes();
        assertTrue(handlePrimitiveAsDefaults(Collections.emptyList()).isEmpty());
        final var metadata = handlePrimitiveAsDefaults(
                mutableList(ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null)));
        assertNotNull(metadata);
        assertFalse(metadata.isEmpty());
        final var map = toMapView(metadata);
        assertTrue(map.get("required").isDefaultValue());
        assertTrue(map.get("defaultValue").isDefaultValue());
        assertFalse(map.get(NAME_ATTRIBUTE).isDefaultValue());
        TypedGeneratorRegistry.clear();
    }

    // Tests for io.cui.test.valueobjects.util.AnnotationHelper.handlePropertyConfigAnnotations
    @Test
    void handlePropertyConfigAnnotationsShouldHandleMissingAnnotation() {
        assertTrue(handlePropertyConfigAnnotations(ClassWithOneVeto.class).isEmpty());
    }

    @Test
    void handlePropertyConfigAnnotationsShouldHandleAnnotations() {
        TypedGeneratorRegistry.registerBasicTypes();
        assertEquals(1, handlePropertyConfigAnnotations(PropertyConfigMinimal.class).size());
        assertEquals(2, handlePropertyConfigAnnotations(PropertyConfigMultiple.class).size());
        var propertyClassAndGenerator =
            handlePropertyConfigAnnotations(PropertyConfigPropertyClassAndGenerator.class);
        assertEquals(1, propertyClassAndGenerator.size());
        var config = propertyClassAndGenerator.iterator().next();
        assertEquals(Integer.class, config.getPropertyClass());
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldFilterBlackAndWhitelistAsMap() {
        TypedGeneratorRegistry.registerBasicTypes();
        final var metadata = handlePrimitiveAsDefaults(
                mutableList(ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null)));
        var map = handleWhiteAndBlacklist(new String[0], new String[0], metadata);
        assertEquals(metadata.size(), map.size());
        // White-listing
        map = handleWhiteAndBlacklist(new String[] { NAME_ATTRIBUTE }, new String[0], metadata);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(NAME_ATTRIBUTE));
        // Black-listing
        map = handleWhiteAndBlacklist(new String[0], new String[] { NAME_ATTRIBUTE }, metadata);
        assertEquals(metadata.size() - 1, map.size());
        assertFalse(map.containsKey(NAME_ATTRIBUTE));
        TypedGeneratorRegistry.clear();
    }

    @Test
    void shouldFilterBlackAndWhitelistAsList() {
        TypedGeneratorRegistry.registerBasicTypes();
        final var metadata = handlePrimitiveAsDefaults(
                mutableList(ReflectionHelper.scanBeanTypeForProperties(PropertyMetadataImpl.class, null)));
        var resultList =
            handleWhiteAndBlacklistAsList(new String[0], new String[0], new ArrayList<>(metadata));
        assertEquals(metadata.size(), resultList.size());
        // White-listing
        resultList =
            handleWhiteAndBlacklistAsList(new String[] { NAME_ATTRIBUTE }, new String[0], new ArrayList<>(metadata));
        assertEquals(1, resultList.size());
        assertTrue(toMapView(resultList).containsKey(NAME_ATTRIBUTE));
        // Black-listing
        resultList =
            handleWhiteAndBlacklistAsList(new String[0], new String[] { NAME_ATTRIBUTE }, new ArrayList<>(metadata));
        assertEquals(metadata.size() - 1, resultList.size());
        assertFalse(toMapView(resultList).containsKey(NAME_ATTRIBUTE));
        TypedGeneratorRegistry.clear();
    }
}
