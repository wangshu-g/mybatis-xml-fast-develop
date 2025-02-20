package com.wangshu.generate.metadata.model;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Model;
import com.wangshu.enu.DataBaseType;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;

import java.util.List;

import static com.wangshu.tool.CommonStaticField.JAVA_SUFFIX;
import static com.wangshu.tool.CommonStaticField.XML_SUFFIX;

public interface ModelInfo<T, F extends ColumnInfo<?, ?>> extends com.wangshu.generate.metadata.model.Model {

    ModuleInfo getModuleInfo();

    T getMetaData();

    Model getDataAnnotation();

    default DataBaseType getDataBaseType() {
        return this.getDataAnnotation().dataBaseType();
    }

    default String getModelDefaultKeyword() {
        return this.getDataAnnotation().modelDefaultKeyword();
    }

    default String getTableName() {
        return this.getDataAnnotation().table();
    }

    default String getModelTitle() {
        return this.getDataAnnotation().title();
    }

    String getModelName();

    String getModelFullName();

    default String getModelPackageName() {
        return this.getModelFullName().replace(StrUtil.concat(false, ".", this.getModelName()), "");
    }

    List<F> getFields();

    default List<F> getBaseFields() {
        return this.getFields().stream().filter(item -> item.isBaseField()).toList();
    }

    default List<F> getJoinFields() {
        return this.getFields().stream().filter(item -> item.isJoinField()).toList();
    }

    default List<F> getClazzJoinFields() {
        return this.getFields().stream().filter(item -> item.isClassJoinField()).toList();
    }

    default List<F> getCollectionJoinFields() {
        return this.getFields().stream().filter(item -> item.isCollectionJoinField()).toList();
    }

    default List<F> getKeyWordFields() {
        return this.getFields().stream().filter(item -> item.isKeywordField()).toList();
    }

    default F getPrimaryField() {
        return this.getBaseFields().stream().filter(ColumnInfo::isPrimaryField).findFirst().orElse(null);
    }

    default F getDefaultModelKeyWordField() {
        return this.getFieldByName(this.getModelDefaultKeyword());
    }

    default F getFieldByName(String name) {
        return this.getBaseFields().stream().filter(item -> StrUtil.equals(item.getName(), name)).findFirst().orElse(null);
    }

    default String getXmlName() {
        return this.getMapperName();
    }

    default String getMapperName() {
        return StrUtil.concat(false, this.getModelName(), "Mapper");
    }

    default String getMapperFullName() {
        return this.getModelFullName().replace(StrUtil.concat(false, "model.", this.getModelName()), StrUtil.concat(false, "mapper.", this.getMapperName()));
    }

    default String getMapperPackageName() {
        return this.getMapperFullName().replace(StrUtil.concat(false, ".", this.getMapperName()), "");
    }

    default String getServiceName() {
        return StrUtil.concat(false, this.getModelName(), "Service");
    }

    default String getServiceFullName() {
        return this.getModelFullName().replace(StrUtil.concat(false, "model.", this.getModelName()), StrUtil.concat(false, "service.", this.getServiceName()));
    }

    default String getServicePackageName() {
        return this.getServiceFullName().replace(StrUtil.concat(false, ".", this.getServiceName()), "");
    }

    default String getServiceImplName() {
        return StrUtil.concat(false, this.getModelName(), "ServiceImpl");
    }

    default String getServiceImplFullName() {
        return this.getModelFullName().replace(StrUtil.concat(false, "model.", this.getModelName()), StrUtil.concat(false, "service.impl.", this.getServiceImplName()));
    }

    default String getServiceImplPackageName() {
        return this.getServiceImplFullName().replace(StrUtil.concat(false, ".", this.getServiceImplName()), "");
    }

    default String getControllerName() {
        return StrUtil.concat(false, this.getModelName(), "Controller");
    }

    default String getControllerFullName() {
        return this.getModelFullName().replace(StrUtil.concat(false, "model.", this.getModelName()), StrUtil.concat(false, "controller.", this.getControllerName()));
    }

    default String getControllerPackageName() {
        return this.getControllerFullName().replace(StrUtil.concat(false, ".", this.getControllerName()), "");
    }

    default String getApiSave() {
        return StrUtil.concat(false, "/", this.getModelName(), "/save");
    }

    default String getApiUpdate() {
        return StrUtil.concat(false, "/", this.getModelName(), "/update");
    }

    default String getApiSelect() {
        return StrUtil.concat(false, "/", this.getModelName(), "/select");
    }

    default String getApiDelete() {
        return StrUtil.concat(false, "/", this.getModelName(), "/delete");
    }

    default String getApiList() {
        return StrUtil.concat(false, "/", this.getModelName(), "/getList");
    }

    default String getApiNestList() {
        return StrUtil.concat(false, "/", this.getModelName(), "/getNestList");
    }

    default String getApiExport() {
        return StrUtil.concat(false, "/", this.getModelName(), "/exportExcel");
    }

    default String getApiImport() {
        return StrUtil.concat(false, "/", this.getModelName(), "/importExcel");
    }

    default String getModelFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleModelPath(), this.getModelName(), JAVA_SUFFIX);
    }

    default String getMapperFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleMapperPath(), this.getMapperName(), JAVA_SUFFIX);
    }

    default String getServiceFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleServicePath(), this.getServiceName(), JAVA_SUFFIX);
    }

    default String getServiceImplFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleServiceImplPath(), this.getServiceImplName(), JAVA_SUFFIX);
    }

    default String getControllerFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleControllerPath(), this.getControllerName(), JAVA_SUFFIX);
    }

    default String getXmlFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleXmlPath(), this.getMapperName(), XML_SUFFIX);
    }

    default String getGenerateModelFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleGenerateModelPath(), this.getModelName(), JAVA_SUFFIX);
    }

    default String getGenerateMapperFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleGenerateMapperPath(), this.getMapperName(), JAVA_SUFFIX);
    }

    default String getGenerateServiceFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleGenerateServicePath(), this.getServiceName(), JAVA_SUFFIX);
    }

    default String getGenerateServiceImplFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleGenerateServiceImplPath(), this.getServiceImplName(), JAVA_SUFFIX);
    }

    default String getGenerateControllerFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleGenerateControllerPath(), this.getControllerName(), JAVA_SUFFIX);
    }

    default String getGenerateXmlFilePath() {
        return StrUtil.concat(false, this.getModuleInfo().getModuleGenerateXmlPath(), this.getMapperName(), XML_SUFFIX);
    }

}
