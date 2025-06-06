---
sidebar_position: 5
---

# controller

会根据模块名称自动生成到 com.xxx.controller ，与 model 模块同级

生成这个只是为了方便，生产环境一定要注意安全问题，可以自己写一个内部使用的 BaseController 继承替换掉

```java
package com.test.controller;

import com.test.model.Article;
import com.test.query.ArticleQuery;
import com.test.service.ArticleService;
import com.wangshu.base.controller.AbstractBaseDataControllerString;
import com.wangshu.base.result.ResultBody;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wangshu.tool.KeyValue.KV;

@Slf4j
@RestController
@RequestMapping("/Article")
public class ArticleController extends AbstractBaseDataControllerString<ArticleService, Article> {
    @Resource
    public ArticleService articleService;

    @Override
    public ArticleService getService() {
        return articleService;
    }

    @Override
    public Article getModel() {
        return new Article();
    }
}
```