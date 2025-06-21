---
sidebar_position: 10
---

# @DefaultOrder

标记默认排序字段，部分数据库版本分页时，强制要求排序字段

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

    @Column
    @CreatedAt
    @DefaultOrder
    private Date createdAt;

}
```