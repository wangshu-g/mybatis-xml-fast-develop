---
sidebar_position: 1
---

# service

会根据模块名称自动生成到 com.xxx.service ，与 model 模块同级

继承 BaseDataService，也不用去读这玩意的代码，就是基于 mapper 扩展一些方法

```java
package com.test.service;

import com.test.model.Article;
import com.wangshu.base.service.BaseDataService;

public interface ArticleService extends BaseDataService<String, Article> {

}
```



