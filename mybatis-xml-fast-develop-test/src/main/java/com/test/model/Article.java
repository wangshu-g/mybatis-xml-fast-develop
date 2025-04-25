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
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@Model(title = "文章", modelDefaultKeyword = "title", dataBaseType = DataBaseType.mysql)
public class Article extends BaseModel {

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

    @Column(conditions = {Condition.all}, title = "文章标题", keyword = true)
    private String title;

    @Column(conditions = {Condition.all}, title = "描述", keyword = true)
    private String desc;

    @Column(conditions = {Condition.all}, title = "文章详情")
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
     * <p>
     * 并不推荐这么做，一般我会在代码中单独处理此类查询
     * 更推荐在业务中避免此类对多查询映射，比如把 articleTagList 处理为 json 字符串存储，使用时再做处理，尽量只做一次性查询
     * 更多避免的是复杂关系映射可能带来的间接查询扩散和不可控问题，特别是查询一个列表
     * 可以看到对于 {@link Article#user} 此类对一查询映射，没有使用 select 方式间接查询，而是通过手动映射方式，仅做一次性查询
     * </p>
     **/
    @Join(leftJoinField = "articleId")
    private List<ArticleTag> articleTagList;

}
