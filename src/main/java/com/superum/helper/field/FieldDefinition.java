package com.superum.helper.field;

import com.superum.helper.NullChecker;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.core.SimpleMappedField;
import com.superum.helper.field.steps.FieldDef;
import com.superum.helper.field.steps.FieldNameStep;
import com.superum.helper.field.steps.GetterStep;
import com.superum.helper.field.steps.TableFieldStep;
import org.jooq.Field;
import org.jooq.TableField;

import java.math.BigDecimal;
import java.util.function.Function;

import static com.superum.helper.validation.Validator.validate;

/**
 * Builder class to define how to create a MappedField; uses step builder pattern
 * @param <T> the class containing the field being defined
 * @param <F> the type of the field inside the database (i.e. java.sql.Date for DATE); class can contain any field, so
 *           long as it can be converted
 */
public class FieldDefinition<T, F> implements FieldNameStep<T, F>, TableFieldStep<T, F>, GetterStep<T, F>, FieldDef<T, F> {

    @Override
    public MappedField<F> toField(T t) {
        F value = getter.apply(t);

        if (value instanceof BigDecimal) {
            BigDecimal valueBig = (BigDecimal) value;
            @SuppressWarnings("unchecked")
            Field<BigDecimal> field = (Field<BigDecimal>) this.field;
            @SuppressWarnings("unchecked")
            MappedField<F> mappedField = (MappedField<F>) SimpleMappedField.valueOf(name, field, isMandatory, isPrimary, valueBig);
            return mappedField;
        }

        return SimpleMappedField.valueOf(name, field, isMandatory, isPrimary, value);
    }

    @Override
    public TableFieldStep<T, F> fieldName(String name) {
        validate(name).not().Null().not().blank()
                .ifInvalid(() -> new IllegalArgumentException("Defined field must have a valid name, not: " + name));

        this.name = name;
        return this;
    }

    @Override
    public GetterStep<T, F> tableField(TableField<?, F> tableField) {
        if (tableField == null)
            throw new IllegalArgumentException("Defined field can't have null tableField");

        this.field = tableField;
        return this;
    }

    @Override
    public FieldDef<T, F> getter(Function<T, F> getter) {
        if (getter == null)
            throw new IllegalArgumentException("Defined field can't have null getter");

        this.getter = getter;
        return this;
    }

    @Override
    public <U> FieldDef<T, F> getter(Function<T, U> getter, Function<U, F> converter) {
        NullChecker.check(getter, converter).notNull("Defined field can't have null getter or converter");

        this.getter = getter.andThen(u -> u == null ? null : converter.apply(u));
        return this;
    }

    @Override
    public FieldDef<T, F> primaryKey() {
        isPrimary = true;
        return this;
    }

    @Override
    public FieldDef<T, F> mandatory() {
        isMandatory = true;
        return this;
    }

    // CONSTRUCTORS

    public static <T, F> FieldNameStep<T, F> steps(Class<T> containerClass, Class<F> fieldClass) {
        return new FieldDefinition<>();
    }

    private FieldDefinition() {}

    // PRIVATE

    private String name;
    private Field<F> field;
    private Function<T, F> getter;
    private boolean isMandatory = false;
    private boolean isPrimary = false;

}
