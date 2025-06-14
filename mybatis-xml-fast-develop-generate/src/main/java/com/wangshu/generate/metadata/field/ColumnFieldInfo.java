package com.wangshu.generate.metadata.field;

// MIT License
//
// Copyright (c) 2025 2560334673@qq.com wangshu-g https://github.com/wangshu-g/mybatis-xml-fast-develop
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.annotation.Primary;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.SqlStyle;
import com.wangshu.generate.metadata.model.ModelClazzInfo;
import com.wangshu.tool.CommonTool;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class ColumnFieldInfo extends AbstractColumnInfo<Field, ModelClazzInfo> {

    public ColumnFieldInfo(Field metaData, ModelClazzInfo model) {
        this.setMetaData(metaData);
        this.setModel(model);
        this.initBaseInfo(metaData, model);
        this.initColumnInfo(metaData, model);
        this.initJoinInfo(metaData, model);
    }

    public void initBaseInfo(@NotNull Field metaData, ModelClazzInfo model) {
        this.setName(metaData.getName());
        this.setSqlStyleName(this.initSqlStyleName(metaData, model));
        this.setJavaTypeName(metaData.getType().getTypeName());
    }

    @Override
    public void initColumnInfo(@NotNull Field metaData, ModelClazzInfo model) {
        Column column = metaData.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            Primary primary = metaData.getAnnotation(Primary.class);
            this.setColumn(column);
            this.setDbColumnType(this.initDbColumnType(metaData, column));
            this.setTitle(column.title());
            this.setComment(column.comment());
            this.setConditions(Arrays.asList(column.conditions()));
            this.setBaseField(true);
            if (Objects.nonNull(primary)) {
                this.setPrimaryField(true);
                this.setIncrPrimary(primary.incr());
            }
            this.setKeywordField(column.keyword());
        }
    }

    @Override
    public void initJoinInfo(@NotNull Field metaData, ModelClazzInfo model) {
        Join join = metaData.getAnnotation(Join.class);
        Class<?> fieldType = metaData.getType();
        if (Objects.nonNull(join)) {
            this.setJoin(join);
            this.setClassJoinField(BaseModel.class.isAssignableFrom(fieldType));
            this.setCollectionJoinField(StrUtil.equals(fieldType.getName(), List.class.getName()));
            this.setLeftModel(this.initLeftModel(model, join));
            this.setRightModel(this.initRightModel(model, join));
            this.setJoin(join);
            this.setJoinField(true);
            this.setLeftSelectFieldNames(Arrays.asList(this.getJoin().leftSelectFields()));
            this.setLeftJoinField(this.getJoin().leftJoinField());
            this.setRightJoinField(this.getJoin().rightJoinField());
            this.setIndirectJoinField(this.getJoin().indirectJoinField());
            this.setJoinType(this.getJoin().joinType());
            this.setJoinCondition(this.getJoin().joinCondition());
            this.setInfix(this.getJoin().infix());
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private ModelClazzInfo initLeftModel(@NotNull ModelClazzInfo model, @NotNull Join join) {
        if (StrUtil.equals(join.leftTable().getSimpleName(), BaseModel.class.getSimpleName())) {
            Class<?> type = this.getMetaData().getType();
            if (BaseModel.class.isAssignableFrom(type)) {
                return new ModelClazzInfo(model.getModuleInfo(), (Class<? extends BaseModel>) type, true);
            } else if (List.class.isAssignableFrom(type)) {
                if (this.getMetaData().getGenericType() instanceof ParameterizedType genericType) {
                    Type[] actualTypeArguments = genericType.getActualTypeArguments();
                    if (actualTypeArguments.length != 0) {
                        return new ModelClazzInfo(model.getModuleInfo(), (Class<? extends BaseModel>) actualTypeArguments[0], true);
                    }
                }
            } else {
                return null;
            }
        } else {
            return new ModelClazzInfo(model.getModuleInfo(), this.getJoin().leftTable(), true);
        }
        return null;
    }

    private ModelClazzInfo initRightModel(@NotNull ModelClazzInfo model, @NotNull Join join) {
//            默认值,未指定rightTable,则为当前类
        if (StrUtil.equals(join.rightTable().getSimpleName(), BaseModel.class.getSimpleName())) {
            return this.getModel();
        } else {
            return new ModelClazzInfo(model.getModuleInfo(), this.getJoin().rightTable(), true);
        }
    }

    private String initDbColumnType(@NotNull Field field, @NotNull Column column) {
        String dbColumnType = column.dbColumnType();
        if (StrUtil.isBlank(dbColumnType)) {
            dbColumnType = CommonTool.getDbColumnTypeByJavaTypeName(this.getModel().getDataBaseType(), this.getJavaTypeName());
        }
        return dbColumnType;
    }

    private String initSqlStyleName(@NotNull Field metaData, @NotNull ModelClazzInfo model) {
        if (Objects.equals(model.getSqlStyle(), SqlStyle.sc)) {
            return StrUtil.toUnderlineCase(this.getName());
        }
        return StrUtil.lowerFirst(this.getName());
    }

}
