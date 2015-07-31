/*
 * Copyright (c) Dovydas Sileika 2015.
 */

package com.superum.utils;

import java.util.*;

public class ArrayUtils {

    public static <T> T[] join(T[] first, T second) {
        T[] copy = Arrays.copyOf(first, first.length + 1);
        copy[first.length] = second;
        return copy;
    }

    @SafeVarargs
    public static <T> T[] join(T[] first, T... second) {
        int offset = first.length;
        int newLength = offset + second.length;
        T[] copy = Arrays.copyOf(first, newLength);
        System.arraycopy(second, 0, copy, offset, newLength - offset);
        return copy;
    }

    public static <T> T[] fromListOfArrays(List<T[]> listOfArrays, Class<T[]> clazz) {
        List<T> list = new ArrayList<>();
        for (T[] strings : listOfArrays)
            Collections.addAll(list, strings);

        return fromList(list, clazz);
    }

    public static <T> T[] fromList(List<T> list, Class<T[]> clazz) {
        return Arrays.copyOf(list.toArray(), list.size(), clazz);
    }

    public static <T> boolean contains(T[] array, T element) {
        if (array == null || array.length == 0)
            return false;

        for (T elementInArray : array)
            if (Objects.equals(elementInArray, element))
                return true;

        return false;
    }

    public static boolean contains(char[] array, char element) {
        if (array == null || array.length == 0)
            return false;

        for (char elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static boolean contains(boolean[] array, boolean element) {
        if (array == null || array.length == 0)
            return false;

        for (boolean elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static boolean contains(int[] array, int element) {
        if (array == null || array.length == 0)
            return false;

        for (int elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static boolean contains(long[] array, long element) {
        if (array == null || array.length == 0)
            return false;

        for (long elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static boolean contains(byte[] array, byte element) {
        if (array == null || array.length == 0)
            return false;

        for (byte elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static boolean contains(short[] array, short element) {
        if (array == null || array.length == 0)
            return false;

        for (short elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static boolean contains(float[] array, float element) {
        if (array == null || array.length == 0)
            return false;

        for (float elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static boolean contains(double[] array, double element) {
        if (array == null || array.length == 0)
            return false;

        for (double elementInArray : array)
            if (elementInArray == element)
                return true;

        return false;
    }

    public static <T> int count(T[] array, T element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (T elementInArray : array)
            if (Objects.equals(elementInArray, element))
                count++;

        return count;
    }

    public static int count(char[] array, char element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (char elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

    public static int count(boolean[] array, boolean element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (boolean elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

    public static int count(int[] array, int element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (int elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

    public static int count(long[] array, long element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (long elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

    public static int count(byte[] array, byte element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (byte elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

    public static int count(short[] array, short element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (short elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

    public static int count(float[] array, float element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (float elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

    public static int count(double[] array, double element) {
        if (array == null || array.length == 0)
            return 0;

        int count = 0;
        for (double elementInArray : array)
            if (elementInArray == element)
                count++;

        return count;
    }

}
