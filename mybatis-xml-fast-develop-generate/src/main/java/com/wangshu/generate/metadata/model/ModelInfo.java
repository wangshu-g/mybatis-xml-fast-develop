package com.wangshu.generate.metadata.model;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Data;
import com.wangshu.enu.DataBaseType;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;
import com.wangshu.tool.StringUtil;

import java.util.List;

import static com.wangshu.tool.CommonStaticField.JAVA_SUFFIX;
import static com.wangshu.tool.CommonStaticField.XML_SUFFIX;

public interface ModelInfo<T, F extends ColumnInfo<?, ?>> extends Model {

    ModuleInfo getModuleInfo();

    T getMetaData();

    Data getDataAnnotation();

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
        return this.getModelFullName().replace(StringUtil.concat(".", this.getModelName()), "");
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
        return StringUtil.concat(this.getModelName(), "Mapper");
    }

    default String getMapperFullName() {
        return this.getModelFullName().replace(StringUtil.concat("model.", this.getModelName()), StringUtil.concat("mapper.", this.getMapperName()));
    }

    default String getMapperPackageName() {
        return this.getMapperFullName().replace(StringUtil.concat(".", this.getMapperName()), "");
    }

    default String getServiceName() {
        return StringUtil.concat(this.getModelName(), "Service");
    }

    default String getServiceFullName() {
        return this.getModelFullName().replace(StringUtil.concat("model.", this.getModelName()), StringUtil.concat("service.", this.getServiceName()));
    }

    default String getServicePackageName() {
        return this.getServiceFullName().replace(StringUtil.concat(".", this.getServiceName()), "");
    }

    default String getServiceImplName() {
        return StringUtil.concat(this.getModelName(), "ServiceImpl");
    }

    default String getServiceImplFullName() {
        return this.getModelFullName().replace(StringUtil.concat("model.", this.getModelName()), StringUtil.concat("service.impl.", this.getServiceImplName()));
    }

    default String getServiceImplPackageName() {
        return this.getServiceImplFullName().replace(StringUtil.concat(".", this.getServiceImplName()), "");
    }

    default String getControllerName() {
        return StringUtil.concat(this.getModelName(), "Controller");
    }

    default String getControllerFullName() {
        return this.getModelFullName().replace(StringUtil.concat("model.", this.getModelName()), StringUtil.concat("controller.", this.getControllerName()));
    }

    default String getControllerPackageName() {
        return this.getControllerFullName().replace(StringUtil.concat(".", this.getControllerName()), "");
    }

    default String getApiSave() {
        return StringUtil.concat("/", this.getModelName(), "/save");
    }

    default String getApiUpdate() {
        return StringUtil.concat("/", this.getModelName(), "/update");
    }

    default String getApiSelect() {
        return StringUtil.concat("/", this.getModelName(), "/select");
    }

    default String getApiDelete() {
        return StringUtil.concat("/", this.getModelName(), "/delete");
    }

    default String getApiList() {
        return StringUtil.concat("/", this.getModelName(), "/getList");
    }

    default String getApiNestList() {
        return StringUtil.concat("/", this.getModelName(), "/getNestList");
    }

    default String getApiExport() {
        return StringUtil.concat("/", this.getModelName(), "/exportExcel");
    }

    default String getApiImport() {
        return StringUtil.concat("/", this.getModelName(), "/importExcel");
    }

    default String getModelFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleModelPath(), this.getModelName(), JAVA_SUFFIX);
    }

    default String getMapperFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleMapperPath(), this.getMapperName(), JAVA_SUFFIX);
    }

    default String getServiceFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleServicePath(), this.getServiceName(), JAVA_SUFFIX);
    }

    default String getServiceImplFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleServiceImplPath(), this.getServiceImplName(), JAVA_SUFFIX);
    }

    default String getControllerFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleControllerPath(), this.getControllerName(), JAVA_SUFFIX);
    }

    default String getXmlFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleXmlPath(), this.getMapperName(), XML_SUFFIX);
    }

    default String getGenerateModelFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleGenerateModelPath(), this.getModelName(), JAVA_SUFFIX);
    }

    default String getGenerateMapperFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleGenerateMapperPath(), this.getMapperName(), JAVA_SUFFIX);
    }

    default String getGenerateServiceFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleGenerateServicePath(), this.getServiceName(), JAVA_SUFFIX);
    }

    default String getGenerateServiceImplFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleGenerateServiceImplPath(), this.getServiceImplName(), JAVA_SUFFIX);
    }

    default String getGenerateControllerFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleGenerateControllerPath(), this.getControllerName(), JAVA_SUFFIX);
    }

    default String getGenerateXmlFilePath() {
        return StringUtil.concat(this.getModuleInfo().getModuleGenerateXmlPath(), this.getMapperName(), XML_SUFFIX);
    }

}
