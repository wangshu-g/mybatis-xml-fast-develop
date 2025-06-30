---
sidebar_position: 2
---

# @Model

## 1.5.3及之前版本

标记 model 实体类

作为 mybatis-xml-fast-develop-generate-compile-time 中的扫描待编译（即当前模块待编译模型类）的钩子

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

## 1.5.3之后版本

1.5.3版本以及之前版本，只能作用于表上，改动后解放掉该限制，方便扩展开发需求

作用于表实体时，该属性为 true，会基于该标记的实体来建表

作用于模型时，该属性应为 false，同时需要指定 name 表名属性，当然你也可以直接继承表实体，会自动向上查找到第一个 @Model(table = true) 的注解

| 属性名称                | 注释                                         |
|---------------------|--------------------------------------------|
| table               | 是否为表实体，默认为 true                            |
| name                | 可选值，用于标记非表实体时手动指定表名，继承表实体可不指定              |
| sqlStyle            | 可选值，SQL 风格，默认 SqlStyle.lcc                 |
| modelDefaultKeyword | 可选值，默认关键字字段                                |
| title               | 可选值，字段标识名称，自动建表的 comment 属性，也是为了代码直观       |
| dataBaseType        | 可选值，数据库类型，根据此配置生成对应语法，注意所有生成的 SQL 都采用强语句规则 |

```java
package com.xxx.model;

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

```java
package com.xxx.table;

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
@Article(title = "文章")
public class Article extends BaseModel {

    @Column(title = "作者id")
    private String uid;

}
```

```java
package com.xxx.model;

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
@Article(title = "文章")
public class ArticleModel extends BaseModel {

    @Join(rightJoinField = "uid")
    private User author;

}
```