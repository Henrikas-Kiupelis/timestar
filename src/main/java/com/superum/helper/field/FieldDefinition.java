package com.superum.helper.field;

import com.superum.helper.NullChecker;
import com.superum.helper.field.core.Mandatory;
import com.superum.helper.field.core.MappedField;
import com.superum.helper.field.core.Primary;
import com.superum.helper.field.core.SimpleMappedField;
import com.superum.helper.time.JodaTimeZoneHandler;
import org.joda.time.LocalDate;
import org.jooq.Field;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * <pre>
 * Defines how a certain field properties can be used to turn it into a mapped field given an object
 *
 * These definitions serve two purposes:
 * 1) Statically define field's properties, such as:
 *      a) name;
 *      b) what database field it maps to;
 *      c) is it mandatory for some operation;
 *      d) is it the primary key;
 *      e) how to extract the value of this field for a given object;
 * 2) Dynamically create mapped fields, which can be used in a plethora of situations:
 *      a) using all fields of a class as a collection/stream/sequence;
 *      b) checking and filtering based around subsets of all the fields;
 *      c) automatically create operations such as sql queries for JOOQ;
 *
 * Only relevant fields should be defined, i.e. fields that are used in SQL write operations, equality comparisons
 * and so on
 * </pre>
 * @param <T> type of the object this field definition belongs to
 * @param <F> type of the field this definition is defining
 */
public final class FieldDefinition<T, F> implements FieldDef<T, F> {

    public MappedField<F> toField(T t) {
        NullChecker.check(t).notNull("Body cannot be null");

        return SimpleMappedField.valueOf(name, field, mandatory, primary, getter.apply(t));
    }

    // CONSTRUCTORS

    public static <T, F> FieldDef<T, F> of(String name, Field<F> field, Mandatory mandatory, Primary primary, Function<T, F> getter) {
        NullChecker.check(name, mandatory, getter).notNull("Name, mandatory and getter function cannot be null for FieldDefinitions");

        return new FieldDefinition<>(name, field, mandatory, primary, getter);
    }

    public static <T> FieldDef<T, BigDecimal> ofBigDecimal(String name, Field<BigDecimal> field, Mandatory mandatory, Primary primary, Function<T, BigDecimal> getter) {
        NullChecker.check(name, mandatory, getter).notNull("Name, mandatory and getter function cannot be null for FieldDefinitions");

        return new BigDecimalFieldDefinition<>(name, field, mandatory, primary, getter);
    }

    public static <T> FieldDef<T, Integer> ofInt(String name, Field<Integer> field, Mandatory mandatory, Primary primary, ToIntFunction<T> getter) {
        NullChecker.check(name, mandatory, getter).notNull("Name, mandatory and getter function cannot be null for FieldDefinitions");

        return new IntFieldDefinition<>(name, field, mandatory, primary, getter);
    }

    public static <T> FieldDef<T, Long> ofLong(String name, Field<Long> field, Mandatory mandatory, Primary primary, ToLongFunction<T> getter) {
        NullChecker.check(name, mandatory, getter).notNull("Name, mandatory and getter function cannot be null for FieldDefinitions");

        return new LongFieldDefinition<>(name, field, mandatory, primary, getter);
    }

    public static <T> FieldDef<T, java.sql.Date> ofDate(String name, Field<java.sql.Date> field, Mandatory mandatory, Primary primary, Function<T, LocalDate> getter) {
        NullChecker.check(name, mandatory, getter).notNull("Name, mandatory and getter function cannot be null for FieldDefinitions");

        return new JavaSqlDateDefinition<>(name, field, mandatory, primary, getter);
    }

    private FieldDefinition(String name, Field<F> field, Mandatory mandatory, Primary primary, Function<T, F> getter) {
        this.name = name;
        this.field = field;
        this.mandatory = mandatory;
        this.primary = primary;
        this.getter = getter;
    }

    // PRIVATE

    private final String name;
    private final Field<F> field;
    private final Mandatory mandatory;
    private final Primary primary;
    private final Function<T, F> getter;

    /**
     * <pre>
     * This class is required explicitly due to generics erasure
     *
     * SimpleMappedField.valueOf() can't resolve BigDecimal version dynamically (JAVA uses static resolving
     * techniques), which results in a SimpleMappedField rather than BigDecimalField, which has the custom
     * equality logic that we need
     * </pre>
     */
    private static final class BigDecimalFieldDefinition<T> implements FieldDef<T, BigDecimal> {

        @Override
        public MappedField<BigDecimal> toField(T t) {
            NullChecker.check(t).notNull("Body cannot be null");

            return SimpleMappedField.valueOf(name, field, mandatory, primary, getter.apply(t));
        }

        // CONSTRUCTORS

        private BigDecimalFieldDefinition(String name, Field<BigDecimal> field, Mandatory mandatory, Primary primary, Function<T, BigDecimal> getter) {
            this.name = name;
            this.field = field;
            this.mandatory = mandatory;
            this.primary = primary;
            this.getter = getter;
        }

        // PRIVATE

        private final String name;
        private final Field<BigDecimal> field;
        private final Mandatory mandatory;
        private final Primary primary;
        private final Function<T, BigDecimal> getter;
    }

    private static final class IntFieldDefinition<T> implements FieldDef<T, Integer> {

        @Override
        public MappedField<Integer> toField(T t) {
            NullChecker.check(t).notNull("Body cannot be null");

            return SimpleMappedField.valueOf(name, field, mandatory, primary, getter.applyAsInt(t));
        }

        // CONSTRUCTORS

        private IntFieldDefinition(String name, Field<Integer> field, Mandatory mandatory, Primary primary, ToIntFunction<T> getter) {
            this.name = name;
            this.field = field;
            this.mandatory = mandatory;
            this.primary = primary;
            this.getter = getter;
        }

        // PRIVATE

        private final String name;
        private final Field<Integer> field;
        private final Mandatory mandatory;
        private final Primary primary;
        private final ToIntFunction<T> getter;

    }

    private static final class LongFieldDefinition<T> implements FieldDef<T, Long> {

        @Override
        public MappedField<Long> toField(T t) {
            NullChecker.check(t).notNull("Body cannot be null");

            return SimpleMappedField.valueOf(name, field, mandatory, primary, getter.applyAsLong(t));
        }

        // CONSTRUCTORS

        private LongFieldDefinition(String name, Field<Long> field, Mandatory mandatory, Primary primary, ToLongFunction<T> getter) {
            this.name = name;
            this.field = field;
            this.mandatory = mandatory;
            this.primary = primary;
            this.getter = getter;
        }

        // PRIVATE

        private final String name;
        private final Field<Long> field;
        private final Mandatory mandatory;
        private final Primary primary;
        private final ToLongFunction<T> getter;

    }

    private static final class JavaSqlDateDefinition<T> implements FieldDef<T, java.sql.Date> {

        @Override
        public MappedField<Date> toField(T t) {
            NullChecker.check(t).notNull("Body cannot be null");

            LocalDate localDate = getter.apply(t);
            if (localDate == null)
                return SimpleMappedField.empty(name, field, mandatory, primary);

            java.sql.Date sqlDate = JodaTimeZoneHandler.getDefault().from(localDate).toJavaSqlDate();
            return SimpleMappedField.valueOf(name, field, mandatory, primary, sqlDate);
        }

        // CONSTRUCTORS

        private JavaSqlDateDefinition(String name, Field<java.sql.Date> field, Mandatory mandatory, Primary primary, Function<T, LocalDate> getter) {
            this.name = name;
            this.field = field;
            this.mandatory = mandatory;
            this.primary = primary;
            this.getter = getter;

        }

        // PRIVATE

        private final String name;
        private final Field<java.sql.Date> field;
        private final Mandatory mandatory;
        private final Primary primary;
        private final Function<T, LocalDate> getter;

    }

}
