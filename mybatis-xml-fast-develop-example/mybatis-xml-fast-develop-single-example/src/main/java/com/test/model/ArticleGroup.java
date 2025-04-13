package com.test.model;

import com.wangshu.annotation.Column;
import com.wangshu.annotation.Model;
import com.wangshu.annotation.Join;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Model(title = "文章分组", modelDefaultKeyword = "groupName")
public class ArticleGroup extends BaseModel {

    @Column(title = "ID", conditions = {Condition.all}, primary = true)
    private String id;

    @Column(title = "创建时间", conditions = {Condition.all})
    private Date createdAt;

    @Column(title = "更新时间", conditions = {Condition.all})
    private Date updatedAt;

    @Column(title = "删除时间", conditions = {Condition.all})
    private Date deletedAt;

    @Column(title = "分组名称", conditions = {Condition.all}, keyword = true)
    private String groupName;

    @Column(title = "描述", conditions = {Condition.all}, keyword = true)
    private String desc;

    @Column(title = "创建者", conditions = {Condition.all})
    private String uid;

    @Join(rightJoinField = "uid")
    private User user;

}
