package com.module2.model;

import com.module1.model.Module1;
import com.wangshu.annotation.*;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Model
public class Module2 extends BaseModel {

    @Column(title = "ID", conditions = {Condition.all})
    @Primary
    @DefaultOrder
    private Long id;

    @Column(title = "创建时间", conditions = {Condition.all})
    @CreatedAt
    private Date createdAt;

    @Column(title = "更新时间", conditions = {Condition.all})
    @UpdatedAt
    private Date updatedAt;

    @Column(title = "删除时间", conditions = {Condition.all})
    @DeletedAt
    private Date deletedAt;

    /**
     * <p>仅分层可以直接 @Join </p>
     **/
    private Module1 module1;

}
