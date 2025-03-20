package com.wangshu.base.query;

import com.wangshu.base.entity.EntityTool;
import com.wangshu.base.model.BaseModel;
import com.wangshu.tool.CacheTool;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author wangshu-g
 *
 * <p>查询参数，参数合法性和规范性问题</p>
 **/
public class Query<T extends BaseModel> extends EntityTool {

    @Override
    public List<Field> modelFields() {
        return CacheTool.getQueryFields(this.getClass());
    }

    @SuppressWarnings("unchecked")
    public T toModel() {
        return (T) this.toEntity(CacheTool.getQueryModelGeneric(this.getClass()));
    }

}