package com.wangshu.annotation;

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
 * @author GSF
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
     * <p>关联属性名称,该值需要自行指定</p>
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
