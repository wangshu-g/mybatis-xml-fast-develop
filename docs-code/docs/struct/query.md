---
sidebar_position: 6
---

# query

查询条件对象，根据 @Column 注解中配置的条件生成条件字段

会根据模块名称自动生成到 com.xxx.query ，与 model 模块同级

```java
package com.test.query;

import com.test.model.Article;
import com.wangshu.base.query.CommonQueryParam;
import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = true
)
public class ArticleQuery extends CommonQueryParam<Article> {
// 此处省略。。。
}

```

```java
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonQueryParam<T extends BaseModel> extends Query<T> {

    private Integer pageIndex;
    private Integer pageSize;
    private String orderColumn;
    private Boolean enableForUpdate;

}
```