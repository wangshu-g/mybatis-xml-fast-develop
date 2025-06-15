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

import com.wangshu.annotation.Model;
import com.wangshu.enu.DataBaseType;
import com.wangshu.enu.SqlStyle;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.config.GenerateConfig;
import com.wangshu.generate.java.GenerateJava;
import com.wangshu.generate.java.GenerateJavaMMSSCQ;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.module.ModuleInfo;
import com.wangshu.generate.xml.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

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
    private String queryName;
    private String queryFullName;
    private String queryPackageName;

    private String apiSave;
    private String apiUpdate;
    private String apiSelect;
    private String apiDelete;
    private String apiList;
    private String apiNestList;
    private String apiStruct;
    @Deprecated
    private String apiExport;
    @Deprecated
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
        this.setQueryName(ModelInfo.super.getQueryName());
        this.setQueryFullName(ModelInfo.super.getQueryFullName());
        this.setQueryPackageName(ModelInfo.super.getQueryPackageName());
    }

    public void initApiInfo() {
        this.setApiSave(ModelInfo.super.getApiSave());
        this.setApiUpdate(ModelInfo.super.getApiUpdate());
        this.setApiSelect(ModelInfo.super.getApiSelect());
        this.setApiDelete(ModelInfo.super.getApiDelete());
        this.setApiList(ModelInfo.super.getApiList());
        this.setApiNestList(ModelInfo.super.getApiNestList());
        this.setApiStruct(ModelInfo.super.getApiStruct());
        this.setApiExport(ModelInfo.super.getApiExport());
        this.setApiImport(ModelInfo.super.getApiImport());
    }

    public @NotNull GenerateJava getGenerateJava(@Nullable GenerateConfig generateConfig, @Nullable Consumer<MessageException> messageExceptionConsumer) {
        return new GenerateJavaMMSSCQ(this, generateConfig, messageExceptionConsumer);
    }

    public @Nullable GenerateXml<? extends ModelInfo, ? extends ColumnInfo> getGenerateXml(@Nullable Consumer<MessageException> messageExceptionConsumer) {
        GenerateXml<? extends ModelInfo, ? extends ColumnInfo> generateXml = null;
        switch (this.getDataBaseType()) {
            case oracle -> generateXml = new GenerateXmlOracle<>((ModelInfo) this, messageExceptionConsumer);
            case mssql -> generateXml = new GenerateXmlMssql<>((ModelInfo) this, messageExceptionConsumer);
            case postgresql -> generateXml = new GenerateXmlPostgresql<>((ModelInfo) this, messageExceptionConsumer);
            case mysql -> generateXml = new GenerateXmlMysql<>((ModelInfo) this, messageExceptionConsumer);
            case mariadb -> generateXml = new GenerateXmlMariadb<>((ModelInfo) this, messageExceptionConsumer);
            case dameng -> generateXml = new GenerateXmlDameng<>((ModelInfo) this, messageExceptionConsumer);
        }
        return generateXml;
    }

}
