package com.wangshu.annotation;

import com.wangshu.ConfigRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangshu-g
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ConfigRegister.class)
public @interface EnableConfig {

    String[] modelPackage() default {};

    boolean enableAutoInitTable() default false;

    String[] targetDataSource() default {"*"};

    boolean enableExceptionHandle() default true;

}
