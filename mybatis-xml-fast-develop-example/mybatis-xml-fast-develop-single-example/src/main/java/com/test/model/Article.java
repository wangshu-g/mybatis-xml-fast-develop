package com.test.model;

import com.wangshu.annotation.*;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Model(title = "文章", modelDefaultKeyword = "title")
public class Article extends BaseModel {

    @Column(title = "ID", conditions = {Condition.all})
    @Primary
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

    @Column(conditions = {Condition.all}, title = "文章标题", keyword = true)
    private String title;

    @Column(conditions = {Condition.all}, title = "描述", keyword = true)
    private String desc;

    @Column(conditions = {Condition.all}, title = "文章详情", dbColumnType = "longtext")
    private String detail;

    @Column(conditions = {Condition.all}, title = "状态")
    private String status;

    @Column(conditions = {Condition.all}, title = "发布者")
    private String uid;

    @Column(conditions = {Condition.all}, title = "文章所属分组")
    private String groupId;

    @Join(rightJoinField = "uid")
    private User user;

    @Join(rightJoinField = "groupId")
    private ArticleGroup articleGroup;

    /**
     * <p>不推荐</p>
     **/
    @Join(leftJoinField = "articleId")
    private List<ArticleTag> articleTagList;

}
