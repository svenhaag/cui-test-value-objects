package io.cui.test.valueobjects.util;

import io.cui.test.generator.TypedGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Hard-core implementation for reflection based corner-cases.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
class WildcardDecoratorGenerator implements TypedGenerator {

    @Getter
    private final Class<?> type;
    private final TypedGenerator<?> generator;

    @Override
    public Object next() {
        return generator.next();
    }
}
