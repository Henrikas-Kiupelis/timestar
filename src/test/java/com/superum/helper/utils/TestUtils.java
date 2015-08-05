package com.superum.helper.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class TestUtils {
 
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static <T> T fromResponse(MvcResult result, Class<T> clazz) throws IOException {
        byte[] content = result.getResponse().getContentAsByteArray();
        return convertBytesToObject(content, clazz);
    }

    public static <T> T fromResponse(MvcResult result, TypeReference<T> type) throws IOException {
        byte[] content = result.getResponse().getContentAsByteArray();
        return convertBytesToObject(content, type);
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }
    
    public static String convertObjectToString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T convertStringToObject(String object, Class<T> clazz) throws IOException {
        return mapper.reader(clazz).readValue(object);
    }

    public static <T> T convertStringToObject(String object, TypeReference<T> type) throws IOException {
        return mapper.reader(type).readValue(object);
    }

    public static <T> T convertBytesToObject(byte[] object, Class<T> clazz) throws IOException {
        return mapper.reader(clazz).readValue(object);
    }

    public static <T> T convertBytesToObject(byte[] object, TypeReference<T> type) throws IOException {
        return mapper.reader(type).readValue(object);
    }
 
    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();
 
        for (int index = 0; index < length; index++)
            builder.append("a");

        return builder.toString();
    }
    
    // PRIVATE
    
    private static final ObjectMapper mapper;
    static {
    	mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
    	mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}