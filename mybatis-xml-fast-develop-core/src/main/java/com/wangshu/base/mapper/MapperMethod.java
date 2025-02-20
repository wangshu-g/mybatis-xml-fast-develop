package com.wangshu.base.mapper;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Data;
import com.wangshu.base.model.BaseModel;
import com.wangshu.tool.CommonTool;
import com.wangshu.tool.StringUtil;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

@Deprecated
public class MapperMethod {

    public String selectMax(ProviderContext context) {
        Class<?> mapperType = context.getMapperType();
        Method mapperMethod = context.getMapperMethod();
        if (BaseDataMapper.class.isAssignableFrom(mapperType)) {
            Class<? extends BaseModel> model = CommonTool.getMapperModel(mapperType);
            if (Objects.nonNull(model)) {
                return StringUtil.concat("select max(${column}) from ", this.getTableName(model));
            }
        }
        return null;
    }

    public String getTableName(@NotNull Class<? extends BaseModel> clazz) {
        String tableName = StrUtil.lowerFirst(clazz.getSimpleName());
        Data dataAnnotation = clazz.getAnnotation(Data.class);
        if (Objects.nonNull(dataAnnotation) && StringUtil.isNotEmpty(dataAnnotation.table())) {
            tableName = dataAnnotation.table();
        }
        return tableName;
    }

}
