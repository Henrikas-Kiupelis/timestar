package com.superum.helper;

import java.util.Arrays;
import java.util.List;

public final class NullChecker {

    public void notNull(String message) {
        int index = objects.indexOf(null);
        if (index >= 0)
            throw new NullPointerException(message + "; parameter at index " + index + " was null, please check: " + objects);
    }

    // CONSTRUCTORS

    public static NullChecker check(Object... objects) {
        return new NullChecker(objects);
    }

    public NullChecker(Object... objects) {
        this.objects =  Arrays.asList(objects);
    }

    // PRIVATE

    private final List<Object> objects;

}
