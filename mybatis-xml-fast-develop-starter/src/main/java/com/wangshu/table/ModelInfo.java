package com.wangshu.table;

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
import com.wangshu.annotation.Model;
import com.wangshu.enu.DataBaseType;
import com.wangshu.enu.SqlStyle;
import com.wangshu.tool.CommonTool;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@lombok.Data
public abstract class ModelInfo {

    private List<FieldInfo> fields;
    private FieldInfo primaryField;
    private List<String> names;
    private Class<?> metadata;
    private Model modelAnnotation;
    private DataBaseType dataBaseType;
    private SqlStyle sqlStyle;
    private String tableName;
    private String modelName;
    private String modelFullName;

    public ModelInfo() {
    }

    public ModelInfo(@NotNull Class<?> clazz) {
        this.metadata = clazz;
        this.modelAnnotation = clazz.getAnnotation(Model.class);
        this.dataBaseType = modelAnnotation.dataBaseType();
        this.sqlStyle = this.modelAnnotation.sqlStyle();
        this.tableName = this.initTableName(clazz);
        this.modelName = clazz.getSimpleName();
        this.modelFullName = clazz.getTypeName();
        this.initNames(clazz);
        this.initFields(clazz);
    }

    public void initFields(Class<?> clazz) {
        List<FieldInfo> fields = new ArrayList<>();
        while (Objects.nonNull(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Objects.nonNull(field.getAnnotation(Column.class))) {
                    fields.add(new FieldInfo(this, field));
                }
            }
            clazz = clazz.getSuperclass();
        }
        this.setFields(fields);
        FieldInfo fieldInfo = this.fields.stream().filter(FieldInfo::getPrimary).findFirst().orElse(null);
        if (Objects.isNull(fieldInfo)) {
            throw new RuntimeException("未指定主键");
        }
        this.primaryField = fieldInfo;
    }

    public void initNames(@NotNull Class<?> clazz) {
        this.setNames(Stream.of(this.modelAnnotation.names()).toList());
    }

    public String initTableName(@NotNull Class<?> clazz) {
        if (StrUtil.isNotBlank(this.modelAnnotation.table())) {
            return this.modelAnnotation.table();
        }
        String table = clazz.getSimpleName();
        return CommonTool.getNewStrBySqlStyle(this.getSqlStyle(), table);
    }

}
