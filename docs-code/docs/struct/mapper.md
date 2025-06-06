---
sidebar_position: 3
---

# mapper

会根据模块名称自动生成到 com.xxx.mapper ，与 model 模块同级

继承 BaseDataMapper，你也不用去读这玩意的代码，就是基于生成的 xml 方法一一对应，不会带来任何困惑

```java
package com.test.mapper;

import com.test.model.Article;
import com.wangshu.base.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseDataMapper<Article> {
}
```