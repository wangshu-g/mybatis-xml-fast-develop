package com.wangshu.tool;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaTypeMapInfo {

    private static final Map<String, String> JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME = new HashMap<>();

    public static String getNameBySimpleName(String simpleName) throws IllegalArgumentException {
        String name = JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.get(simpleName);
        if (StringUtil.isEmpty(name)) {
            throw new IllegalArgumentException("Unsupported SimpleName: " + simpleName);
        } else {
            return name;
        }
    }

    static {
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(String.class.getSimpleName(), String.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Long.class.getSimpleName(), Long.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(long.class.getSimpleName(), long.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Integer.class.getSimpleName(), Integer.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(int.class.getSimpleName(), int.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(BigDecimal.class.getSimpleName(), BigDecimal.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Short.class.getSimpleName(), Short.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(short.class.getSimpleName(), short.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Float.class.getSimpleName(), Float.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(float.class.getSimpleName(), float.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Double.class.getSimpleName(), Double.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(double.class.getSimpleName(), double.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Character.class.getSimpleName(), Character.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(char.class.getSimpleName(), char.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Boolean.class.getSimpleName(), Boolean.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(boolean.class.getSimpleName(), boolean.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Date.class.getSimpleName(), Date.class.getName());
    }

}