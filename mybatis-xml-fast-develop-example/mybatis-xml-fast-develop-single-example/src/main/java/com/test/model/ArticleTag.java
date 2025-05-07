package com.test.model;

import com.wangshu.annotation.Column;
import com.wangshu.annotation.Model;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Model(title = "文章标签", modelDefaultKeyword = "tagName")
public class ArticleTag extends BaseModel {

    @Column(title = "ID", conditions = {Condition.all}, primary = true)
    private String id;

    @Column(title = "创建时间", conditions = {Condition.all})
    private Date createdAt;

    @Column(title = "更新时间", conditions = {Condition.all})
    private Date updatedAt;

    @Column(title = "删除时间", conditions = {Condition.all})
    private Date deletedAt;

    @Column(title = "标签名称", conditions = {Condition.all}, keyword = true)
    private String tagName;

    @Column(title = "所属文章", conditions = {Condition.all})
    private String articleId;

}
