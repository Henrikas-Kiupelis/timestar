package com.superum.utils;

public class ObjectUtils {

    public static int hash(Object... objects) {
        return hashPart(17, objects);
    }

    public static int hashPart(int init, Object... objects) {
        for (Object o : objects)
            init = (init << 5) - init + (o == null ? 0 : o.hashCode());

        return init;
    }

}
