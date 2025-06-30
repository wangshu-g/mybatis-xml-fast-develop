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

import com.wangshu.enu.DataBaseType;
import com.wangshu.enu.SqlStyle;

import java.lang.annotation.*;

/**
 * @author wangshu-g
 * <p>标记 model 实体类</p>
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {

    /**
     * <p>改动，作为表和模型的标识，最初是将表的实体本身作为模型来环绕设计的</p>
     * <p>1.5.3版本以及之前版本，{@link Model} 只能作用于表上，改动后解放掉该限制，方便扩展开发需求</p>
     * <p>作用于表实体时，该属性为 true，会基于该标记的实体来建表</p>
     * <p>作用于模型时，该属性应为 false，同时需要指定 {@link Model#name()} 表名属性，当然你也可以直接继承表实体，会自动向上查找到第一个 @Model(table = true) 的注解</p>
     **/
    boolean table() default true;

//    @Deprecated
//    String table() default "";

    String name() default "";

    SqlStyle sqlStyle() default SqlStyle.lcc;

    String modelDefaultKeyword() default "";

    String title() default "";

//    @Deprecated
//    String[] names() default {};

    DataBaseType dataBaseType() default DataBaseType.mysql;

}