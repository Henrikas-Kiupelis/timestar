package com.superum.helper.fields.core;

import com.google.common.collect.ImmutableList;
import com.superum.helper.SpecialUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * NamedField intended for use with non-primitives
 * </pre>
 */
public class SimpleField<T> extends NamedField<T> {

    @Override
    public boolean isSet() {
        return value != null;
    }

    @Override
    public T getValue() {
        return value;
    }

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof SimpleField))
            return false;

        SimpleField other = (SimpleField) o;

        return this.equalsName(other) && Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, value);
    }

    // CONSTRUCTORS

    public static <T> SimpleField<T> empty(String fieldName, Mandatory mandatory) {
        return new SimpleField<>(fieldName, null, mandatory);
    }

    public static SimpleField<BigDecimal> valueOf(String fieldName, BigDecimal value, Mandatory mandatory) {
        if (value == null)
            return empty(fieldName, mandatory);

        return new BigDecimalField(fieldName, value, mandatory);
    }

    /**
     * @throws UnsupportedOperationException - java.util.Date/java.sql.Date fields not allowed
     * @deprecated Please use org.joda.time.Instant/org.joda.time.LocalDate instead!
     */
    @Deprecated
    public static SimpleField<Date> valueOf(String fieldName, Date value, Mandatory mandatory) throws ParseException {
        throw new UnsupportedOperationException("java.util.Date/java.sql.Date fields not allowed," +
                " please use org.joda.time.Instant/org.joda.time.LocalDate instead!");
    }

    public static <T> SimpleField<T> valueOf(String fieldName, T value, Mandatory mandatory) {
        if (value == null)
            return empty(fieldName, mandatory);

        return new SimpleField<>(fieldName, value, mandatory);
    }

    /**
     * <pre>
     * This version ensures that the list inside the field will remain immutable (as long as the original list
     * is not messed with)
     * </pre>
     */
    public static <T> SimpleField<List<T>> immutableValueOf(String fieldName, List<T> value, Mandatory mandatory) {
        if (value == null)
            return empty(fieldName, mandatory);

        return new SimpleField<>(fieldName, ImmutableList.copyOf(value), mandatory);
    }

    private SimpleField(String fieldName, T value, Mandatory mandatory) {
        super(fieldName, mandatory);
        this.value = value;
    }

    // PROTECTED

    protected final T value;

    // EXTENDING CLASSES FOR SPECIAL CASES

    /**
     * <pre>
     * Specialized NamedField, intended to be used with java.math.BigDecimal
     *
     * This Field will use compareTo() method to check for equality of the Fields, which will ignore things such as scale
     * </pre>
     */
    private static final class BigDecimalField extends SimpleField<BigDecimal> {

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

        private BigDecimalField(String fieldName, BigDecimal value, Mandatory mandatory) {
            super(fieldName, value, mandatory);
        }

    }

}
