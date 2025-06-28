package com.wangshu.generate.metadata.model;

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
import com.wangshu.annotation.Model;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.SqlStyle;
import com.wangshu.generate.metadata.field.AbstractColumnInfo;
import com.wangshu.generate.metadata.field.ColumnFieldInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;
import com.wangshu.tool.CommonTool;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class ModelClazzInfo extends AbstractModelInfo<Class<? extends BaseModel>, ColumnFieldInfo> {

    public ModelClazzInfo(ModuleInfo moduleInfo, Class<? extends BaseModel> metaData) {
        this.setModuleInfo(moduleInfo);
        this.setMetaData(metaData);
        this.initBaseInfo(moduleInfo, metaData, false);
        this.initFields(moduleInfo, metaData, false);
        this.initNameInfo();
        this.initApiInfo();
    }

    public ModelClazzInfo(ModuleInfo moduleInfo, Class<? extends BaseModel> metaData, boolean ignoreJoinFields) {
        this.setModuleInfo(moduleInfo);
        this.setMetaData(metaData);
        this.initBaseInfo(moduleInfo, metaData, ignoreJoinFields);
        this.initFields(moduleInfo, metaData, ignoreJoinFields);
        this.initNameInfo();
        this.initApiInfo();
    }

    public void initBaseInfo(ModuleInfo moduleInfo, @NotNull Class<? extends BaseModel> metaData, boolean ignoreJoinFields) {
        Model modelAnnotation = metaData.getAnnotation(Model.class);
        this.setModelAnnotation(modelAnnotation);
        this.setDataBaseType(modelAnnotation.dataBaseType());
        this.setModelDefaultKeyword(modelAnnotation.modelDefaultKeyword());
        this.setSqlStyle(this.initSqlStyle());
        this.setTableName(this.initTableName(moduleInfo, metaData, ignoreJoinFields));
        this.setModelTitle(modelAnnotation.title());
        this.setModelName(metaData.getSimpleName());
        this.setModelFullName(metaData.getTypeName());
        this.setModelPackageName(this.getModelFullName().replace(StrUtil.concat(false, ".", this.getModelName()), ""));
    }

    private void initFields(ModuleInfo moduleInfo, Class<? extends BaseModel> metaData, boolean ignoreJoinFields) {
        Class<?> clazz = metaData;
        List<ColumnFieldInfo> fields = new ArrayList<>();
        Map<String, Object> nameMap = new HashMap<>();
        while (Objects.nonNull(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Objects.isNull(nameMap.get(field.getName()))) {
                    nameMap.put(field.getName(), new Object());
                    if (Objects.nonNull(field.getAnnotation(Column.class))) {
                        fields.add(new ColumnFieldInfo(field, this));
                    } else if (Objects.nonNull(field.getAnnotation(Join.class)) && !ignoreJoinFields) {
                        fields.add(new ColumnFieldInfo(field, this));
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        this.setFields(fields);
        this.setBaseFields(fields.stream().filter(AbstractColumnInfo::isBaseField).sorted((p1, p2) -> Boolean.compare(p1.isPrimaryField(), p2.isPrimaryField())).toList().reversed());
        this.setJoinFields(fields.stream().filter(AbstractColumnInfo::isJoinField).toList());
        this.setClazzJoinFields(fields.stream().filter(AbstractColumnInfo::isClassJoinField).toList());
        this.setCollectionJoinFields(fields.stream().filter(AbstractColumnInfo::isCollectionJoinField).toList());
        this.setKeyWordFields(fields.stream().filter(AbstractColumnInfo::isKeywordField).collect(Collectors.toList()));
        this.setPrimaryField(fields.stream().filter(AbstractColumnInfo::isPrimaryField).findFirst().orElse(null));
        this.setDefaultModelKeyWordField(this.getFieldByName(this.getModelDefaultKeyword()));
    }

    private SqlStyle initSqlStyle() {
        if (this.getModelAnnotation().table() || StrUtil.isNotBlank(this.getModelAnnotation().name())) {
            return this.getModelAnnotation().sqlStyle();
        }
        Class<?> parentTableModel = this.findParentTableModel();
        Model tableModelAnnotation = parentTableModel.getAnnotation(Model.class);
        return tableModelAnnotation.sqlStyle();
    }

    private String initTableName(ModuleInfo moduleInfo, @NotNull Class<? extends BaseModel> metaData, boolean ignoreJoinFields) {
        if (this.getModelAnnotation().table()) {
            return this.initTableModelTableName();
        } else {
            return this.initModelTableName();
        }
    }

    private String initTableModelTableName() {
        if (StrUtil.isNotBlank(this.getModelAnnotation().name())) {
            return this.getModelAnnotation().name();
        }
        return CommonTool.getNewStrBySqlStyle(this.getSqlStyle(), this.getMetaData().getSimpleName());
    }

    private String initModelTableName() {
        Class<?> parentTableModel = this.findParentTableModel();
        Model tableModelAnnotation = parentTableModel.getAnnotation(Model.class);
        if (StrUtil.isNotBlank(tableModelAnnotation.name())) {
            return tableModelAnnotation.name();
        }
        return CommonTool.getNewStrBySqlStyle(this.getSqlStyle(), parentTableModel.getSimpleName());
    }

    private Class<?> findParentTableModel() {
        Class<?> clazz = this.getMetaData();
        while (Objects.nonNull(clazz)) {
            Model annotation = clazz.getAnnotation(Model.class);
            if (Objects.nonNull(annotation) && annotation.table()) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        if (Objects.isNull(clazz)) {
            throw new IllegalArgumentException("未能向上查找到表实体的 Model 注解，请手动指定当前模型类表名");
        }
        return clazz;
    }

}
