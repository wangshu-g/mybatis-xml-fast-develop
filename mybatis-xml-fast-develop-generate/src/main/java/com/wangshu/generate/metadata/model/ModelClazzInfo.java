package com.wangshu.generate.metadata.model;

import com.wangshu.annotation.Column;
import com.wangshu.annotation.Model;
import com.wangshu.annotation.Join;
import com.wangshu.base.model.BaseModel;
import com.wangshu.generate.metadata.field.AbstractColumnInfo;
import com.wangshu.generate.metadata.field.ColumnFieldInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        this.setTableName(this.initTableName(moduleInfo, metaData, ignoreJoinFields));
        this.setModelTitle(modelAnnotation.title());
        this.setModelName(metaData.getSimpleName());
        this.setModelFullName(metaData.getTypeName());
        this.setModelPackageName(this.getModelFullName().replace(StrUtil.concat(false, ".", this.getModelName()), ""));
    }

    private void initFields(ModuleInfo moduleInfo, Class<? extends BaseModel> metaData, boolean ignoreJoinFields) {
        Class<?> clazz = metaData;
        List<ColumnFieldInfo> fields = new ArrayList<>();
        while (Objects.nonNull(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Objects.nonNull(field.getAnnotation(Column.class))) {
                    fields.add(new ColumnFieldInfo(field, this));
                } else if (Objects.nonNull(field.getAnnotation(Join.class)) && !ignoreJoinFields) {
                    fields.add(new ColumnFieldInfo(field, this));
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

    private String initTableName(ModuleInfo moduleInfo, @NotNull Class<? extends BaseModel> metaData, boolean ignoreJoinFields) {
        Model modelAnnotation = metaData.getAnnotation(Model.class);
        String table = null;
        if (Objects.nonNull(modelAnnotation)) {
            this.setModelAnnotation(modelAnnotation);
            this.setModelDefaultKeyword(modelAnnotation.modelDefaultKeyword());
            table = modelAnnotation.table();
        }
        if (StrUtil.isBlank(table)) {
            table = metaData.getSimpleName();
        }
        return table;
    }

}
