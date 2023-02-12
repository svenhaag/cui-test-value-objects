package io.cui.test.valueobjects.testbeans.generator;

import io.cui.test.generator.internal.net.java.quickcheck.Generator;
import io.cui.test.generator.internal.net.java.quickcheck.generator.PrimitiveGenerators;

@SuppressWarnings("javadoc")
public class QuickCheckNumberGenerator implements Generator<Number> {

    @Override
    public Number next() {
        return PrimitiveGenerators.integers().next();
    }

}
