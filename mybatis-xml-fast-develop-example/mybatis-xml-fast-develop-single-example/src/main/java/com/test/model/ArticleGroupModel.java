package com.test.model;

import com.test.table.ArticleGroup;
import com.test.table.User;
import com.wangshu.annotation.Join;
import com.wangshu.annotation.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Model(table = false)
public class ArticleGroupModel extends ArticleGroup {

    @Join(rightJoinField = "uid")
    private User user;

}
