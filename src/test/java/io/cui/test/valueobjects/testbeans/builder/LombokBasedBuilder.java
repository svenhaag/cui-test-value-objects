package io.cui.test.valueobjects.testbeans.builder;

import static io.cui.test.valueobjects.generator.JavaTypesGenerator.STRINGS_LETTER;
import static io.cui.tools.collect.CollectionLiterals.immutableSortedSet;

import java.io.Serializable;
import java.util.SortedSet;

import io.cui.test.valueobjects.property.PropertyMetadata;
import io.cui.test.valueobjects.property.impl.BuilderMetadata;
import io.cui.test.valueobjects.property.util.CollectionType;
import io.cui.test.valueobjects.property.util.PropertyAccessStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;

@SuppressWarnings("javadoc")
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder
public class LombokBasedBuilder implements Serializable {

    private static final long serialVersionUID = 5021276874059936648L;

    public static final SortedSet<PropertyMetadata> METADATA_COMPLETE =
        immutableSortedSet(
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("name").required(true)
                                .propertyAccessStrategy(PropertyAccessStrategy.BUILDER_DIRECT)
                                .build())
                        .build(),
                BuilderMetadata.builder().delegateMetadata(
                        STRINGS_LETTER.metadataBuilder("elements").required(false)
                                .collectionType(CollectionType.SORTED_SET)
                                .propertyAccessStrategy(PropertyAccessStrategy.BUILDER_COLLECTION_AND_SINGLE_ELEMENT)
                                .build())
                        .builderSingleAddMethodName("element")
                        .build());
    @Getter
    @NonNull
    private final String name;

    @Singular
    @Getter
    private final SortedSet<String> elements;

    public LombokBasedBuilder(LombokBasedBuilder copy) {
        name = copy.getName();
        elements = copy.getElements();
    }

}
