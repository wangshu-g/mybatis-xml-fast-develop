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
import com.wangshu.generate.metadata.field.AbstractColumnInfo;
import com.wangshu.generate.metadata.field.ColumnElementInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
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
        this.setSqlStyle(modelAnnotation.sqlStyle());
        this.setTableName(this.initTableName(moduleInfo, metaData, typeUtils, ignoreJoinFields));
        this.setModelTitle(modelAnnotation.title());
        this.setModelName(metaData.getSimpleName().toString());
        this.setModelFullName(metaData.asType().toString());
        this.setModelPackageName(this.getModelFullName().replace(StrUtil.concat(false, ".", this.getModelName()), ""));
    }

    public void initFields(ModuleInfo moduleInfo, TypeElement metaData, Types typeUtils, boolean ignoreJoinFields) {
        List<ColumnElementInfo> fields = new ArrayList<>();
        while (metaData != null) {
            for (Element enclosedElement : metaData.getEnclosedElements()) {
                if (enclosedElement instanceof VariableElement temp) {
                    if (Objects.nonNull(enclosedElement.getAnnotation(Column.class))) {
                        fields.add(new ColumnElementInfo(temp, this));
                    } else if (Objects.nonNull(enclosedElement.getAnnotation(Join.class)) && !ignoreJoinFields) {
                        fields.add(new ColumnElementInfo(temp, this));
                    }
                }
            }
            if (metaData.getSuperclass() != null) {
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

    private String initTableName(ModuleInfo moduleInfo, @NotNull Element metaData, Types typeUtils, boolean ignoreJoinFields) {
        if (Objects.nonNull(this.getModelAnnotation()) && StrUtil.isNotBlank(this.getModelAnnotation().table())) {
            return this.getModelAnnotation().table();
        }
        String table = metaData.getSimpleName().toString();
        switch (this.getSqlStyle()) {
            case sc -> table = StrUtil.toUnderlineCase(table);
            case su -> table = StrUtil.toUnderlineCase(table).toUpperCase();
            default -> table = StrUtil.lowerFirst(table);
        }
        return table;
    }

}
