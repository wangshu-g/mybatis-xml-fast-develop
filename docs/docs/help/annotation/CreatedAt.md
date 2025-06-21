---
sidebar_position: 6
---

# @CreatedAt

标记创建时间

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
    private Date createdAt;

}
```