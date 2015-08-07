package com.superum.helper;

import com.superum.helper.math.Power;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * SecureRandom wrapper, which provides some extra methods, i.e. String/char/etc
 */
public final class Random {

    /**
     * @return random number with a certain amount of digits; i.e. numberWithDigits(6) returns a number from 100000 to 999999
     */
    public static int numberWithDigits(int digitCount) {
        if (digitCount < 1)
            throw new IllegalArgumentException("Amount of digits must be positive");

        if (digitCount > 9)
            throw new IllegalArgumentException("Cannot fit all " + digitCount + " digits numbers in an integer, largest is " + Integer.MAX_VALUE);

        int highBound = (int)Power.of(10, digitCount);
        return numberChecked(highBound / 10, highBound);
    }

    /**
     * @return random number from lowBound to highBound; i.e. number(10, 100) returns a number from 10 to 99
     */
    public static int number(int lowBound, int highBound) {
        if (lowBound < 1 || highBound < 1)
            throw new IllegalArgumentException("Bounds must be positive");

        if (lowBound >= highBound)
            throw new IllegalArgumentException("Low bound cannot be larger or equal to the high bound");

        return numberChecked(lowBound, highBound);
    }

    /**
     * @return random number from 0 to limit-1; i.e. number(10) returns a number from 0 to 9
     */
    public static int number(int limit) {
        if (limit < 1)
            throw new IllegalArgumentException("Limit must be positive");

        return numberChecked(limit);
    }

    /**
     * @return random char from '0' to '9'
     */
    public static char digitChar() {
        return fromChecked(DIGITS);
    }

    /**
     * @return random char from 'a' to 'z'; uses english
     */
    public static char lowCaseChar() {
        return fromChecked(LETTERS);
    }

    /**
     * @return random char from 'A' to 'Z'; uses english
     */
    public static char upCaseChar() {
        return fromChecked(CAPS);
    }

    /**
     * @return random char from a given string
     */
    public static char from(String string) {
        if (string == null || string.isEmpty())
            throw new IllegalArgumentException("Empty or null string nor allowed");

        return fromChecked(string);
    }

    /**
     * @return random char digit, letter or capital letter, depending on choice
     * @throws IllegalArgumentException if all 3 parameters are false
     */
    public static char customChar(boolean useDigits, boolean useLetters, boolean useCaps) {
        return fromChecked(customized(useDigits, useLetters, useCaps));
    }

    /**
     * @return random string from chars of a given string; length of returned string is equal to amount
     */
    public static String from(String string, int amount) {
        if (string == null || string.isEmpty())
            throw new IllegalArgumentException("Empty or null string nor allowed");

        return fromChecked(string, amount);
    }

    /**
     * @return random string made of digits, letters or capital letters, depending on choice;
     *          length of returned string is equal to amount
     * @throws IllegalArgumentException if all 3 parameters are false
     */
    public static String customString(boolean useDigits, boolean useLetters, boolean useCaps, int amount) {
        return fromChecked(customized(useDigits, useLetters, useCaps), amount);
    }

    /**
     * @return random password made of digits, letters or capital letters, depending on choice;
     *          length of password is equal to amount
     */
    public static char[] password(boolean useDigits, boolean useLetters, boolean useCaps, int length) {
        char[] password = new char[length];
        for (int i = 0; i < length; i++)
            password[i] = customChar(useDigits, useLetters, useCaps);

        return password;
    }

    // PRIVATE

    private Random() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String DIGITS = makeDigits();
    private static final String LETTERS = makeLetters();
    private static final String CAPS = makeCaps();

    /**
     * Private version of number(int, int) method, which assumes the bound check has already been done
     */
    private static int numberChecked(int lowBound, int highBound) {
        return numberChecked(highBound - lowBound) + lowBound;
    }

    /**
     * Private version of number(int) method, which assumes the limit positivity check has already been done
     */
    private static int numberChecked(int limit) {
        return RANDOM.nextInt(limit);
    }

    /**
     * Private version of from(String) method, which assumes null/empty check has already been done
     */
    private static char fromChecked(String string) {
        return string.charAt(number(string.length()));
    }

    /**
     * Private version of from(String, int) method, which assumes null/empty check has already been done
     */
    private static String fromChecked(String string, int amount) {
        return IntStream.range(0, amount).mapToObj(i -> from(string)).map(String::valueOf).collect(Collectors.joining());
    }

    private static String customized(boolean useDigits, boolean useLetters, boolean useCaps) {
        if (!useDigits && !useLetters && !useCaps)
            throw new IllegalArgumentException("You must pick at least a single use case!");

        StringBuilder custom = new StringBuilder();
        if (useDigits) custom.append(DIGITS);
        if (useLetters) custom.append(LETTERS);
        if (useCaps) custom.append(CAPS);
        return custom.toString();
    }

    private static String makeDigits() {
        StringBuilder chars = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            chars.append(ch);
        return chars.toString();
    }

    private static String makeLetters() {
        StringBuilder chars = new StringBuilder();
        for (char ch = 'a'; ch <= 'z'; ++ch)
            chars.append(ch);
        return chars.toString();
    }

    private static String makeCaps() {
        StringBuilder chars = new StringBuilder();
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            chars.append(ch);
        return chars.toString();
    }

}
