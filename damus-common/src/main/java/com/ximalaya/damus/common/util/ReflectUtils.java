package com.ximalaya.damus.common.util;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

public class ReflectUtils {

    /**
     * 获取obj对象fieldName的Field
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        return FieldUtils.getField(obj.getClass(), fieldName);
    }

    /**
     * 获取obj对象fieldName的属性值
     */
    public static Object getValueByFieldName(Object obj, String fieldName)
            throws IllegalAccessException {
        return FieldUtils.readField(obj, fieldName, true);
    }

    /**
     * 设置obj对象fieldName的属性值
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        FieldUtils.writeField(obj, fieldName, value, true);
    }

    public static void invokeMethod(Object targetObject, String methodName, Class<?> parameterType,
            Object parameter) throws Exception {
        MethodUtils.invokeExactMethod(targetObject, methodName, new Object[] { parameter },
                new Class<?>[] { parameterType });
    }
}
