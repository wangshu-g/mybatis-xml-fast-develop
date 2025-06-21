---
sidebar_position: 2
---

# @Model

标记 model 实体类

作为 mybatis-xml-fast-develop-generate-compile-time 中的钩子

编译期会扫描附带该注解的模型实体类

| 属性名称                | 注释                                         |
|---------------------|--------------------------------------------|
| table               | 可选值，表名                                     |
| sqlStyle            | 可选值，SQL 风格                                 |
| modelDefaultKeyword | 可选值，默认关键字字段                                |
| title               | 可选值，字段标识名称，自动建表的 comment 属性，也是为了代码直观       |
| names               | 可选值，待用                                     |
| dataBaseType        | 可选值，数据库类型，根据此配置生成对应语法，注意所有生成的 SQL 都采用强语句规则 |


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

}
```