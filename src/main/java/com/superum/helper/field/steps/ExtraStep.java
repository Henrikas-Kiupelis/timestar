package com.superum.helper.field.steps;

public interface ExtraStep<T, F> {

    FieldDef<T, F> primaryKey();
    FieldDef<T, F> mandatory();

}
