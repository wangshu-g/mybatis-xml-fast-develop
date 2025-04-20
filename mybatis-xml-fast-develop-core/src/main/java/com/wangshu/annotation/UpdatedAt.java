package com.wangshu.annotation;

import java.lang.annotation.*;

/**
 * @author wangshu-g
 *
 * <p>标记更新时间字段</p>
 **/
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdatedAt {
}
