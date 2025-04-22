package com.wangshu.annotation;

// MIT License
//
// Copyright (c) 2025 2560334673@qq.com wangshu-g https://github.com/wangshu-g/mybatis-xml-fast-develop
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

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
     * <p>待用属性</p>
     **/
    int order() default 0;

    /**
     * <p>可选值,默认=,生成哪些条件</p>
     **/
    Condition[] conditions() default {Condition.equal};


}
