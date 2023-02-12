package io.cui.test.valueobjects.contract;

import static io.cui.test.valueobjects.contract.CopyConstructorContractImpl.createTestContract;
import static io.cui.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.api.TestContract;
import io.cui.test.valueobjects.generator.TypedGeneratorRegistry;
import io.cui.test.valueobjects.testbeans.copyconstructor.BadCopyConstructor;
import io.cui.test.valueobjects.testbeans.copyconstructor.BadDeepCopyCopyConstructor;
import io.cui.test.valueobjects.testbeans.copyconstructor.DeepCopyCopyConstructor;
import io.cui.test.valueobjects.testbeans.copyconstructor.InterfaceCopyConstructor;
import io.cui.test.valueobjects.testbeans.copyconstructor.MockWithReadOnly;
import io.cui.test.valueobjects.testbeans.copyconstructor.OneRequiredFieldCopyConstructor;

class CopyConstructorContractImplTest {

    @AfterEach
    void after() {
        TypedGeneratorRegistry.clear();
    }

    @BeforeEach
    void before() {
        TypedGeneratorRegistry.registerBasicTypes();
    }

    private static final List<TestContract<OneRequiredFieldCopyConstructor>> MOCK_CONTRACT =
        immutableList(OneRequiredFieldCopyConstructor.MOCK_INSTANTIATOR_CONTRACT);

    @Test
    void shouldDetermineContractCorrectly() {
        assertFalse(createTestContract(OneRequiredFieldCopyConstructor.class, CopyConstructorContractImplTest.class,
                OneRequiredFieldCopyConstructor.ATTRIBUTE_LIST, MOCK_CONTRACT).isPresent());
        assertTrue(createTestContract(OneRequiredFieldCopyConstructor.class, OneRequiredFieldCopyConstructor.class,
                OneRequiredFieldCopyConstructor.ATTRIBUTE_LIST, MOCK_CONTRACT).isPresent());
        assertTrue(createTestContract(InterfaceCopyConstructor.class, InterfaceCopyConstructor.class,
                InterfaceCopyConstructor.ATTRIBUTE_LIST,
                immutableList(InterfaceCopyConstructor.MOCK_INSTANTIATOR_CONTRACT)).isPresent());
        assertTrue(createTestContract(BadCopyConstructor.class, BadCopyConstructor.class,
                BadCopyConstructor.ATTRIBUTE_LIST, immutableList(BadCopyConstructor.MOCK_INSTANTIATOR_CONTRACT))
                        .isPresent());
        assertTrue(createTestContract(DeepCopyCopyConstructor.class, DeepCopyCopyConstructor.class,
                DeepCopyCopyConstructor.ATTRIBUTE_LIST,
                immutableList(DeepCopyCopyConstructor.MOCK_INSTANTIATOR_CONTRACT)).isPresent());
    }

    @Test
    @SuppressWarnings("java:S5778") // owolff Collections.emptyList() considered unproblematic
    void shouldFailToDetermineContractOnEmptyIntantiatorList() {
        assertThrows(AssertionError.class, () -> {
            createTestContract(OneRequiredFieldCopyConstructor.class, OneRequiredFieldCopyConstructor.class,
                    OneRequiredFieldCopyConstructor.ATTRIBUTE_LIST, Collections.emptyList());
        });
    }

    @Test
    void shouldAssertCorrectConstructorCorrectly() {
        createTestContract(OneRequiredFieldCopyConstructor.class, OneRequiredFieldCopyConstructor.class,
                OneRequiredFieldCopyConstructor.ATTRIBUTE_LIST, MOCK_CONTRACT).get().assertContract();
    }

    @Test
    void shouldFailToAssertInvalidType() {
        var contract =
            createTestContract(BadCopyConstructor.class, BadCopyConstructor.class, BadCopyConstructor.ATTRIBUTE_LIST,
                    immutableList(BadCopyConstructor.MOCK_INSTANTIATOR_CONTRACT)).get();
        assertThrows(AssertionError.class, () -> contract.assertContract());
    }

    @Test
    void shouldAssertDeepCopy() {
        createTestContract(DeepCopyCopyConstructor.class, DeepCopyCopyConstructor.class,
                DeepCopyCopyConstructor.ATTRIBUTE_LIST,
                immutableList(DeepCopyCopyConstructor.MOCK_INSTANTIATOR_CONTRACT)).get().assertContract();
    }

    @Test
    void shouldDetectInvalidDeepCopy() {
        var contract =
            createTestContract(BadDeepCopyCopyConstructor.class, BadDeepCopyCopyConstructor.class,
                    BadDeepCopyCopyConstructor.ATTRIBUTE_LIST,
                    immutableList(BadDeepCopyCopyConstructor.MOCK_INSTANTIATOR_CONTRACT)).get();
        assertThrows(AssertionError.class, () -> contract.assertContract());
    }

    @Test
    void shouldHandleReadOnlyCopy() {
        var contract =
            createTestContract(MockWithReadOnly.class, MockWithReadOnly.class, MockWithReadOnly.ATTRIBUTE_LIST,
                    immutableList(MockWithReadOnly.MOCK_INSTANTIATOR_CONTRACT)).get();
        assertThrows(AssertionError.class, () -> contract.assertContract());
    }
}
