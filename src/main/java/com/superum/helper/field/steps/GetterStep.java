package com.superum.helper.field.steps;

import java.util.function.Function;

public interface GetterStep<T, F> {

    FieldDef<T, F> getter(Function<T, F> getter);
    <U> FieldDef<T, F> getter(Function<T, U> getter, Function<U, F> converter);

}
