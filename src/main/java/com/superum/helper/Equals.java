package com.superum.helper;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
     * @return true if all provided getters return equal objects for both t1 and t2, false otherwise
     */
    public boolean equals(T t1, T t2) {
        return !Seq.seq(getters)
                .map(getter -> Tuple.tuple(getter.apply(t1), getter.apply(t2)))
                .filter(tuple2 -> !tuple2.map(Equals::equalsAdjusted))
                .findAny().isPresent();
    }

    /**
     * WARNING! BigDecimals can produce inconsistent results for this method
     * @return true if all provided getters return equal objects for every parameter, false otherwise
     */
    @SafeVarargs
    public final boolean equals(T... objects) {
        return !Seq.seq(getters)
                .map(getter -> Arrays.stream(objects).map(getter::apply).distinct())
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

    private static boolean equalsAdjusted(Object o1, Object o2) {
        return o1 instanceof BigDecimal && o2 instanceof BigDecimal
                ? SpecialUtils.equalsJavaMathBigDecimal((BigDecimal)o1, (BigDecimal)o2)
                : Objects.equals(o1, o2);
    }

}
