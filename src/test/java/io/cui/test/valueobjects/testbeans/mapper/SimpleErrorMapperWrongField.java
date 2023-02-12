package io.cui.test.valueobjects.testbeans.mapper;

import static io.cui.tools.collect.CollectionLiterals.mutableList;

import java.util.function.Function;

@SuppressWarnings("javadoc")
public class SimpleErrorMapperWrongField implements Function<SimpleSourceBean, SimpleTargetBean> {

    @Override
    public SimpleTargetBean apply(SimpleSourceBean t) {
        var target = new SimpleTargetBean();
        target.setNameFirst(t.getLastname());
        target.setNameFirst(t.getFirstname());
        target.setListOfAttributes(mutableList(t.getFirstname()));
        return target;
    }

}
