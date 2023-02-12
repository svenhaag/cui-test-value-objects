package io.cui.test.valueobjects.contract;

import static io.cui.test.valueobjects.generator.JavaTypesGenerator.STRINGS;
import static io.cui.test.valueobjects.testbeans.ComplexBean.ATTRIBUTE_BAD_STRING;
import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static io.cui.tools.collect.CollectionLiterals.immutableSortedSet;
import static io.cui.tools.collect.CollectionLiterals.mutableSortedSet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.api.TestContract;
import io.cui.test.valueobjects.objects.RuntimeProperties;
import io.cui.test.valueobjects.objects.impl.BeanInstantiator;
import io.cui.test.valueobjects.objects.impl.DefaultInstantiator;
import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.testbeans.ComplexBean;
import io.cui.test.valueobjects.testbeans.beanproperty.BeanPropertyTestClassSimple;

class BeanPropertyContractImplTest {

    private static final SortedSet<PropertyMetadata> COMPLETE_METADATA =
        immutableSortedSet(ComplexBean.completeValidMetadata());

    private static final List<PropertyMetadata> COMPLETE_METADATA_AS_LIST = immutableList(COMPLETE_METADATA);

    private static final BeanInstantiator<ComplexBean> BEAN_INSTANTIATOR =
        new BeanInstantiator<>(new DefaultInstantiator<>(ComplexBean.class), new RuntimeProperties(COMPLETE_METADATA));

    @Test
    void shouldHandleComplexSetup() {
        final var support = new BeanPropertyContractImpl<>(BEAN_INSTANTIATOR);
        support.assertContract();
    }

    @Test
    void shouldFailOnInvalidBean() {
        final SortedSet<PropertyMetadata> generators = mutableSortedSet();
        generators.add(STRINGS.metadata(ATTRIBUTE_BAD_STRING));
        final var instantiator =
            new BeanInstantiator<>(new DefaultInstantiator<>(ComplexBean.class),
                    new RuntimeProperties(generators));
        final var support = new BeanPropertyContractImpl<>(instantiator);
        assertThrows(AssertionError.class, () -> support.assertContract());
    }

    @Test
    void factoryMethodShouldProvideContractOnValidParameter() {
        final Optional<TestContract<ComplexBean>> contract = BeanPropertyContractImpl.createBeanPropertyTestContract(
                ComplexBean.class, BeanPropertyTestClassSimple.class, COMPLETE_METADATA_AS_LIST);
        assertTrue(contract.isPresent());
        contract.get().assertContract();
        assertNotNull(contract.get().getInstantiator());
    }

    @Test
    void factoryMethodShouldNotProvideContractOnInvalidParameter() {
        Optional<TestContract<ComplexBean>> contract = BeanPropertyContractImpl
                .createBeanPropertyTestContract(ComplexBean.class, this.getClass(), COMPLETE_METADATA_AS_LIST);
        assertFalse(contract.isPresent());
        contract = BeanPropertyContractImpl.createBeanPropertyTestContract(ComplexBean.class,
                BeanPropertyTestClassSimple.class, immutableList());
        assertFalse(contract.isPresent());
    }
}
