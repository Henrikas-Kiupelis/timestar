package com.superum.helper.field.core;

import eu.goodlike.misc.SpecialUtils;
import org.jooq.Field;

import java.math.BigDecimal;

/**
 * AbstractMappedField implementation which handles the value of the field
 * @param <F> type of the value in this field
 */
public class SimpleMappedField<F> extends AbstractMappedField<F> {

    @Override
    public F getValue() {
        return value;
    }

    @Override
    public boolean isSet() {
        return value != null;
    }

    // CONSTRUCTORS

    public static <F> SimpleMappedField<F> valueOf(String name, Field<F> field, boolean isMandatory, boolean isPrimary, F value) {
        if (value == null)
            return empty(name, field, isMandatory, isPrimary);

        return new SimpleMappedField<>(name, field, isMandatory, isPrimary, value);
    }

    public static SimpleMappedField<BigDecimal> valueOf(String name, Field<BigDecimal> field, boolean isMandatory, boolean isPrimary, BigDecimal value) {
        if (value == null)
            return empty(name, field, isMandatory, isPrimary);

        return new BigDecimalField(name, field, isMandatory, isPrimary, value);
    }

    public static <F> SimpleMappedField<F> empty(String name, Field<F> field, boolean isMandatory, boolean isPrimary) {
        return new SimpleMappedField<>(name, field, isMandatory, isPrimary, null);
    }

    private SimpleMappedField(String name, Field<F> field, boolean isMandatory, boolean isPrimary, F value) {
        super(name, field, isMandatory, isPrimary);
        this.value = value;
    }

    // PROTECTED

    protected final F value;

    // EXTENDING CLASSES FOR SPECIAL CASES

    /**
     * <pre>
     * Specialized MappedField, intended to be used with java.math.BigDecimal
     *
     * This field will use compareTo() method to check for equality of the field, which will ignore things such as scale
     * </pre>
     */
    private static final class BigDecimalField extends SimpleMappedField<BigDecimal> {

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof BigDecimalField))
                return false;

            BigDecimalField other = (BigDecimalField) o;

            return SpecialUtils.equalsJavaMathBigDecimal(this.value, other.value);
        }

        @Override
        public int hashCode() {
            return SpecialUtils.bigDecimalCustomHashCode(value);
        }

        // CONSTRUCTORS

        private BigDecimalField(String fieldName, Field<BigDecimal> field, boolean isMandatory, boolean isPrimary, BigDecimal value) {
            super(fieldName, field, isMandatory, isPrimary, value);
        }

    }

}
