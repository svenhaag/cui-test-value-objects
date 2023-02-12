package io.cui.test.valueobjects.api.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.cui.test.generator.TypedGenerator;
import io.cui.test.valueobjects.generator.dynamic.DynamicTypedGenerator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.AssertionStrategy;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.property.util.PropertyAccessStrategy;
import io.cui.tools.property.PropertyMemberInfo;
import io.cui.tools.property.PropertyReadWrite;

/**
 * While the test classes are capable of auto-detecting JavaProperties you need to adjust them from
 * time to time. With this annotation you can do this.
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(PropertyConfigs.class)
public @interface PropertyConfig {

    /**
     * Identifies the name of the property
     *
     * @return the actual name of the property, must never be null nor empty
     * @see {@link PropertyMetadata#getName()}.
     */
    String name();

    /**
     * @return the type of the property. This can either be the actual type, in case
     *         {@link PropertyMetadata#getCollectionType()} is {@link CollectionType#NO_ITERABLE},
     *         the component-type in case of {@link CollectionType#ARRAY_MARKER} or the type
     *         argument for a collection for the other {@link CollectionType}s, see
     *         {@link PropertyMetadata#next()} and
     *         {@link PropertyMetadata#resolveActualClass()}
     * @see {@link PropertyMetadata#getPropertyClass()}.
     */
    Class<?> propertyClass();

    /**
     * @return the wrapped {@link TypedGenerator} to dynamically create properties. If it is not set
     *         {@link DynamicTypedGenerator} will be chosen
     * @see {@link PropertyMetadata#getGenerator()}.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends TypedGenerator> generator() default DynamicTypedGenerator.class;

    /**
     * @return boolean indicating whether the property defines a default value, defaults to false
     * @see {@link PropertyMetadata#isDefaultValue()}.
     */
    boolean defaultValue() default false;

    /**
     * @return boolean indicating whether the given property is required, defaults to false
     * @see {@link PropertyMetadata#isRequired()}.
     */
    boolean required() default false;

    /**
     * @return The {@link PropertyMemberInfo}, defaults to {@link PropertyMemberInfo#DEFAULT}
     * @see {@link PropertyMetadata#getPropertyMemberInfo()}.
     */
    PropertyMemberInfo propertyMemberInfo() default PropertyMemberInfo.DEFAULT;

    /**
     * In case there is a collectionType defined the generated values will implicitly wrapped in
     * the corresponding collection class defined by that wrapper, defaults to
     * {@link CollectionType#NO_ITERABLE}.
     *
     * @return the {@link CollectionType}
     * @see {@link PropertyMetadata#getCollectionType()}.
     */
    CollectionType collectionType() default CollectionType.NO_ITERABLE;

    /**
     * @return whether the property can be read or written, default to
     *         {@link PropertyReadWrite#READ_WRITE}
     * @see {@link PropertyMetadata#getPropertyReadWrite()}.
     */
    PropertyReadWrite propertyReadWrite() default PropertyReadWrite.READ_WRITE;

    /**
     * Defines different ways for reading / writing properties.
     *
     * @return the {@link PropertyAccessStrategy}, defaults to
     *         {@link PropertyAccessStrategy#BEAN_PROPERTY}
     * @see {@link PropertyMetadata#getPropertyAccessStrategy()}.
     */
    PropertyAccessStrategy propertyAccessStrategy() default PropertyAccessStrategy.BEAN_PROPERTY;

    /**
     * Defines the the way how to deal with equality regarding
     * PropertySupport.assertValueSet(Object)
     *
     * @return the {@link AssertionStrategy}, defaults to {@link AssertionStrategy#DEFAULT}
     */
    AssertionStrategy assertionStrategy() default AssertionStrategy.DEFAULT;
}
