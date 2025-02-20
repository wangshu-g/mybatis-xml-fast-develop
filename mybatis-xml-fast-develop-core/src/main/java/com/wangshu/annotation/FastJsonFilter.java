package com.wangshu.annotation;

import com.wangshu.enu.FilterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author GSF
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FastJsonFilter {

    FilterType filterType() default FilterType.NULL;

}
