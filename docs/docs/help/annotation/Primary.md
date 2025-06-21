---
sidebar_position: 5
---

# @Primary

标记主键字段，被该注解标识会被视为主键字段

某些不支持自增的数据库，会根据 incr 属性决定是否创建序列

| 属性名称 | 注释                    |
|------|-----------------------|
| incr | 可选值，是否是自增列，默认值为 false |


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

}
```