package com.superum.api.table;

import com.superum.api.table.dto.OptimizedLessonTableDTO;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Caches Lesson Table for certain parameters, to reduce the database load; it should be invalidated after every non-GET request
 */
public class TableCache {

    public static synchronized void put(Integer page, Integer perPage, Long start, Long end, OptimizedLessonTableDTO table) {
        TABLE_CACHE.put(Tuple.tuple(page, perPage, start, end), table);
    }

    public static synchronized OptimizedLessonTableDTO get(Integer page, Integer perPage, Long start, Long end) {
        return TABLE_CACHE.get(Tuple.tuple(page, perPage, start, end));
    }

    public static synchronized void invalidateCache() {
        TABLE_CACHE = Collections.synchronizedMap(new HashMap<>());
    }

    private static Map<Tuple4<Integer, Integer, Long, Long>, OptimizedLessonTableDTO> TABLE_CACHE = new HashMap<>();

}
