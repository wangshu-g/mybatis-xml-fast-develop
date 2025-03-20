package com.wangshu.cache;

import com.wangshu.base.model.BaseModel;
import com.wangshu.base.query.Query;
import com.wangshu.tool.CommonTool;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

@Data
public class QueryCache {

    public List<Field> fields;
    public Class<? extends BaseModel> modelGeneric;

    public QueryCache(Class<? extends Query> queryClazz) {
        this.fields = CommonTool.getClazzFields(queryClazz);
        this.modelGeneric = CommonTool.getQueryModel(queryClazz);
    }

}
