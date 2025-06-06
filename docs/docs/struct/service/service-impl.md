---
sidebar_position: 2
---

# service-impl

会根据模块名称自动生成到 com.xxx.service.impl

``` java
package com.test.service.impl;

import com.test.mapper.ArticleMapper;
import com.test.model.Article;
import com.test.service.ArticleService;
import com.wangshu.base.service.AbstractBaseDataService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ArticleServiceImpl extends AbstractBaseDataService<String, ArticleMapper, Article> implements ArticleService {
    @Resource
    public ArticleMapper articleMapper;

    @Override
    public ArticleMapper getMapper() {
        return articleMapper;
    }

    @Override
    public void deleteAll() {
        getMapper()._delete(new HashMap<>());
    }
}

```



