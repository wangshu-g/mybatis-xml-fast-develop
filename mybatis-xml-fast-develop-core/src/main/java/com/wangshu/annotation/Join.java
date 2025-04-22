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
import com.wangshu.enu.JoinCondition;
import com.wangshu.enu.JoinType;

import java.lang.annotation.*;

/**
 * <p>标记连表查询字段</p>
 * <p>select</p>
 * <p>...currentModelBaseFields,</p>
 * <p>...leftSelectFields</p>
 * <p>from currentModel</p>
 * <p>left join leftTable on (左) leftTable.leftJoinField = rightTable.rightJoinField (右)</p>
 *
 * @author wangshu-g
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {

    /**
     * <p>可选值,默认值为当前标注字段的类型</p>
     **/
    Class<? extends BaseModel> leftTable() default BaseModel.class;

    /**
     * <p>可选值,默认值为id或关联实体中指定的主键字段</p>
     **/
    String leftJoinField() default "id";

    /**
     * <p>可选值,默认值为关联实体的所有基础属性</p>
     **/
    String[] leftSelectFields() default {"*"};

    /**
     * <p>可选值,默认值为当前实体类</p>
     **/
    Class<? extends BaseModel> rightTable() default BaseModel.class;

    /**
     * <p>这个是连表必填值,指定关联属性名称</p>
     * <p>left join leftTable on leftTable.id = rightTable.rightJoinField</p>
     **/
    String rightJoinField() default "id";

    /**
     * <p>可选值</p>
     * <p>使用场景较少,用于存在间接关联关系时精确指定关联属性字段</p>
     * <p>当rightTable不是当前实体类,自动选择当前实体类中类型与rightTable类型一致的join字段作为关联项</p>
     * <p>当存在一个以上符合条件的字段,该属性用于精确指定关联属性字段</p>
     **/
    String indirectJoinField() default "";

    /**
     * <p>可选值,默认值为left</p>
     **/
    JoinType joinType() default JoinType.left;

    /**
     * <p>可选值,默认值为=</p>
     **/
    JoinCondition joinCondition() default JoinCondition.equal;

    /**
     * <p>可选值,默认值为Model</p>
     **/
    String infix() default "Model";

    /**
     * <p>可选值</p>
     **/
    String comment() default "";

}
