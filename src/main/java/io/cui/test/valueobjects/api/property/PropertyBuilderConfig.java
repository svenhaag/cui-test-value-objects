package io.cui.test.valueobjects.api.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.PropertyAccessStrategy;

/**
 * While the test classes are capable of auto-detecting JavaProperties you need to adjust them from
 * time to time. With this annotation you can do this for builder specific properties. This
 * annotation can only adjust an already existing {@link PropertyMetadata} and can not be used
 * stand-alone
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(PropertyBuilderConfigs.class)
public @interface PropertyBuilderConfig {

    /**
     * @return the name of the property. Must not null nor empty. Must refer to an already existing
     *         {@link PropertyMetadata}
     */
    String name();

    /**
     * Defines different ways for reading / writing properties.
     *
     * @return the {@link PropertyAccessStrategy}, defaults to
     *         {@link PropertyAccessStrategy#BUILDER_DIRECT}
     */
    PropertyAccessStrategy propertyAccessStrategy() default PropertyAccessStrategy.BUILDER_DIRECT;

    /**
     * In case methodPrefix is not set the corresponding build method to be accessed for setting the
     * value is the name of the attribute: propertyName(), in case it is a concrete value, e.g.
     * 'with' it will taken into account: withPropertName().
     *
     * @return the method prefix, defaults to empty string
     */
    String methodPrefix() default "";

    /**
     * In case this builderMethodName is set it will be used directly for deriving the write-method.
     *
     * @return builderMethodName, defaults to empty string
     */
    String builderMethodName() default "";

    /**
     * Only needed for builder that deal with {@link Iterable} and single elements, see
     * {@link PropertyAccessStrategy#BUILDER_COLLECTION_AND_SINGLE_ELEMENT} for details
     *
     * @return builderSingleAddMethodName, defaults to empty string
     */
    String builderSingleAddMethodName() default "";
}
