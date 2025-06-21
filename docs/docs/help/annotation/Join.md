---
sidebar_position: 4
---

# @Join

标记连表字段

| 属性名称              | 注释                                                                               |
|-------------------|----------------------------------------------------------------------------------|
| leftTable         | 可选值，默认值为当前标注字段的类型                                                                |
| leftJoinField     | 可选值，默认值为 id 或关联实体中指定的主键字段                                                        |
| leftSelectFields  | 可选值，默认值为关联实体的所有基础属性                                                              |
| rightTable        | 可选值，默认值为当前实体类                                                                    |
| rightJoinField    | 连表必填值，指定关联属性名称。例：left join leftTable on leftTable.id = rightTable.rightJoinField |
| indirectJoinField | 可选值，用于精确指定间接关联字段；用于存在多个可能匹配字段的情况                                                 |
| joinType          | 可选值，默认值为 left                                                                    |
| joinCondition     | 可选值，默认值为 =                                                                       |
| infix             | 可选值，默认值为 Model                                                                   |
| comment           | 可选值                                                                              |


```java

package com.test.model;

import com.wangshu.annotation.*;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.Condition;
import com.wangshu.enu.DataBaseType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@Model(title = "文章", modelDefaultKeyword = "title", dataBaseType = DataBaseType.mysql)
public class Article extends BaseModel {
    
    @Column
    @Primary
    private String id;

    // 一对一，left join User on User.id = Article.uid
    @Join(rightJoinField = "uid")
    private User user;
    
    // 一对一
    @Join(rightJoinField = "groupId")
    private ArticleGroup articleGroup;

    // 一对多，不建议使用，最好单独查
    @Join(leftJoinField = "articleId")
    private List<ArticleTag> articleTagList;

}

```