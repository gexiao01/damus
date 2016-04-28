package com.ximalaya.damus.common.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> STRING_OBJECT_REFERENCE = new TypeReference<Map<String, Object>>() {
    };

    private static final Logger logger = Logger.getLogger(JsonUtils.class);

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public static String toJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            String message = new LogMessageBuilder("convert to json string fail").addParameter(
                    "object", object).toString();
            logger.warn(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static <T> T fromJsonString(Class<T> clazz, String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (Exception e) {
            String message = new LogMessageBuilder("convert from json string fail").addParameter(
                    "clazz", clazz).addParameter("jsonString", jsonString).toString();
            logger.warn(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static <T> T fromJsonString(TypeReference<T> typeReference, String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (Exception e) {
            String message = new LogMessageBuilder("convert from json string fail").addParameter(
                    "typeReference", typeReference).addParameter("jsonString", jsonString)
                    .toString();
            logger.warn(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static <T> T fromJsonStringToCollection(
            @SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass,
            Class<?> elementClass, String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(
                    collectionClass, elementClass);
            return OBJECT_MAPPER.readValue(jsonString, collectionType);
        } catch (Exception e) {
            String message = new LogMessageBuilder("convert from json string fail").addParameter(
                    "collectionClass", collectionClass).addParameter("elementClass", elementClass)
                    .addParameter("jsonString", jsonString).toString();
            logger.warn(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static Map<String, Object> toFieldMap(Object object) {
        return OBJECT_MAPPER.convertValue(object, STRING_OBJECT_REFERENCE);
    }

}
