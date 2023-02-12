package io.cui.test.valueobjects.mapper;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.cui.test.valueobjects.BaseMapperTest;
import io.cui.test.valueobjects.api.VerifyMapperConfiguration;
import io.cui.test.valueobjects.testbeans.mapper.SimpleErrorMapperWrongField;
import io.cui.test.valueobjects.testbeans.mapper.SimpleSourceBean;
import io.cui.test.valueobjects.testbeans.mapper.SimpleTargetBean;

@VerifyMapperConfiguration(equals = { "firstname:nameFirst", "lastname:nameLast", "attributeList:listOfAttributes" })
class BaseMapperTestErrorCollectionMapperTest
        extends BaseMapperTest<SimpleErrorMapperWrongField, SimpleSourceBean, SimpleTargetBean> {

    @Test
    void shouldAssertMapper() {
        assertThrows(AssertionError.class, () -> super.verifyMapper());
    }
}
