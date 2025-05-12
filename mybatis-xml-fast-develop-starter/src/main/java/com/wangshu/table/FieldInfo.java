package com.wangshu.table;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.base.model.BaseModelWithDefaultFields;
import com.wangshu.tool.CommonTool;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

@lombok.Data
public class FieldInfo {

    private ModelInfo modelInfo;
    private Field metadata;
    private String name;
    private String sqlStyleName;
    private Column columnAnnotation;
    private String comment;
    private Boolean primary;
    private Boolean autoIncrement;
    private Boolean defaultNull;
    private Integer defaultLength;
    private Type javaType;
    private String javaTypeName;
    private String dbColumnType;

    public FieldInfo(@NotNull ModelInfo modelInfo, @NotNull Field field) {
        this.modelInfo = modelInfo;
        this.metadata = field;
        this.name = field.getName();
        this.sqlStyleName = CommonTool.getNewStrBySqlStyle(modelInfo.getSqlStyle(), field.getName());
        this.columnAnnotation = field.getAnnotation(Column.class);
        this.initComment(modelInfo, field);
        this.initJavaTypeInfo(modelInfo, field);
        this.initPrimaryInfo(modelInfo, field);
        this.initDbColumnType(modelInfo, field);
        this.defaultLength = CommonTool.getDefaultLengthByDbColumnType(modelInfo.getDataBaseType(), this.getDbColumnType().toUpperCase());
    }

    public void initComment(@NotNull ModelInfo modelInfo, @NotNull Field field) {
        String comment = this.columnAnnotation.comment();
        if (StrUtil.isBlank(comment)) {
            comment = this.columnAnnotation.title();
        }
        if (StrUtil.isBlank(comment)) {
            comment = field.getName();
        }
        this.comment = comment;
    }

    public void initJavaTypeInfo(@NotNull ModelInfo modelInfo, @NotNull Field field) {
        Class<?> clazz = modelInfo.getMetadata();
        if (this.columnAnnotation.primary() && BaseModelWithDefaultFields.class.isAssignableFrom(clazz) && field.getType().equals(Object.class) && clazz.getGenericSuperclass() instanceof ParameterizedType temp) {
            this.javaType = Arrays.stream(temp.getActualTypeArguments()).toList().getFirst();
        } else {
            this.javaType = field.getType();
        }
        this.javaTypeName = this.javaType.getTypeName();
    }

    public void initPrimaryInfo(@NotNull ModelInfo modelInfo, @NotNull Field field) {
        this.primary = this.columnAnnotation.primary();
        if (this.primary) {
            this.autoIncrement = Objects.equals(this.getJavaType(), Long.class) || Objects.equals(this.getJavaType(), Integer.class);
        } else {
            this.autoIncrement = false;
        }
        this.defaultNull = !this.primary;
    }

    public void initDbColumnType(@NotNull ModelInfo modelInfo, @NotNull Field field) {
        String dbColumnType = this.columnAnnotation.dbColumnType();
        if (StrUtil.isBlank(dbColumnType)) {
            dbColumnType = CommonTool.getDbColumnTypeByJavaTypeName(modelInfo.getDataBaseType(), this.getJavaTypeName());
        }
        this.dbColumnType = dbColumnType;
    }

}
