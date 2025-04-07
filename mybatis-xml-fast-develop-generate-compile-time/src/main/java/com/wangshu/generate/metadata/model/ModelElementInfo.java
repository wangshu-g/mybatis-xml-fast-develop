package com.wangshu.generate.metadata.model;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.annotation.Model;
import com.wangshu.enu.SqlStyle;
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
        this.setTableName(StrUtil.lowerFirst(this.initTableName(moduleInfo, metaData, typeUtils, ignoreJoinFields)));
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
            case lcc -> table = StrUtil.lowerFirst(table);
            case sc -> table = StrUtil.toUnderlineCase(table);
        }
        return table;
    }

}
