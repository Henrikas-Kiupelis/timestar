package com.superum.helper.field.core;

import com.google.common.collect.ImmutableList;
import com.superum.helper.SpecialUtils;
import org.jooq.Field;

import java.math.BigDecimal;
import java.util.List;

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

    public static <F> SimpleMappedField<F> empty(String name, Field<F> field, Mandatory mandatory, Primary primary) {
        return new SimpleMappedField<>(name, field, mandatory, primary, null);
    }

    public static <F> SimpleMappedField<F> valueOf(String name, Field<F> field, Mandatory mandatory, Primary primary, F value) {
        if (value == null)
            return empty(name, field, mandatory, primary);

        return new SimpleMappedField<>(name, field, mandatory, primary, value);
    }

    public static SimpleMappedField<Integer> valueOf(String name, Field<Integer> field, Mandatory mandatory, Primary primary, int value) {
        return new IntField(name, field, mandatory, primary, value);
    }

    public static SimpleMappedField<Long> valueOf(String name, Field<Long> field, Mandatory mandatory, Primary primary, long value) {
        return new LongField(name, field, mandatory, primary, value);
    }

    public static SimpleMappedField<BigDecimal> valueOf(String name, Field<BigDecimal> field, Mandatory mandatory, Primary primary, BigDecimal value) {
        if (value == null)
            return empty(name, field, mandatory, primary);

        return new BigDecimalField(name, field, mandatory, primary, value);
    }

    /**
     * <pre>
     * This version ensures that the list inside the field will remain immutable (as long as the original list
     * is not messed with)
     * </pre>
     */
    public static <F> SimpleMappedField<List<F>> valueOf(String name, Field<?> field, Mandatory mandatory, Primary primary, List<F> value) {
        if (value == null)
            return empty(name, null, mandatory, primary);

        return new SimpleMappedField<>(name, null, mandatory, primary, ImmutableList.copyOf(value));
    }

    private SimpleMappedField(String name, Field<F> field, Mandatory mandatory, Primary primary, F value) {
        super(name, field, mandatory, primary);
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
            return value == null ? 0 : Double.valueOf(value.doubleValue()).hashCode();
        }

        // CONSTRUCTORS

        private BigDecimalField(String fieldName, Field<BigDecimal> field, Mandatory mandatory, Primary primary, BigDecimal value) {
            super(fieldName, field, mandatory, primary, value);
        }

    }

    /**
     * <pre>
     * Specialized MappedField, intended to be used with int
     *
     * This field will check if a field is set by comparing it to the default primitive value, which is 0
     * </pre>
     */
    private static final class IntField extends SimpleMappedField<Integer> {
        @Override
        public boolean isSet() {
            return value != 0;
        }

        // CONSTRUCTORS

        private IntField(String name, Field<Integer> field, Mandatory mandatory, Primary primary, int value) {
            super(name, field, mandatory, primary, value);
        }
    }

    /**
     * <pre>
     * Specialized MappedField, intended to be used with long
     *
     * This field will check if a field is set by comparing it to the default primitive value, which is 0
     * </pre>
     */
    private static final class LongField extends SimpleMappedField<Long> {
        @Override
        public boolean isSet() {
            return value != 0;
        }

        // CONSTRUCTORS

        private LongField(String name, Field<Long> field, Mandatory mandatory, Primary primary, long value) {
            super(name, field, mandatory, primary, value);
        }
    }

}
