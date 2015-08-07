package com.superum.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

public class MockMvcUtils {

    public static <T> T fromResponse(MvcResult result, Class<T> clazz) throws IOException {
        byte[] content = result.getResponse().getContentAsByteArray();
        return JsonUtils.convertBytesToObject(content, clazz);
    }

    public static <T> T fromResponse(MvcResult result, TypeReference<T> type) throws IOException {
        byte[] content = result.getResponse().getContentAsByteArray();
        return JsonUtils.convertBytesToObject(content, type);
    }

    // PRIVATE

    private MockMvcUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }

}
