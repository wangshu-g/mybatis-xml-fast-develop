package com.wangshu.tool;

import com.alibaba.fastjson2.JSONObject;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class Convert {

    private Convert() {
    }

    private static final ConversionService CONVERSION_SERVICE;

    private static final List<String> DATE_PATTERNS = List.of(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy/MM/dd"
    );

    static {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(String.class, Date.class, Convert::parseDate);
        service.addConverter(String.class, LocalDateTime.class, Convert::parseLocalDateTime);
        service.addConverter(String.class, LocalDate.class, Convert::parseLocalDate);
        CONVERSION_SERVICE = service;
    }

    public static <T> T convert(Class<T> targetClass, Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Map<?, ?>) {
            return JSONObject.parseObject(JSONObject.toJSONString(value), targetClass);
        }
        return CONVERSION_SERVICE.convert(value, targetClass);
    }

    private static @Nullable Date parseDate(String source) {
        for (String pattern : DATE_PATTERNS) {
            if (pattern.contains("HH")) {
                LocalDateTime ldt = LocalDateTime.parse(source, DateTimeFormatter.ofPattern(pattern));
                return java.sql.Timestamp.valueOf(ldt);
            } else {
                LocalDate ld = LocalDate.parse(source, DateTimeFormatter.ofPattern(pattern));
                return java.sql.Date.valueOf(ld);
            }
        }
        return null;
    }

    private static @Nullable LocalDateTime parseLocalDateTime(String source) {
        for (String pattern : DATE_PATTERNS) {
            if (!pattern.contains("HH")) {
                continue;
            }
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }

    private static @Nullable LocalDate parseLocalDate(String source) {
        for (String pattern : DATE_PATTERNS) {
            if (pattern.contains("HH")) {
                continue;
            }
            return LocalDate.parse(source, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }

}
