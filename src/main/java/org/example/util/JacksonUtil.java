package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> valueType) throws Exception {
        return objectMapper.readValue(json, valueType);
    }

    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson(byte[] jsonData, Class<T> valueType) throws Exception {
        return objectMapper.readValue(jsonData, valueType);
    }
}
