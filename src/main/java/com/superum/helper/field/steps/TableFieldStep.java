package com.superum.helper.field.steps;

import org.jooq.TableField;

public interface TableFieldStep<T, F> {

    GetterStep<T, F> tableField(TableField<?, F> tableField);

}
