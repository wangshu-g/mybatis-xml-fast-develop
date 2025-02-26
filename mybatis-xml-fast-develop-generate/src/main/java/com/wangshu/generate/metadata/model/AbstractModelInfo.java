package com.wangshu.generate.metadata.model;

import com.wangshu.annotation.Model;
import com.wangshu.enu.DataBaseType;
import com.wangshu.enu.SqlStyle;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;

import java.util.List;

@lombok.Data
public abstract class AbstractModelInfo<T, F extends ColumnInfo<?, ?>> implements ModelInfo<T, F> {

    private ModuleInfo moduleInfo;
    private T metaData;

    private Model modelAnnotation;
    private DataBaseType dataBaseType;
    private String modelDefaultKeyword;
    private SqlStyle sqlStyle;
    private String tableName;
    private String modelTitle;
    private String modelName;
    private String modelFullName;
    private String modelPackageName;

    private List<F> fields;
    private List<F> baseFields;
    private List<F> joinFields;
    private List<F> clazzJoinFields;
    private List<F> collectionJoinFields;
    private List<F> keyWordFields;
    private F defaultModelKeyWordField;
    private F primaryField;

    private String mapperName;
    private String mapperFullName;
    private String mapperPackageName;
    private String serviceName;
    private String serviceFullName;
    private String servicePackageName;
    private String serviceImplName;
    private String serviceImplFullName;
    private String serviceImplPackageName;
    private String controllerName;
    private String controllerFullName;
    private String controllerPackageName;

    private String apiSave;
    private String apiUpdate;
    private String apiSelect;
    private String apiDelete;
    private String apiList;
    private String apiNestList;
    private String apiExport;
    private String apiImport;

    public AbstractModelInfo() {

    }

    public AbstractModelInfo(ModuleInfo moduleInfo, T metaData) {
        this.moduleInfo = moduleInfo;
        this.metaData = metaData;
        this.initNameInfo();
        this.initApiInfo();
    }

    public AbstractModelInfo(ModuleInfo moduleInfo, T metaData, boolean ignoreJoinFields) {
        this.moduleInfo = moduleInfo;
        this.metaData = metaData;
        this.initNameInfo();
        this.initApiInfo();
    }

    public void initNameInfo() {
        this.setMapperName(ModelInfo.super.getMapperName());
        this.setMapperFullName(ModelInfo.super.getMapperFullName());
        this.setMapperPackageName(ModelInfo.super.getMapperPackageName());
        this.setServiceName(ModelInfo.super.getServiceName());
        this.setServiceFullName(ModelInfo.super.getServiceFullName());
        this.setServicePackageName(ModelInfo.super.getServicePackageName());
        this.setServiceImplName(ModelInfo.super.getServiceImplName());
        this.setServiceImplFullName(ModelInfo.super.getServiceImplFullName());
        this.setServiceImplPackageName(ModelInfo.super.getServiceImplPackageName());
        this.setControllerName(ModelInfo.super.getControllerName());
        this.setControllerFullName(ModelInfo.super.getControllerFullName());
        this.setControllerPackageName(ModelInfo.super.getControllerPackageName());
    }

    public void initApiInfo() {
        this.setApiSave(ModelInfo.super.getApiSave());
        this.setApiUpdate(ModelInfo.super.getApiUpdate());
        this.setApiSelect(ModelInfo.super.getApiSelect());
        this.setApiDelete(ModelInfo.super.getApiDelete());
        this.setApiList(ModelInfo.super.getApiList());
        this.setApiNestList(ModelInfo.super.getApiNestList());
        this.setApiExport(ModelInfo.super.getApiExport());
        this.setApiImport(ModelInfo.super.getApiImport());
    }

}
