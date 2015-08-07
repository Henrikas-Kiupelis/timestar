package com.superum.helper.math;

/**
 * <pre>
 * Efficient calculation of power of numbers
 *
 * Be careful of overflows when using large numbers! The result should not exceed Long.MAX_VALUE
 * </pre>
 */
public final class Power {

    /**
     * @return number ^ power; i.e. 2 ^ 4 = 2 * 2 * 2 * 2 = 16
     */
    public static long of(long number, long power) {
        if (power < 0)
            throw new IllegalArgumentException("Only powers >= 0 allowed.");

        if (power == 0)
            return 1;

        return power(number, power);
    }

    // PRIVATE

    /**
     * <pre>
     * This method uses an efficient exponentiation algorithm:
     * Given a ^ b
     * if b is even, then a ^ b = a ^ (b/2) * a ^ (b/2)
     * if b is odd, then a ^ b = a ^ (b/2) * a ^ (b/2) * a
     *
     * testing for even/odd is achieved by (b & 1), which leaves only the 2^0 bit
     *
     * b/2 can be achieved with bit shift operation b >> 1
     * </pre>
     * @return number ^ power
     */
    private static long power(long number, long exponent) {
        if (exponent == 1)
            return number;

        long squaredOut = power(number * number, exponent >> 1);

        return (exponent & 1) == 0
                ? squaredOut
                : number * squaredOut;
    }

}
