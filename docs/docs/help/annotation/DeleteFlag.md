---
sidebar_position: 9
---

# @DeleteFlag

标记是否删除，_softDelete 会根据 @DeletedAt 和 @DeleteFlag 实现

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
    @DeleteFlag
    private Boolean deleteFlag;

}
```
