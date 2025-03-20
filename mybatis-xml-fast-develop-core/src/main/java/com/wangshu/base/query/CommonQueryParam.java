package com.wangshu.base.query;

import com.wangshu.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangshu-g
 *
 * <p>常用固定参数</p>
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonQueryParam<T extends BaseModel> extends Query<T> {

    private Integer pageIndex;
    private Integer pageSize;
    private String orderColumn;
    private Boolean enableForUpdate;

}