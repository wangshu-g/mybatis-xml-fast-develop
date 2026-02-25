package com.wangshu.tool;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Objects;

public final class Convert {

    private Convert() {
    }

    private static final ConversionService CONVERSION_SERVICE = DefaultConversionService.getSharedInstance();

    public static <T> T convert(Class<T> targetClass, Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return CONVERSION_SERVICE.convert(value, targetClass);
    }

}
