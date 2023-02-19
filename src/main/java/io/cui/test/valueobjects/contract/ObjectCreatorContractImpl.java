package io.cui.test.valueobjects.contract;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import io.cui.test.valueobjects.api.TestContract;
import io.cui.test.valueobjects.api.contracts.VerifyConstructor;
import io.cui.test.valueobjects.api.contracts.VerifyConstructors;
import io.cui.test.valueobjects.api.contracts.VerifyFactoryMethod;
import io.cui.test.valueobjects.api.contracts.VerifyFactoryMethods;
import io.cui.test.valueobjects.objects.ParameterizedInstantiator;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.ConstructorBasedInstantiator;
import io.cui.test.valueobjects.objects.impl.FactoryBasedInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.PropertySupport;
import io.cui.test.valueobjects.util.AnnotationHelper;
import io.cui.tools.collect.CollectionBuilder;
import io.cui.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * TestContract for dealing Constructor and factories, {@link VerifyConstructor} and
 * {@link VerifyFactoryMethod} respectively
 *
 * @author Oliver Wolff
 * @param <T> identifying the objects to be tested.
 */
@RequiredArgsConstructor
public class ObjectCreatorContractImpl<T> implements TestContract<T> {

    private static final CuiLogger log = new CuiLogger(ObjectCreatorContractImpl.class);

    @Getter
    @NonNull
    private final ParameterizedInstantiator<T> instantiator;

    @Override
    public void assertContract() {
        final var builder = new StringBuilder("Verifying ");
        builder.append(getClass().getName()).append("\nWith configuration: ")
                .append(this.instantiator.toString());
        log.info(builder.toString());

        shouldPersistAllParameter();
        shouldHandleRequiredAndDefaults();
        shouldFailOnMissingRequiredAttributes();
    }

    private void shouldFailOnMissingRequiredAttributes() {
        final var information =
            getInstantiator().getRuntimeProperties();
        final var required = information.getRequiredAsPropertySupport(true);

        for (final PropertySupport support : required) {
            if (!support.isPrimitive()) {
                final List<PropertySupport> iterating = new ArrayList<>(required);
                iterating.remove(support);
                iterating.add(support.createCopy(false));
                var failed = false;
                try {
                    getInstantiator().newInstance(iterating, false);
                    failed = true;
                } catch (final AssertionError e) {
                    // expected
                }
                if (failed) {
                    throw new AssertionError(
                            "Object Should not build due to missing required attribute " + support);
                }
            }
        }
    }

    private void shouldHandleRequiredAndDefaults() {
        final var information =
            getInstantiator().getRuntimeProperties();

        final var required = information.getRequiredAsPropertySupport(true);
        final var instance = getInstantiator().newInstance(required, false);

        for (final PropertySupport support : required) {
            if (support.isReadable()) {
                support.assertValueSet(instance);
            }
        }

        for (final PropertySupport support : information.getDefaultAsPropertySupport(false)) {
            if (support.isReadable()) {
                support.assertDefaultValue(instance);
            }
        }

        for (final PropertySupport support : information.getAdditionalAsPropertySupport(false)) {
            if (support.isReadable() && !support.isDefaultValue()) {
                support.assertValueSet(instance);
            }
        }

    }

    private void shouldPersistAllParameter() {
        final var properties = this.instantiator
                .getRuntimeProperties().getAllAsPropertySupport(true);

        final var instance = this.instantiator.newInstance(properties, false);
        for (final PropertySupport support : properties) {
            if (support.isReadable()) {
                support.assertValueSet(instance);
            }
        }
    }

    /**
     * Factory method for creating a {@link List} of instances of {@link ObjectCreatorContractImpl}
     * depending on the given parameter
     *
     * @param beanType identifying the type to be tested. Must not be null
     * @param annotated the annotated unit-test-class. It is expected to be annotated with
     *            {@link VerifyConstructor} and / or {@link VerifyConstructors},
     *            {@link VerifyFactoryMethod} and / or {@link VerifyFactoryMethods} otherwise the
     *            method will return empty list
     * @param initialPropertyMetadata identifying the complete set of {@link PropertyMetadata},
     *            where the actual {@link PropertyMetadata} for the test will be filtered by
     *            using the attributes defined within {@link VerifyConstructor} and / or
     *            {@link VerifyFactoryMethod}. Must not be null.
     * @return a {@link List} of instances of {@link ObjectCreatorContractImpl} in case all
     *         requirements for the parameters are correct, otherwise it will return an empty list
     */
    public static final <T> List<ObjectCreatorContractImpl<T>> createTestContracts(
            final Class<T> beanType, final Class<?> annotated,
            final List<PropertyMetadata> initialPropertyMetadata) {

        requireNonNull(beanType, "beantype must not be null");
        requireNonNull(initialPropertyMetadata, "initialPropertyMetadata must not be null");

        final var builder = new CollectionBuilder<ObjectCreatorContractImpl<T>>();
        // VerifyConstructor
        for (final VerifyConstructor contract : AnnotationHelper
                .extractConfiguredConstructorContracts(annotated)) {
            final var properties =
                AnnotationHelper.constructorConfigToPropertyMetadata(contract,
                        initialPropertyMetadata);
            final ParameterizedInstantiator<T> instantiator = new ConstructorBasedInstantiator<>(
                    beanType, new RuntimeProperties(properties));
            builder.add(new ObjectCreatorContractImpl<>(instantiator));
        }
        // Verify Factory method
        for (final VerifyFactoryMethod contract : AnnotationHelper
                .extractConfiguredFactoryContracts(annotated)) {
            final var properties =
                AnnotationHelper.factoryConfigToPropertyMetadata(contract,
                        initialPropertyMetadata);
            Class<?> enclosingType = beanType;
            if (!VerifyFactoryMethod.class.equals(contract.enclosingType())) {
                enclosingType = contract.enclosingType();
            }
            final ParameterizedInstantiator<T> instantiator =
                new FactoryBasedInstantiator<>(beanType, new RuntimeProperties(properties),
                        enclosingType, contract.factoryMethodName());
            builder.add(new ObjectCreatorContractImpl<>(instantiator));
        }
        return builder.toImmutableList();
    }

}
