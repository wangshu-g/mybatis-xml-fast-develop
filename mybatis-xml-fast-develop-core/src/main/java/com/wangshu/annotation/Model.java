package com.wangshu.annotation;

import com.wangshu.enu.DataBaseType;

import java.lang.annotation.*;

/**
 * 标记model实体类
 *
 * @author GSF
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {

    String table() default "";

    String modelDefaultKeyword() default "";

    String title() default "";

    String[] names() default {};

    DataBaseType dataBaseType() default DataBaseType.mysql;

}