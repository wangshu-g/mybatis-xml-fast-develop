package com.wangshu.annotation;

import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.Condition;

import java.lang.annotation.*;

/**
 * 标记基础字段
 *
 * @author wangshu-g
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * <p>可选值,默认值为当前实体类</p>
     **/
    Class<? extends BaseModel> table() default BaseModel.class;

    /**
     * <p>可选值,指定当前标注字段是否是主键</p>
     **/
    boolean primary() default false;

    /**
     * <p>可选值,会根据类型选择数据类型</p>
     **/
    String jdbcType() default "";

    /**
     * <p>可选值,数据长度默认-1</p>
     **/
    int length() default -1;

    /**
     * <p>可选值,字段标题||注释</p>
     **/
    String comment() default "";

    /**
     * <p>可选值,字段标题||注释</p>
     **/
    String title() default "";

    /**
     * <p>可选值,指定当前标注字段是否是关键词字段</p>
     **/
    boolean keyword() default false;

    /**
     * <p>顺序,试验待用属性</p>
     **/
    int order() default 0;

    /**
     * <p>可选值,默认=,生成哪些条件</p>
     **/
    Condition[] conditions() default {Condition.equal};

    /**
     * <p>全文索引,试验待用属性</p>
     **/
    boolean enableFullText() default false;

}
