package com.wangshu.tool;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Slf4j
public class StringUtil {

    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "null".equals(String.valueOf(str)) || String.valueOf(str).trim().isEmpty());
    }

    @NotNull
    public static String concat(String... str) {
        if (Objects.isNull(str) || str.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : str) {
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

}
