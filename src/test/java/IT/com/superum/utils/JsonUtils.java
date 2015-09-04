package IT.com.superum.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

public class JsonUtils {
 
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static String replace(String json, String field, Object value) throws IOException {
        Map<String, Object> jsonMap = JsonUtils.convertStringToObject(json, JSON_MAP);
        jsonMap.put(field, value);
        return JsonUtils.convertObjectToString(jsonMap);
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

    // PRIVATE

    private JsonUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
    }
    
    private static final ObjectMapper mapper;
    static {
    	mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
    	mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static final TypeReference<Map<String, Object>> JSON_MAP = new TypeReference<Map<String, Object>>() {};

}