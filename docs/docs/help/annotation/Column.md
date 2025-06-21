---
sidebar_position: 3
---

# @Column

标记字段，被该注解标识会被视为字段建表

[详情见 model 中用法](/docs/struct/model)

| 属性名称         | 注释                   |
|--------------|----------------------|
| table        | 可选值，默认值为当前实体类        |
| dbColumnType | 可选值，会根据类型选择数据类型      |
| comment      | 可选值，字段标题或注释          |
| title        | 可选值，字段标题或注释          |
| keyword      | 可选值，指定当前标注字段是否是关键词字段 |
| conditions   | 可选值，生成哪些条件，默认只有 = 条件 |

## xml 中参数名称生成对照

以 String name 字段为例子，除了 equal，参数名称生成逻辑就是 [字段名称]+[首字母大写的条件名称]

**所有的 or 条件，必须添加 enableOr 参数**，[详情见CommonQueryParam](/docs/struct/query)

| 名称          | 解释               | 生成的参数名称         |
|-------------|------------------|-----------------|
| equal       | 等于 (=)           | name            |
| less        | 小于 (`<`)         | nameLess        |
| great       | 大于 (`>`)         | nameGreat       |
| like        | 模糊匹配 (LIKE)      | nameLike        |
| instr       | 包含（使用 INSTR 函数）  | nameInstr       |
| in          | 属于某个集合 (IN)      | nameIn          |
| isNull      | 是否为 NULL         | nameIsNull      |
| isNotNull   | 是否不为 NULL        | nameIsNotNull   |
| orEqual     | 或：等于             | nameOrEqual     |
| orLess      | 或：小于             | nameOrLess      |
| orGreat     | 或：大于             | nameOrGreat     |
| orLike      | 或：模糊匹配           | nameOrLike      |
| orInstr     | 或：包含             | nameOrInstr     |
| orIn        | 或：属于集合           | nameOrIn        |
| orIsNull    | 或：为空             | nameOrIsNull    |
| orIsNotNull | 或：不为空            | nameOrIsNotNull |
| all         | 所有条件都生效（通常表示全匹配） | 代表所有条件          |

```java
import com.wangshu.annotation.*;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.Condition;
import com.wangshu.enu.DataBaseType;
import com.wangshu.enu.SqlStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@Model(title = "用户")
public class User extends BaseModel {

    @Column(title = "ID")
    @Primary
    private String id;

    @Column(conditions = {Condition.equal, Condition.instr, Condition.like})
    private String name;

    @Column(conditions = {Condition.equal, Condition.less, Condition.great})
    private String age;

}
```