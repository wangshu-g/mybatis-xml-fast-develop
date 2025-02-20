package com.wangshu.generate.java;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.squareup.javapoet.*;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.annotation.Model;
import com.wangshu.base.controller.AbstractBaseDataControllerString;
import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.enu.Condition;
import com.wangshu.enu.JoinCondition;
import com.wangshu.enu.JoinType;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.GenerateJavaUtil;
import jakarta.annotation.Resource;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
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

@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class GenerateJavaMMSC<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateJava {

    private T model;
    private Consumer<MessageException> message;
    private Class<? extends BaseDataController> controllerSuperClazz = AbstractBaseDataControllerString.class;

    private String modelCode;
    private String mapperCode;
    private String serviceCode;
    private String serviceImplCode;
    private String controllerCode;

    public GenerateJavaMMSC(T model, Consumer<MessageException> message) {
        this.model = model;
        this.message = message;
    }

    public GenerateJavaMMSC(T model) {
        this.model = model;
    }

    public String generateModelClass() throws IOException {
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.getModel().getModelName()).addModifiers(Modifier.PUBLIC).superclass(BaseModel.class);

        AnnotationSpec dataAnnotation = GenerateJavaUtil.generateAnnotationSpec(Model.class);
        typeSpec.addAnnotation(dataAnnotation);

        AnnotationSpec lombokDataAnnotation = GenerateJavaUtil.generateAnnotationSpec(lombok.Data.class);
        typeSpec.addAnnotation(lombokDataAnnotation);

        AnnotationSpec.Builder accessorsAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Accessors.class);
        accessorsAnnotation.addMember("chain", "$L", true);

        typeSpec.addAnnotation(accessorsAnnotation.build());

        AnnotationSpec.Builder equalsAndHashCodeAnnotation = GenerateJavaUtil.generateAnnotationBuilder(EqualsAndHashCode.class);
        equalsAndHashCodeAnnotation.addMember("callSuper", "$L", true);

        typeSpec.addAnnotation(equalsAndHashCodeAnnotation.build());

        for (F item : this.getModel().getFields()) {
            FieldSpec.Builder fieldSpec = null;
            if (item.isBaseField()) {
                try {
                    Class<?> clazz = Class.forName(item.getJavaTypeName());
                    fieldSpec = GenerateJavaUtil.generateFieldBuilder(TypeName.get(clazz), item.getName(), Modifier.PRIVATE);
                    AnnotationSpec.Builder columnAnnotation = GenerateJavaUtil.generateAnnotationBuilder(Column.class);
                    columnAnnotation.addMember("title", "$S", item.getTitle());
                    columnAnnotation.addMember("comment", "$S", item.getComment());
                    columnAnnotation.addMember("conditions", "$T.all", Condition.class);
                    columnAnnotation.addMember("primary", "$L", item.isPrimaryField());
                    fieldSpec.addAnnotation(columnAnnotation.build());
                } catch (ClassNotFoundException e) {
                    this.printError(StrUtil.concat(false, "加载类: ", item.getJavaTypeName(), " 失败"));
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
            if (Objects.nonNull(fieldSpec)) {
                typeSpec.addField(fieldSpec.build());
            }
        }

        return GenerateJavaUtil.getJavaCode(this.getModel().getModelPackageName(), typeSpec.build());
    }

    public String generateMapperInterface() throws IOException {
        TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(this.getModel().getMapperName()).addModifiers(Modifier.PUBLIC);

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(BaseDataMapper.class, BaseModel.class);
        typeSpec.addSuperinterface(parameterizedTypeName);

        AnnotationSpec mapperAnnotation = GenerateJavaUtil.generateAnnotationSpec(Mapper.class);
        typeSpec.addAnnotation(mapperAnnotation);

        return GenerateJavaUtil.getJavaCode(this.getModel().getMapperPackageName(), typeSpec.build());
    }

    public String generateServiceClass() throws IOException, ClassNotFoundException {

        Class<?> primaryFieldClazz = Class.forName(this.getModel().getPrimaryField().getJavaTypeName());

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(BaseDataService.class, primaryFieldClazz, BaseDataMapper.class, BaseModel.class);
        TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(this.getModel().getServiceName()).addModifiers(Modifier.PUBLIC).addSuperinterface(parameterizedTypeName);

        return GenerateJavaUtil.getJavaCode(this.getModel().getServicePackageName(), typeSpec.build());
    }

    public String generateServiceImplClass() throws ClassNotFoundException, IOException {
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

        return GenerateJavaUtil.getJavaCode(this.getModel().getServiceImplPackageName(), typeSpec.build())
                .replaceAll(BASE_MAPPER_PACKAGE_NAME, this.getModel().getMapperFullName())
                .replaceAll(BASE_MAPPER_CLAZZ_SIMPLE_NAME, this.getModel().getMapperName())
                .replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName())
                .replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName())
                .replaceAll(BASE_DATA_SERVICE_CLAZZ_PACKAGE_NAME, this.getModel().getServiceFullName())
                .replaceAll(StrUtil.concat(false, "implements ", BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME), StrUtil.concat(false, "implements ", this.getModel().getServiceName()));
    }

    public String generateControllerClass() throws IOException {
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

        return GenerateJavaUtil.getJavaCode(this.getModel().getControllerPackageName(), typeSpec.build())
                .replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName())
                .replaceAll(ABSTRACT_BASE_DATA_SERVICE_PACKAGE_NAME, this.getModel().getServiceFullName())
                .replaceAll(ABSTRACT_BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME, this.getModel().getServiceName())
                .replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName());
    }

    @Override
    public Consumer<MessageException> getMessage() {
        return this.message;
    }

    public boolean writeModel(String path) {
        if (StrUtil.isBlank(this.getModelCode())) {
            this.generateModelCode();
        }
        if (StrUtil.isBlank(this.getModelCode())) {
            return false;
        }
        File file = FileUtil.touch(path);
        file.deleteOnExit();
        FileUtil.writeString(this.getModelCode(), file, StandardCharsets.UTF_8);
        return true;
    }

    public boolean writeMapper(String path) {
        if (StrUtil.isBlank(this.getMapperCode())) {
            this.generateMapperCode();
        }
        if (StrUtil.isBlank(this.getMapperCode())) {
            return false;
        }
        File file = FileUtil.touch(path);
        file.deleteOnExit();
        FileUtil.writeString(this.getMapperCode(), file, StandardCharsets.UTF_8);
        return true;
    }

    public boolean writeService(String path) {
        if (StrUtil.isBlank(this.getServiceCode())) {
            this.generateServiceCode();
        }
        if (StrUtil.isBlank(this.getServiceCode())) {
            return false;
        }
        File file = FileUtil.touch(path);
        file.deleteOnExit();
        FileUtil.writeString(this.getServiceCode(), file, StandardCharsets.UTF_8);
        return true;
    }

    public boolean writeServiceImpl(String path) {
        if (StrUtil.isBlank(this.getServiceImplCode())) {
            this.generateServiceImplCode();
        }
        if (StrUtil.isBlank(this.getServiceImplCode())) {
            return false;
        }
        File file = FileUtil.touch(path);
        file.deleteOnExit();
        FileUtil.writeString(this.getServiceImplCode(), file, StandardCharsets.UTF_8);
        return true;
    }

    public boolean writeController(String path) {
        if (StrUtil.isBlank(this.getControllerCode())) {
            this.generateControllerCode();
        }
        if (StrUtil.isBlank(this.getControllerCode())) {
            return false;
        }
        File file = FileUtil.touch(path);
        file.deleteOnExit();
        FileUtil.writeString(this.getControllerCode(), file, StandardCharsets.UTF_8);
        return true;
    }

    public String generateModelCode() {
        try {
            String modelCode = this.generateModelClass()
                    .replaceAll(StrUtil.concat(false, "package ", this.getModel().getModelFullName()), StrUtil.concat(false, "package ", this.getModel().getModelPackageName()));
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
            return modelCode;
        } catch (IOException e) {
            this.printError(StrUtil.concat(false, "写入model失败:", this.getModel().getModelName(), ",失败原因: ", e.getMessage()), e);
        }
        return null;
    }

    public String generateMapperCode() {
        try {
            String mapperCode = this.generateMapperInterface()
                    .replaceAll(StrUtil.concat(false, "package ", this.getModel().getMapperFullName()), StrUtil.concat(false, "package ", this.getModel().getMapperPackageName()))
                    .replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName()).replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName());
            this.setMapperCode(mapperCode);
            return mapperCode;
        } catch (IOException e) {
            this.printError(StrUtil.concat(false, "获取mapper失败,对应的model类是:", this.getModel().getModelName(), ",失败原因: ", e.getMessage()), e);
        }
        return null;
    }

    public String generateServiceCode() {
        try {
            String serviceCode = this.generateServiceClass()
                    .replaceAll(StrUtil.concat(false, "package ", this.getModel().getServiceFullName()), StrUtil.concat(false, "package ", this.getModel().getServicePackageName()))
                    .replaceAll(BASE_MAPPER_PACKAGE_NAME, this.getModel().getMapperFullName()).replaceAll(BASE_MAPPER_CLAZZ_SIMPLE_NAME, this.getModel().getMapperName())
                    .replaceAll(BASE_MODEL_PACKAGE_NAME, this.getModel().getModelFullName()).replaceAll(BASE_MODEL_CLAZZ_SIMPLE_NAME, this.getModel().getModelName());
            this.setServiceCode(serviceCode);
            return serviceCode;
        } catch (IOException e) {
            this.printError(StrUtil.concat(false, "获取service失败,对应的model类是:", this.getModel().getModelName(), ",失败原因: ", e.getMessage()), e);
        } catch (ClassNotFoundException e) {
            this.printError(StrUtil.concat(false, "主键类型异常,对应的model类是:", this.getModel().getModelName()));
            this.printError(StrUtil.concat(false, "获取service失败,对应的model类是:", this.getModel().getModelName(), ",失败原因: ", e.getMessage()), e);
        }
        return null;
    }

    public String generateServiceImplCode() {
        try {
            String serviceImplCode = this.generateServiceImplClass();
            this.setServiceImplCode(serviceImplCode);
            return serviceImplCode;
        } catch (IOException e) {
            this.printError(StrUtil.concat(false, "获取serviceImpl失败,对应的model类是:", this.getModel().getModelName(), ",失败原因: ", e.getMessage()), e);
        } catch (ClassNotFoundException e) {
            this.printError(StrUtil.concat(false, "主键类型异常,对应的model类是:", this.getModel().getModelName()));
            this.printError(StrUtil.concat(false, "获取serviceImpl失败,对应的model类是:", this.getModel().getModelName(), ",失败原因: ", e.getMessage()), e);
        }
        return null;
    }

    public String generateControllerCode() {
        try {
            String controllerCode = this.generateControllerClass();
            this.setControllerCode(controllerCode);
            return controllerCode;
        } catch (IOException e) {
            this.printWarn(StrUtil.concat(false, "写入controller失败,对应的model类是:", this.getModel().getModelName(), ",失败原因: ", e.getMessage()));
        }
        return null;
    }

    @Override
    public boolean writeModel() {
        return this.writeModel(this.getModel().getGenerateModelFilePath());
    }

    @Override
    public boolean writeMapper() {
        return this.writeMapper(this.getModel().getGenerateMapperFilePath());
    }

    @Override
    public boolean writeService() {
        return this.writeService(this.getModel().getGenerateServiceFilePath());
    }

    @Override
    public boolean writeServiceImpl() {
        return this.writeServiceImpl(this.getModel().getGenerateServiceImplFilePath());
    }

    @Override
    public boolean writeController() {
        return this.writeController(this.getModel().getGenerateControllerFilePath());
    }

}
