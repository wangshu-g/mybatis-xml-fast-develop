package com.wangshu.cache;

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
import com.wangshu.enu.Condition;
import com.wangshu.tool.CommonTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@lombok.Data
public class ModelCache {

    public List<Field> fields;
    public Map<String, Field> fieldsMap;
    public List<Field> baseFields;
    public Field primaryField;
    public List<ColumnType> columnTypes;
    /**
     * <p>用于可能的${orderColumn}注入的安全校验</p>
     **/
    public List<String> orderColumnPossibleParameterName;
    /**
     * <p>用于删除方法中不存在有效删除参数时出现危险sql的校验</p>
     **/
    public List<String> deleteMethodPossibleWhereParameterName;
    /**
     * <p>用于更新方法中不存在有效删除参数时出现危险sql的校验</p>
     **/
    public List<String> updateMethodPossibleWhereParameterName;

    public ModelCache(Class<? extends BaseModel> modelClazz) {
        this.fields = CommonTool.getClazzFields(modelClazz);
        this.fieldsMap = this.fields.stream().collect(Collectors.toMap(Field::getName, value -> value));
        this.baseFields = this.fields.stream().filter(field -> Objects.nonNull(field.getAnnotation(Column.class))).toList();
        this.primaryField = modelPrimaryField();
        this.columnTypes = modelColumnType(modelClazz);
        this.orderColumnPossibleParameterName = orderColumnPossibleParameterName(modelClazz);
        this.deleteMethodPossibleWhereParameterName = deleteMethodPossibleWhereParameterName(modelClazz);
        this.updateMethodPossibleWhereParameterName = this.deleteMethodPossibleWhereParameterName;
    }

    private @Nullable Field modelPrimaryField() {
        List<Field> list = this.baseFields.stream().filter(field -> {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (Objects.nonNull(columnAnnotation)) {
                return columnAnnotation.primary();
            }
            return false;
        }).toList();
        return list.isEmpty() ? null : list.getFirst();
    }

    private @NotNull List<ColumnType> modelColumnType(@NotNull Class<? extends BaseModel> modelClazz) {
        List<ColumnType> columnTypes = new ArrayList<>();
        Model annotation = modelClazz.getAnnotation(Model.class);
        if (Objects.nonNull(annotation)) {
            for (Field field : this.baseFields) {
                columnTypes.add(new ColumnType(field));
            }
        }
        return columnTypes;
    }

    private @NotNull List<String> orderColumnPossibleParameterName(@NotNull Class<? extends BaseModel> modelClazz) {
        List<String> orderColumnPossibleParameterName = new ArrayList<>();
        for (Field clazzField : CommonTool.getClazzFields(modelClazz)) {
            String name = clazzField.getName();
            Column column = clazzField.getAnnotation(Column.class);
            Join join = clazzField.getAnnotation(Join.class);
            if (Objects.nonNull(column)) {
                orderColumnPossibleParameterName.add(name);
            } else if (Objects.nonNull(join)) {
                String infix = join.infix();
                Class<? extends BaseModel> leftJoinClazz = joinFieldLeftJoinClazz(clazzField);
                if (Objects.nonNull(leftJoinClazz)) {
                    for (Field clazzBaseField : CommonTool.getClazzBaseFields(leftJoinClazz)) {
                        orderColumnPossibleParameterName.add(StrUtil.concat(false, name, infix, StrUtil.upperFirst(clazzBaseField.getName())));
                    }
                }
            }
        }
        return orderColumnPossibleParameterName;
    }

    private @NotNull List<String> deleteMethodPossibleWhereParameterName(@NotNull Class<? extends BaseModel> modelClazz) {
        List<String> list = new ArrayList<>();
        for (Field clazzBaseField : CommonTool.getClazzBaseFields(modelClazz)) {
            list.addAll(baseFieldPossibleWhereParameterName(clazzBaseField));
        }
        return list;
    }

    private @NotNull List<String> baseFieldPossibleWhereParameterName(@NotNull Field baseField) {
        List<String> list = new ArrayList<>();
        Column annotation = baseField.getAnnotation(Column.class);
        if (Objects.nonNull(annotation)) {
            List<Condition> conditions = Arrays.asList(annotation.conditions());
            if (conditions.getFirst().equals(Condition.all)) {
                conditions = Condition.getEntries();
            }
            String name = baseField.getName();
            for (Condition condition : conditions) {
                if (!condition.equals(Condition.all)) {
                    list.add(StrUtil.concat(false, name, StringUtils.capitalize(condition.equals(Condition.equal) ? "" : condition.name())));
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private @Nullable Class<? extends BaseModel> joinFieldLeftJoinClazz(@NotNull Field joinField) {
        Join annotation = joinField.getAnnotation(Join.class);
        Class<? extends BaseModel> clazz = null;
        if (Objects.nonNull(annotation)) {
            if (Objects.equals(annotation.leftTable(), BaseModel.class)) {
                Class<?> type = joinField.getType();
                if (BaseModel.class.isAssignableFrom(type)) {
                    clazz = (Class<? extends BaseModel>) type;
                } else if (List.class.isAssignableFrom(type)) {
                    if (joinField.getGenericType() instanceof ParameterizedType genericType) {
                        Type[] actualTypeArguments = genericType.getActualTypeArguments();
                        if (actualTypeArguments.length != 0 && BaseModel.class.isAssignableFrom((Class<?>) actualTypeArguments[0])) {
                            clazz = (Class<? extends BaseModel>) actualTypeArguments[0];
                        }
                    }
                }
            } else {
                return annotation.leftTable();
            }
        }
        return clazz;
    }

}
