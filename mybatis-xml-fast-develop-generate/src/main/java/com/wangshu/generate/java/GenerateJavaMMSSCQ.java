package com.wangshu.generate.java;

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

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.squareup.javapoet.*;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.annotation.Model;
import com.wangshu.annotation.Primary;
import com.wangshu.base.controller.AbstractBaseDataControllerString;
import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.query.CommonQueryParam;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.enu.Condition;
import com.wangshu.enu.JoinCondition;
import com.wangshu.enu.JoinType;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.config.GenerateConfig;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.GenerateJavaUtil;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.wangshu.tool.CommonStaticField.*;

/**
 * @author wangshu-g
 *
 * <p>MMSCD model mapper service serviceImpl controller query</p>
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GenerateJavaMMSSCQ<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateJava {

    private T model;
    private GenerateConfig generateConfig;
    private Consumer<MessageException> message;
    private Class<? extends BaseDataController> controllerSuperClazz = AbstractBaseDataControllerString.class;

    private String modelCode;
    private String mapperCode;
    private String serviceCode;
    private String serviceImplCode;
    private String controllerCode;
    private String queryCode;

    public GenerateJavaMMSSCQ(@NotNull T model, @Nullable GenerateConfig generateConfig, @Nullable Consumer<MessageException> messageExceptionConsumer) {
        this.model = model;
        this.generateConfig = Objects.isNull(generateConfig) ? new GenerateConfig() : generateConfig;
        this.message = messageExceptionConsumer;
    }

    public GenerateJavaMMSSCQ(@NotNull T model, @Nullable Consumer<MessageException> messageExceptionConsumer) {
        this.model = model;
        this.generateConfig = new GenerateConfig();
        this.message = messageExceptionConsumer;
    }

    public GenerateJavaMMSSCQ(@NotNull T model) {
        this.model = model;
        this.generateConfig = new GenerateConfig();
    }

    public void generateModel() {
        String modelCode = "";

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.getModel().getModelName()).addModifiers(Modifier.PUBLIC).superclass(BaseModel.class);

        AnnotationSpec dataAnnotation = GenerateJavaUtil.generateAnnotationSpec(Model.class);
        typeSpec.addAnnotation(dataAnnotation);

        AnnotationSpec lombokDataAnnotation = GenerateJavaUtil.generateAnnotationSpec(Data.class);
        typeSpec.addAnnotation(lombokDataAnnotation);

        AnnotationSpec.Builder accessorsAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Accessors.class);
        accessorsAnnotation.addMember("chain", "$L", true);

        typeSpec.addAnnotation(accessorsAnnotation.build());

        AnnotationSpec.Builder equalsAndHashCodeAnnotation = GenerateJavaUtil.generateAnnotationBuilder(EqualsAndHashCode.class);
        equalsAndHashCodeAnnotation.addMember("callSuper", "$L", true);

        typeSpec.addAnnotation(equalsAndHashCodeAnnotation.build());

        try {
            for (F item : this.getModel().getFields()) {
                FieldSpec.Builder fieldSpec;
                if (item.isBaseField()) {
                    Class<?> clazz = Class.forName(item.getJavaTypeName());
                    fieldSpec = GenerateJavaUtil.generateFieldBuilder(TypeName.get(clazz), item.getName(), Modifier.PRIVATE);
                    AnnotationSpec.Builder columnAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Column.class);
                    columnAnnotation.addMember("title", "$S", item.getTitle());
                    columnAnnotation.addMember("comment", "$S", item.getComment());
                    columnAnnotation.addMember("conditions", "$T.all", Condition.class);
                    columnAnnotation.addMember("primary", "$L", item.isPrimaryField());
                    fieldSpec.addAnnotation(columnAnnotation.build());
                    if (item.isPrimaryField()) {
                        AnnotationSpec.Builder primaryAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Primary.class);
                        columnAnnotation.addMember("incr", "$S", item.isIncr());
                        fieldSpec.addAnnotation(primaryAnnotation.build());
                    }
                } else if (item.isCollectionJoinField()) {
                    ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(List.class, BaseModel.class);
                    fieldSpec = GenerateJavaUtil.generateFieldBuilder(parameterizedTypeName, item.getName(), Modifier.PRIVATE);
                    AnnotationSpec.Builder joinAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Join.class);
                    joinAnnotation.addMember("leftTable", "$S.class", item.getLeftModel().getModelName());
                    joinAnnotation.addMember("leftJoinField", "$S", item.getLeftJoinField());
                    joinAnnotation.addMember("leftSelectFields", "{$S}", "*");
                    joinAnnotation.addMember("rightTable", "$S.class", item.getRightModel().getModelName());
                    joinAnnotation.addMember("rightJoinField", "$S", item.getRightJoinField());
                    joinAnnotation.addMember("joinType", "$T.$L", JoinType.class, item.getJoinType().name());
                    joinAnnotation.addMember("joinCondition", "$T.$L", JoinCondition.class, item.getJoinCondition().name());
                    joinAnnotation.addMember("infix", "$S", item.getInfix());
                    fieldSpec.addAnnotation(joinAnnotation.build());
                } else {
                    fieldSpec = GenerateJavaUtil.generateFieldBuilder(TypeName.get(BaseModel.class), item.getName(), Modifier.PRIVATE);
                    AnnotationSpec.Builder joinAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Join.class);
                    joinAnnotation.addMember("leftTable", "$S.class", item.getLeftModel().getModelName());
                    joinAnnotation.addMember("leftJoinField", "$S", item.getLeftJoinField());
                    joinAnnotation.addMember("leftSelectFields", "{$S}", "*");
                    joinAnnotation.addMember("rightTable", "$S.class", item.getRightModel().getModelName());
                    joinAnnotation.addMember("rightJoinField", "$S", item.getRightJoinField());
                    joinAnnotation.addMember("joinType", "$T.$L", JoinType.class, item.getJoinType().name());
                    joinAnnotation.addMember("joinCondition", "$T.$L", JoinCondition.class, item.getJoinCondition().name());
                    joinAnnotation.addMember("infix", "$S", item.getInfix());
                    fieldSpec.addAnnotation(joinAnnotation.build());
                }
                typeSpec.addField(fieldSpec.build());
            }
            modelCode = GenerateJavaUtil.getJavaCode(this.getModel().getModelPackageName(), typeSpec.build());
            modelCode = modelCode.replaceAll(StrUtil.concat(false, "package ", this.getModel().getModelFullName()), StrUtil.concat(false, "package ", this.getModel().getModelPackageName()));
            for (F item : this.getModel().getFields()) {
                if (item.isCollectionJoinField()) {
                    modelCode = modelCode.replace(StrUtil.concat(false, "\"", item.getLeftModel().getModelName(), "\""), item.getLeftModel().getModelName());
                    modelCode = modelCode.replace(StrUtil.concat(false, "\"", item.getRightModel().getModelName(), "\""), item.getRightModel().getModelName());
                    modelCode = modelCode.replace(StrUtil.concat(false, "List<BaseModel> ", item.getName()), StrUtil.concat(false, "List<", item.getLeftModel().getModelName(), "> ", item.getName()));
                } else if (item.isClassJoinField()) {
                    modelCode = modelCode.replace(StrUtil.concat(false, "\"", item.getLeftModel().getModelName(), "\""), item.getLeftModel().getModelName());
                    modelCode = modelCode.replace(StrUtil.concat(false, "\"", item.getRightModel().getModelName(), "\""), item.getRightModel().getModelName());
                    modelCode = modelCode.replace(StrUtil.concat(false, "BaseModel ", item.getName()), StrUtil.concat(false, item.getLeftModel().getModelName(), " ", item.getName()));
                }
            }
            this.setModelCode(modelCode);
        } catch (ClassNotFoundException e) {
            this.printError("动态加载类失败", e);
        } catch (IOException e) {
            this.printError("JavaFileObject 操作异常", e);
        }
    }

    public void generateMapperInterface() {
        String mapperCode = "";

        TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(this.getModel().getMapperName()).addModifiers(Modifier.PUBLIC);

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(BaseDataMapper.class, BaseModel.class);
        typeSpec.addSuperinterface(parameterizedTypeName);

        AnnotationSpec mapperAnnotation = GenerateJavaUtil.generateAnnotationSpec(Mapper.class);
        typeSpec.addAnnotation(mapperAnnotation);

        try {
            mapperCode = GenerateJavaUtil.getJavaCode(this.getModel().getMapperPackageName(), typeSpec.build());
            mapperCode = mapperCode.replaceAll(StrUtil.concat(false, "package ", this.getModel().getMapperFullName()), StrUtil.concat(false, "package ", this.getModel().getMapperPackageName()))
                    .replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName()).replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName());
            this.setMapperCode(mapperCode);
        } catch (IOException e) {
            this.printError("JavaFileObject 操作异常", e);
        }
    }

    public void generateService() {
        String serviceCode = "";

        try {
            Class<?> primaryFieldClazz = Class.forName(this.getModel().getPrimaryField().getJavaTypeName());

            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(BaseDataService.class, primaryFieldClazz, BaseModel.class);
            TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(this.getModel().getServiceName()).addModifiers(Modifier.PUBLIC).addSuperinterface(parameterizedTypeName);

            serviceCode = GenerateJavaUtil.getJavaCode(this.getModel().getServicePackageName(), typeSpec.build());
            serviceCode = serviceCode.replaceAll(StrUtil.concat(false, "package ", this.getModel().getServiceFullName()), StrUtil.concat(false, "package ", this.getModel().getServicePackageName()))
                    .replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName()).replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName());
            this.setServiceCode(serviceCode);
        } catch (ClassNotFoundException e) {
            this.printError("动态加载类失败", e);
        } catch (IOException e) {
            this.printError("JavaFileObject 操作异常", e);
        }
    }

    public void generateServiceImpl() {
        String serviceImplCode = "";

        try {
            Class<?> primaryFieldClazz = Class.forName(this.getModel().getPrimaryField().getJavaTypeName());

            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(AbstractBaseDataService.class, primaryFieldClazz, BaseDataMapper.class, BaseModel.class);
            TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.getModel().getServiceImplName()).addModifiers(Modifier.PUBLIC).superclass(parameterizedTypeName).addSuperinterface(BaseDataService.class);

            String mapper = StrUtil.concat(false, this.getModel().getMapperName().substring(0, 1).toLowerCase(), this.getModel().getMapperName().substring(1));

            FieldSpec clazzDeclare = GenerateJavaUtil.generateFieldBuilder(TypeName.get(BaseDataMapper.class), mapper, Modifier.PUBLIC).addAnnotation(Resource.class).build();
            typeSpec.addField(clazzDeclare);

            MethodSpec getMapperMethod = GenerateJavaUtil.generateMethodBuilder("getMapper", TypeName.get(BaseDataMapper.class), Override.class, Modifier.PUBLIC).addCode(StrUtil.concat(false, "return ", mapper, ";")).build();
            typeSpec.addMethod(getMapperMethod);

            AnnotationSpec serviceAnnotation = GenerateJavaUtil.generateAnnotationSpec(Service.class);
            typeSpec.addAnnotation(serviceAnnotation);

            serviceImplCode = GenerateJavaUtil.getJavaCode(this.getModel().getServiceImplPackageName(), typeSpec.build());
            serviceImplCode = serviceImplCode.replaceAll(BASE_MAPPER_PACKAGE_NAME, this.getModel().getMapperFullName())
                    .replaceAll(BASE_MAPPER_CLAZZ_SIMPLE_NAME, this.getModel().getMapperName())
                    .replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName())
                    .replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName())
                    .replaceAll(BASE_DATA_SERVICE_CLAZZ_PACKAGE_NAME, this.getModel().getServiceFullName())
                    .replaceAll(StrUtil.concat(false, "implements ", BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME), StrUtil.concat(false, "implements ", this.getModel().getServiceName()));
            this.setServiceImplCode(serviceImplCode);
        } catch (ClassNotFoundException e) {
            this.printError("动态加载类失败", e);
        } catch (IOException e) {
            this.printError("JavaFileObject 操作异常", e);
        }
    }

    public void generateController() {
        String controllerCode = "";

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.getModel().getControllerName()).addModifiers(Modifier.PUBLIC).superclass(ParameterizedTypeName.get(controllerSuperClazz, AbstractBaseDataService.class, BaseModel.class));

        String service = StrUtil.concat(false, this.getModel().getServiceName().substring(0, 1).toLowerCase(), this.getModel().getServiceName().substring(1));

        FieldSpec clazzDeclare = GenerateJavaUtil.generateFieldBuilder(TypeName.get(AbstractBaseDataService.class), service, Modifier.PUBLIC).addAnnotation(Resource.class).build();
        typeSpec.addField(clazzDeclare);

        MethodSpec getServiceMethod = GenerateJavaUtil.generateMethodBuilder("getService", TypeName.get(AbstractBaseDataService.class), Override.class, Modifier.PUBLIC).addCode(StrUtil.concat(false, "return ", service, ";")).build();
        typeSpec.addMethod(getServiceMethod);

        MethodSpec getModelMethod = GenerateJavaUtil.generateMethodBuilder("getModel", TypeName.get(BaseModel.class), Override.class, Modifier.PUBLIC).addCode(StrUtil.concat(false, "return new ", BASE_MODEL_CLAZZ_SIMPLE_NAME, "();")).build();
        typeSpec.addMethod(getModelMethod);

        AnnotationSpec restControllerAnnotation = GenerateJavaUtil.generateAnnotationSpec(RestController.class);
        typeSpec.addAnnotation(restControllerAnnotation);

        AnnotationSpec.Builder requestMappingAnnotation = GenerateJavaUtil.generateAnnotationBuilder(RequestMapping.class);
        requestMappingAnnotation.addMember("value", "$S", StrUtil.concat(false, "/", this.getModel().getModelName()));
        typeSpec.addAnnotation(requestMappingAnnotation.build());

        try {
            controllerCode = GenerateJavaUtil.getJavaCode(this.getModel().getControllerPackageName(), typeSpec.build());
            controllerCode = controllerCode.replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName())
                    .replaceAll(ABSTRACT_BASE_DATA_SERVICE_PACKAGE_NAME, this.getModel().getServiceFullName())
                    .replaceAll(ABSTRACT_BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME, this.getModel().getServiceName())
                    .replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName());
            this.setControllerCode(controllerCode);
        } catch (IOException e) {
            this.printError("JavaFileObject 操作异常", e);
        }
    }

    private final List<Condition> nullConditionList = List.of(Condition.isNull, Condition.orIsNull, Condition.isNotNull, Condition.orIsNotNull);
    private final List<Condition> inConditionList = List.of(Condition.in, Condition.orIn);

    public void generateQuery() {
        String queryCode = "";

        TypeName booleanTypeName = TypeName.get(Boolean.class);

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(CommonQueryParam.class, BaseModel.class);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.getModel().getQueryName())
                .addModifiers(Modifier.PUBLIC)
                .superclass(parameterizedTypeName);

        AnnotationSpec lombokDataAnnotation = GenerateJavaUtil.generateAnnotationSpec(Data.class);
        typeSpec.addAnnotation(lombokDataAnnotation);

        AnnotationSpec.Builder accessorsAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Accessors.class);
        accessorsAnnotation.addMember("chain", "$L", true);

        typeSpec.addAnnotation(accessorsAnnotation.build());

        AnnotationSpec.Builder equalsAndHashCodeAnnotation = GenerateJavaUtil.generateAnnotationBuilder(EqualsAndHashCode.class);
        equalsAndHashCodeAnnotation.addMember("callSuper", "$L", true);

        typeSpec.addAnnotation(equalsAndHashCodeAnnotation.build());

        for (F item : this.getModel().getFields()) {
            if (item.isBaseField()) {
                List<Condition> conditions = item.getConditions();
                if (conditions.getFirst().equals(Condition.all)) {
                    conditions = Condition.getEntries();
                }
                for (Condition condition : conditions) {
                    try {
                        Class<?> clazz = Class.forName(item.getJavaTypeName());
                        TypeName itemTypeName = TypeName.get(clazz);
                        if (condition.equals(Condition.equal)) {
                            FieldSpec.Builder fieldSpec = GenerateJavaUtil.generateFieldBuilder(itemTypeName, item.getName(), Modifier.PRIVATE);
                            typeSpec.addField(fieldSpec.build());
                        } else {
                            if (!condition.equals(Condition.all)) {
                                String name = StrUtil.concat(false, item.getName(), StringUtils.capitalize(condition.name()));
                                FieldSpec.Builder fieldSpec;
                                if (nullConditionList.contains(condition)) {
                                    fieldSpec = GenerateJavaUtil.generateFieldBuilder(booleanTypeName, name, Modifier.PRIVATE);
                                } else if (inConditionList.contains(condition)) {
                                    ParameterizedTypeName listTypeName = ParameterizedTypeName.get(List.class, clazz);
                                    fieldSpec = GenerateJavaUtil.generateFieldBuilder(listTypeName, name, Modifier.PRIVATE);
                                } else {
                                    fieldSpec = GenerateJavaUtil.generateFieldBuilder(itemTypeName, name, Modifier.PRIVATE);
                                }
                                typeSpec.addField(fieldSpec.build());
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        this.printError(StrUtil.concat(false, "加载类: ", item.getJavaTypeName(), " 失败"));
                    }
                }
            } else {
                T leftModel = item.getLeftModel();
                List<F> baseFields = leftModel.getBaseFields();
                List<String> leftSelectFieldNames = item.getLeftSelectFieldNames();
                if (!StrUtil.equals(leftSelectFieldNames.getFirst(), "*")) {
                    baseFields = baseFields.stream().filter(field -> leftSelectFieldNames.contains(field.getName())).toList();
                }
                for (F field : baseFields) {
                    List<Condition> conditions = field.getConditions();
                    if (conditions.getFirst().equals(Condition.all)) {
                        conditions = Condition.getEntries();
                    }
                    for (Condition condition : conditions) {
                        try {
                            Class<?> clazz = Class.forName(field.getJavaTypeName());
                            TypeName fieldTypeName = TypeName.get(clazz);
                            if (condition.equals(Condition.equal)) {
                                String name = StrUtil.concat(false, item.getName(), item.getInfix(), StringUtils.capitalize(field.getName()));
                                FieldSpec.Builder fieldSpec = GenerateJavaUtil.generateFieldBuilder(fieldTypeName, name, Modifier.PRIVATE);
                                typeSpec.addField(fieldSpec.build());
                            } else {
                                if (!condition.equals(Condition.all)) {
                                    String name = StrUtil.concat(false, item.getName(), item.getInfix(), StringUtils.capitalize(field.getName()), StringUtils.capitalize(condition.name()));
                                    FieldSpec.Builder fieldSpec;
                                    if (nullConditionList.contains(condition)) {
                                        fieldSpec = GenerateJavaUtil.generateFieldBuilder(booleanTypeName, name, Modifier.PRIVATE);
                                    } else if (inConditionList.contains(condition)) {
                                        ParameterizedTypeName listTypeName = ParameterizedTypeName.get(List.class, clazz);
                                        fieldSpec = GenerateJavaUtil.generateFieldBuilder(listTypeName, name, Modifier.PRIVATE);
                                    } else {
                                        fieldSpec = GenerateJavaUtil.generateFieldBuilder(fieldTypeName, name, Modifier.PRIVATE);
                                    }
                                    typeSpec.addField(fieldSpec.build());
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            this.printError(StrUtil.concat(false, "加载类: ", item.getJavaTypeName(), " 失败"));
                        }
                    }
                }
            }
        }
        try {
            queryCode = GenerateJavaUtil.getJavaCode(this.getModel().getQueryPackageName(), typeSpec.build());
            queryCode = queryCode.replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName())
                    .replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName());
        } catch (IOException e) {
            this.printError("JavaFileObject 操作异常", e);
        }

        this.setQueryCode(queryCode);
    }

    @Override
    public Consumer<MessageException> getMessage() {
        return this.message;
    }

    @Override
    public void generate() {
        if (StrUtil.isBlank(this.getModelCode()) && this.generateConfig.isModel()) {
            this.generateModel();
        }
        if (StrUtil.isBlank(this.getMapperCode()) && this.generateConfig.isMapper()) {
            this.generateMapperInterface();
        }
        if (StrUtil.isBlank(this.getServiceCode()) && this.generateConfig.isService()) {
            this.generateService();
        }
        if (StrUtil.isBlank(this.getServiceImplCode()) && this.generateConfig.isServiceImpl()) {
            this.generateServiceImpl();
        }
        if (StrUtil.isBlank(this.getControllerCode()) && this.generateConfig.isController()) {
            this.generateController();
        }
        if (StrUtil.isBlank(this.getQueryCode()) && this.generateConfig.isQuery()) {
            this.generateQuery();
        }
    }

    @Override
    public boolean writeJava() {
        this.generate();
        try {
            if (this.generateConfig.isModel()) {
                File modelFile = FileUtil.touch(this.getModel().getGenerateModelFilePath());
                modelFile.deleteOnExit();
                FileUtil.writeString(this.getModelCode(), modelFile, StandardCharsets.UTF_8);
            }
            if (this.generateConfig.isMapper()) {
                File mapperInterfaceFile = FileUtil.touch(this.getModel().getGenerateMapperFilePath());
                mapperInterfaceFile.deleteOnExit();
                FileUtil.writeString(this.getMapperCode(), mapperInterfaceFile, StandardCharsets.UTF_8);
            }
            if (this.generateConfig.isService()) {
                File serviceFile = FileUtil.touch(this.getModel().getGenerateServiceFilePath());
                serviceFile.deleteOnExit();
                FileUtil.writeString(this.getServiceCode(), serviceFile, StandardCharsets.UTF_8);
            }
            if (this.generateConfig.isServiceImpl()) {
                File serviceImplFile = FileUtil.touch(this.getModel().getGenerateServiceImplFilePath());
                serviceImplFile.deleteOnExit();
                FileUtil.writeString(this.getServiceImplCode(), serviceImplFile, StandardCharsets.UTF_8);
            }
            if (this.generateConfig.isController()) {
                File controllerFile = FileUtil.touch(this.getModel().getGenerateControllerFilePath());
                controllerFile.deleteOnExit();
                FileUtil.writeString(this.getControllerCode(), controllerFile, StandardCharsets.UTF_8);
            }
            if (this.generateConfig.isQuery()) {
                File queryFile = FileUtil.touch(this.getModel().getGenerateQueryFilePath());
                queryFile.deleteOnExit();
                FileUtil.writeString(this.getQueryCode(), queryFile, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            this.printError("导出 java 文件异常", e);
            return false;
        }
        return true;
    }

}
