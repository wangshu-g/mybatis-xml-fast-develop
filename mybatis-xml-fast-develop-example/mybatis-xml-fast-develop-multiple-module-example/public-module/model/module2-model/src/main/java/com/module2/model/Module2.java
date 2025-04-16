package com.module2.model;

import com.module1.model.Module1;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Model;
import com.wangshu.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Model
public class Module2 extends BaseModel {

    @Column(primary = true)
    private Long id;

    @Column
    private String createdAt;

    @Column
    private String updatedAt;

    @Column
    private String deletedAt;

    /**
     * <p>仅分层可以直接 @Join </p>
     **/
    private Module1 module1;

}
