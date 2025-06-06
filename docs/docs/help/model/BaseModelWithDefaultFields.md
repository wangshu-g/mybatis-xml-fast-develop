---
sidebar_position: 2
---

# BaseModelWithDefaultFields

继承 BaseModel，添加一些常用的字段

```java 
package com.wangshu.base.model;

import com.wangshu.annotation.*;
import com.wangshu.enu.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author wangshu-g
 * <p>几个常用的字段</p>
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class BaseModelWithDefaultFields<T> extends BaseModel {

    @Column(conditions = Condition.all)
    @Primary
    @DefaultOrder
    private T id;

    @CreatedAt
    @Column(conditions = Condition.all)
    private Date createdAt = new Date();

    @UpdatedAt
    @Column(conditions = Condition.all)
    private Date updatedAt = new Date();

    @DeletedAt
    @Column(conditions = Condition.all)
    private Date deletedAt;

    @DeleteFlag
    @Column(conditions = Condition.all)
    private Boolean deleteFlag = false;

}

```