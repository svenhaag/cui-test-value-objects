package io.cui.test.valueobjects.api.property;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.util.AssertionStrategy;
import io.cui.tools.property.PropertyMemberInfo;
import io.cui.tools.property.PropertyReadWrite;

/**
 * While the test classes are capable of auto-detecting JavaProperties you need to adjust them from
 * time to time. With this annotation you can do this for all properties detected
 *
 * @author Oliver Wolff
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface PropertyReflectionConfig {

    /**
     * @return boolean indicating whether to scan the class at all. if it is set {@code true} there
     *         will be no scanning at all.
     */
    boolean skip() default false;

    /**
     * @return an array of properties, identified by their names that are not to be considered for
     *         the tests: black-list. At this level of configuration this will skip the actual
     *         reflection-based scanning for that properties as well
     */
    String[] exclude() default {};

    /**
     * @return an array of properties, identified by their names that are to be considered for
     *         the tests: white-list
     */
    String[] of() default {};

    /**
     * @return an array of properties, identified by their names that are to be treated as required
     *         properties, see {@link PropertyMetadata#isRequired()}
     */
    String[] required() default {};

    /**
     * @return an array of properties, identified by their names that are to be treated as transient
     *         properties, see {@link PropertyMemberInfo#TRANSIENT}
     */
    String[] transientProperties() default {};

    /**
     * @return an array of properties, identified by their names that are to be treated as having a
     *         default values, see {@link PropertyMetadata#isDefaultValue()}
     */
    String[] defaultValued() default {};

    /**
     * @return an array of properties, identified by their names that are to be treated as being
     *         read-only, see {@link PropertyReadWrite#READ_ONLY}, usually used in conjunction with
     *         {@link #defaultValued()}
     */
    String[] readOnly() default {};

    /**
     * @return an array of properties, identified by their names that are to be treated as being
     *         write-only, see {@link PropertyReadWrite#WRITE_ONLY}, usually used in cases where a
     *         property to be written will result in other properties but itself can not be accessed
     *         directly
     */
    String[] writeOnly() default {};

    /**
     * @return an array of properties, identified by their names representing at least a
     *         {@link Collection} that are to be asserted ignoring the concrete order, see
     *         {@link PropertyConfig#assertionStrategy()} and
     *         {@link AssertionStrategy#COLLECTION_IGNORE_ORDER}. The default implementation will
     *         always respect / assert the same order of elements.
     */
    String[] assertUnorderedCollection() default {};
}
