package com.module1.model;

import com.wangshu.annotation.Column;
import com.wangshu.annotation.Model;
import com.wangshu.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Model
public class Module1 extends BaseModel {

    @Column(primary = true)
    private Long id;

    @Column
    private String idCard;

    @Column
    private String createdAt;

    @Column
    private String updatedAt;

    @Column
    private String deletedAt;

}
