package com.test.model;

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
@Model(title = "文章标签", modelDefaultKeyword = "tagName", dataBaseType = DataBaseType.mysql)
public class ArticleTag extends BaseModel {

    @Column(title = "ID", conditions = {Condition.all}, primary = true)
    @DefaultOrder
    private String id;

    @Column(title = "创建时间", conditions = {Condition.all})
    @CreatedAt
    private Date createdAt;

    @Column(title = "更新时间", conditions = {Condition.all})
    @UpdatedAt
    private Date updatedAt;

    @Column(title = "删除时间", conditions = {Condition.all})
    @DeletedAt
    private Date deletedAt;

    @Column(title = "标签名称", conditions = {Condition.all}, keyword = true)
    private String tagName;

    @Column(title = "所属文章", conditions = {Condition.all})
    private String articleId;

}
