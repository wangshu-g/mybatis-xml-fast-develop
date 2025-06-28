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
import com.wangshu.enu.SqlStyle;
import com.wangshu.generate.metadata.field.AbstractColumnInfo;
import com.wangshu.generate.metadata.field.ColumnElementInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;
import com.wangshu.tool.CommonTool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class ModelElementInfo extends AbstractModelInfo<TypeElement, ColumnElementInfo> {

    private Types typeUtils;

    public ModelElementInfo(ModuleInfo moduleInfo, TypeElement metaData, Types typeUtils) {
        this.setModuleInfo(moduleInfo);
        this.setMetaData(metaData);
        this.setTypeUtils(typeUtils);
        this.initBaseInfo(moduleInfo, metaData, typeUtils, false);
        this.initFields(moduleInfo, metaData, typeUtils, false);
        this.initNameInfo();
        this.initApiInfo();
    }

    public ModelElementInfo(ModuleInfo moduleInfo, TypeElement metaData, Types typeUtils, boolean ignoreJoinFields) {
        this.setModuleInfo(moduleInfo);
        this.setMetaData(metaData);
        this.setTypeUtils(typeUtils);
        this.initBaseInfo(moduleInfo, metaData, typeUtils, ignoreJoinFields);
        this.initFields(moduleInfo, metaData, typeUtils, ignoreJoinFields);
        this.initNameInfo();
        this.initApiInfo();
    }

    public void initBaseInfo(ModuleInfo moduleInfo, @NotNull TypeElement metaData, Types typeUtils, boolean ignoreJoinFields) {
        Model modelAnnotation = metaData.getAnnotation(Model.class);
        this.setModelAnnotation(modelAnnotation);
        this.setDataBaseType(modelAnnotation.dataBaseType());
        this.setModelDefaultKeyword(modelAnnotation.modelDefaultKeyword());
        this.setSqlStyle(this.initSqlStyle());
        this.setTableName(this.initTableName(moduleInfo, metaData, typeUtils, ignoreJoinFields));
        this.setModelTitle(modelAnnotation.title());
        this.setModelName(metaData.getSimpleName().toString());
        this.setModelFullName(metaData.asType().toString());
        this.setModelPackageName(this.getModelFullName().replace(StrUtil.concat(false, ".", this.getModelName()), ""));
    }

    private void initFields(ModuleInfo moduleInfo, TypeElement metaData, Types typeUtils, boolean ignoreJoinFields) {
        List<ColumnElementInfo> fields = new ArrayList<>();
        DeclaredType owner = null;
        Map<String, Object> nameMap = new HashMap<>();
        while (Objects.nonNull(metaData)) {
            for (Element enclosedElement : metaData.getEnclosedElements()) {
                if (enclosedElement instanceof VariableElement temp) {
                    if (Objects.isNull(nameMap.get(temp.getSimpleName().toString()))) {
                        nameMap.put(temp.getSimpleName().toString(), new Object());
                        if (Objects.nonNull(enclosedElement.getAnnotation(Column.class))) {
                            fields.add(new ColumnElementInfo(temp, this, owner));
                        } else if (Objects.nonNull(enclosedElement.getAnnotation(Join.class)) && !ignoreJoinFields) {
                            fields.add(new ColumnElementInfo(temp, this, null));
                        }
                    }
                }
            }
            if (Objects.nonNull(metaData.getSuperclass())) {
                if (metaData.getSuperclass() instanceof DeclaredType temp) {
                    owner = temp;
                }
                metaData = (TypeElement) typeUtils.asElement(metaData.getSuperclass());
            } else {
                metaData = null;
            }
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
        if (this.getModelAnnotation().table()) {
            return this.getModelAnnotation().sqlStyle();
        }
        TypeElement parentTableModel = this.findParentTableModel();
        Model tableModelAnnotation = parentTableModel.getAnnotation(Model.class);
        return tableModelAnnotation.sqlStyle();
    }

    private String initTableName(ModuleInfo moduleInfo, @NotNull Element metaData, Types typeUtils, boolean ignoreJoinFields) {
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
        return CommonTool.getNewStrBySqlStyle(this.getSqlStyle(), this.getMetaData().getSimpleName().toString());
    }

    private String initModelTableName() {
        TypeElement parentTableModel = this.findParentTableModel();
        Model tableModelAnnotation = parentTableModel.getAnnotation(Model.class);
        if (StrUtil.isNotBlank(tableModelAnnotation.name())) {
            return tableModelAnnotation.name();
        }
        return CommonTool.getNewStrBySqlStyle(this.getSqlStyle(), parentTableModel.getSimpleName().toString());
    }

    private TypeElement findParentTableModel() {
        TypeElement metaData = this.getMetaData();
        while (Objects.nonNull(metaData)) {
            Model annotation = metaData.getAnnotation(Model.class);
            if (Objects.nonNull(annotation) && annotation.table()) {
                break;
            }
            if (Objects.nonNull(metaData.getSuperclass())) {
                metaData = (TypeElement) typeUtils.asElement(metaData.getSuperclass());
            } else {
                metaData = null;
            }
        }
        if (Objects.isNull(metaData)) {
            throw new IllegalArgumentException("未能向上查找到表实体的 Model 注解，请手动指定当前模型类表名");
        }
        return metaData;
    }

}
