package com.test.model;

import com.test.table.Article;
import com.test.table.ArticleGroup;
import com.test.table.ArticleTag;
import com.test.table.User;
import com.wangshu.annotation.Join;
import com.wangshu.annotation.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Model(table = false)
public class ArticleModel extends Article {

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
