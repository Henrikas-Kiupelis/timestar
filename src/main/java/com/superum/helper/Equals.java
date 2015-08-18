package com.superum.helper;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <pre>
 * Tests for equality by using getter functions, i.e. Teacher:;getId
 * The getters themselves don't need to be public, or simple - so long as they retrieve information
 * about the object, that would be used in equals(Object) method, they are good enough
 * </pre>
 * @param <T> type of Object whose getters are going to be used
 */
public final class Equals<T> {

    /**
     * This method checks Objects.equals() regardless of type
     * @return true if all provided getters return equal objects for both t1 and t2, false otherwise
     */
    public boolean equalsPure(T t1, T t2) {
        return equals(t1, t2, tuple2 -> tuple2.map(Objects::equals));
    }

    /**
     * This method checks Objects.equals() for all objects, except BigDecimals, which check
     * SpecialUtils.equalsJavaMathBigDecimal()
     * @return true if all provided getters return equal objects for both t1 and t2, false otherwise
     */
    public boolean equals(T t1, T t2) {
        return equals(t1, t2, tuple2 -> tuple2.map(Equals::equalsAdjusted));
    }

    /**
     * This method uses a provided equality predicate to test if two objects are equal
     * @return true if all provided getters return equal objects for both t1 and t2, false otherwise
     */
    public boolean equals(T t1, T t2, Predicate<Tuple2<Object, Object>> equalityFunction) {
        return !Seq.seq(getters)
                .map(getter -> Tuple.tuple(getter.apply(t1), getter.apply(t2)))
                .filter(equalityFunction.negate())
                .findAny().isPresent();
    }

    /**
     * WARNING! BigDecimals/arrays can produce inconsistent results for this method
     * @return true if all provided getters return equal objects for every parameter, false otherwise
     */
    @SafeVarargs
    public final boolean equals(T... objects) {
        return !Seq.seq(getters)
                .map(getter -> Stream.of(objects).map(getter::apply).distinct())
                .filter(stream -> stream.count() > 1)
                .findAny().isPresent();
    }

    // CONSTRUCTORS

    /**
     * @deprecated avoid using this to avoid compiler warnings
     */
    @SafeVarargs
    @Deprecated
    public Equals(Function<T, ?>... getters) {
        this(Arrays.asList(getters));
    }

    public Equals(List<Function<T, ?>> getters) {
        this.getters = getters;
    }

    // PRIVATE

    private final List<Function<T, ?>> getters;

    /**
     * Ensures that BigDecimals are compared ignoring their scale
     */
    private static boolean equalsAdjusted(Object o1, Object o2) {
        return o1 instanceof BigDecimal && o2 instanceof BigDecimal
                ? SpecialUtils.equalsJavaMathBigDecimal((BigDecimal)o1, (BigDecimal)o2)
                : Objects.equals(o1, o2);
    }

}
