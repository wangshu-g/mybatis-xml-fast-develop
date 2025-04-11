package com.wangshu.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 当使用 scan-class-file: true,并且模块内没有{@link Model}注解时，AbstractProcessor 不会触发，使用该注解强制触发编译期生成
 * </p>
 *
 * @author wangshu-g
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForceTrigger {
}
