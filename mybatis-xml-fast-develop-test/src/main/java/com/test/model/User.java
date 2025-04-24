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
@Model(title = "用户", modelDefaultKeyword = "name", dataBaseType = DataBaseType.mssql)
public class User extends BaseModel {

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

    @Column(title = "昵称", conditions = {Condition.all}, keyword = true)
    private String name = "";

    @Column(title = "自述", conditions = {Condition.all}, keyword = true)
    private String readMe = "";

}
