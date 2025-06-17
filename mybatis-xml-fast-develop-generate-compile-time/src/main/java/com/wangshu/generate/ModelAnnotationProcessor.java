package com.wangshu.generate;

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
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.google.auto.service.AutoService;
import com.wangshu.annotation.Model;
import com.wangshu.base.model.BaseModel;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.config.GenerateConfigCompileTime;
import com.wangshu.generate.java.GenerateJavaMMSSCQ;
import com.wangshu.generate.metadata.model.ModelClazzInfo;
import com.wangshu.generate.metadata.model.ModelElementInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.generate.metadata.module.ModuleTemplateInfo;
import com.wangshu.generate.xml.GenerateXml;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author wangshu-g
 */
@SupportedAnnotationTypes({"com.wangshu.annotation.Model", "com.wangshu.annotation.ForceTrigger"})
@AutoService(Processor.class)
public class ModelAnnotationProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;
    private Consumer<MessageException> messageExceptionConsumer;
    private String modulePath;
    private String moduleName;
    private GenerateConfigCompileTime generateJavaConfigCompileTime;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messageExceptionConsumer = (MessageException message) -> {
            switch (message.getMessageType()) {
                case ERROR:
                    printError(message.getMessage());
                    break;
                case WARN:
                    printWarn(message.getMessage());
                    break;
                case NOTE:
                    printNote(message.getMessage());
                    break;
            }
        };
        String moduleClassesPath = Objects.requireNonNull(ModelAnnotationProcessor.class.getClassLoader().getResource("")).getPath();
        modulePath = moduleClassesPath.replace("target/classes/", "");
        String[] dirNamePath = modulePath.replaceFirst("/", "").split("/");
        moduleName = dirNamePath[dirNamePath.length - 1];
        String applicationYmlFilePath = StrUtil.concat(false, moduleClassesPath, File.separator, "application.yml");
        generateJavaConfigCompileTime = new GenerateConfigCompileTime(applicationYmlFilePath);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        if (SourceVersion.latest().compareTo(SourceVersion.RELEASE_8) > 0) {
            return SourceVersion.latest();
        }
        return SourceVersion.RELEASE_8;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean process(Set<? extends TypeElement> annotations, @NotNull RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        try {
            Map<String, ModuleTemplateInfo> moduleTemplateInfoMap = new HashMap<>();
            if (generateJavaConfigCompileTime.isScanClassFile()) {
                if (StrUtil.isBlank(generateJavaConfigCompileTime.getScanClassFileModelPackage())) {
                    printError("使用 scan-class-file 必须配置 scan-class-file-model-package");
                } else {
                    for (Class<?> clazz : ClassUtil.scanPackage(generateJavaConfigCompileTime.getScanClassFileModelPackage())) {
                        if (clazz.isAnnotationPresent(Model.class) && BaseModel.class.isAssignableFrom(clazz)) {
                            String modelPackageName = this.getModelPackageName(clazz.getName());
                            String modelParentPackageName = this.getModelParentPackageName(modelPackageName);
                            String modelPackageDirName = this.getModelPackageDirName(modelPackageName);
                            ModuleTemplateInfo moduleTemplateInfo = new ModuleTemplateInfo(moduleName, modelParentPackageName, modulePath, modelPackageDirName);
                            ModelClazzInfo modelClazzInfo = new ModelClazzInfo(moduleTemplateInfo, (Class<? extends BaseModel>) clazz);
                            writeGenerateFile(modelClazzInfo, generateJavaConfigCompileTime);
                            moduleTemplateInfoMap.put(modelPackageDirName, moduleTemplateInfo);
                        }
                    }
                }
            } else {
                for (Element element : roundEnv.getElementsAnnotatedWith(Model.class)) {
                    if (element instanceof TypeElement temp) {
                        String modelPackageName = this.getModelPackageName(element.asType().toString());
                        String modelParentPackageName = this.getModelParentPackageName(modelPackageName);
                        String modelPackageDirName = this.getModelPackageDirName(modelPackageName);
                        ModuleTemplateInfo moduleTemplateInfo = new ModuleTemplateInfo(moduleName, modelParentPackageName, modulePath, modelPackageDirName);
                        ModelElementInfo modelElementInfo = new ModelElementInfo(moduleTemplateInfo, temp, typeUtils);
                        writeGenerateFile(modelElementInfo, generateJavaConfigCompileTime);
                        moduleTemplateInfoMap.put(modelPackageDirName, moduleTemplateInfo);
                    }
                }
            }
            for (ModuleTemplateInfo item : moduleTemplateInfoMap.values()) {
                copyFolderToFolder(new File(item.getModuleGeneratePath()).getAbsolutePath(), new File(item.getModulePath()).getAbsolutePath(), false);
                copyFolderToFolder(new File(item.getModuleGenerateXmlPath()).getAbsolutePath(), new File(item.getModuleXmlPath()).getAbsolutePath(), generateJavaConfigCompileTime.isForceOverwriteXml());
                copyFolderToFolder(new File(item.getModuleGenerateXmlPath()).getAbsolutePath(), new File(item.getModuleCompileClassesXmlPath()).getAbsolutePath(), generateJavaConfigCompileTime.isForceOverwriteXml());
            }
        } catch (Exception e) {
            printError("生成失败");
            printError(e.getMessage());
            return false;
        }
        return true;
    }


    private @NotNull String getModelPackageName(@NotNull String modelFullName) {
        return modelFullName.substring(0, modelFullName.lastIndexOf("."));
    }

    private @NotNull String getModelParentPackageName(@NotNull String modelPackageName) {
        return modelPackageName.substring(0, modelPackageName.lastIndexOf("."));
    }

    private @NotNull String getModelPackageDirName(@NotNull String modelPackageName) {
        String[] parts = modelPackageName.split("\\.");
        return parts[parts.length - 1];
    }

    private void writeGenerateFile(@NotNull ModelInfo<?, ?> modelInfo, GenerateConfigCompileTime generateJavaConfigCompileTime) throws IOException {
        GenerateXml generateXml = modelInfo.getGenerateXml(this.messageExceptionConsumer);
        if (Objects.isNull(generateXml)) {
            this.printError("暂未支持的数据库类型");
            return;
        }
        GenerateJavaMMSSCQ generateJava = (GenerateJavaMMSSCQ) modelInfo.getGenerateJava(generateJavaConfigCompileTime, this.messageExceptionConsumer);
        generateXml.writeXml();
        generateJava.writeJava();
        if (!FileUtil.exist(modelInfo.getMapperFilePath()) && generateJavaConfigCompileTime.isMapper()) {
            writeJavaSourceFile(modelInfo.getMapperFullName(), generateJava.getMapperCode());
        }
        if (!FileUtil.exist(modelInfo.getServiceFilePath()) && generateJavaConfigCompileTime.isService()) {
            writeJavaSourceFile(modelInfo.getServiceFullName(), generateJava.getServiceCode());
        }
        if (!FileUtil.exist(modelInfo.getServiceImplFilePath()) && generateJavaConfigCompileTime.isServiceImpl()) {
            writeJavaSourceFile(modelInfo.getServiceImplFullName(), generateJava.getServiceImplCode());
        }
        if (!FileUtil.exist(modelInfo.getControllerFilePath()) && generateJavaConfigCompileTime.isController()) {
            writeJavaSourceFile(modelInfo.getControllerFullName(), generateJava.getControllerCode());
        }
        if (!FileUtil.exist(modelInfo.getQueryFilePath()) && generateJavaConfigCompileTime.isQuery()) {
            writeJavaSourceFile(modelInfo.getQueryFullName(), generateJava.getQueryCode());
        }
    }

    private void copyFolderToFolder(String sourceDir, String targetDir, boolean overwrite) throws IOException {
        File targetFolder = new File(targetDir);
        if (!targetFolder.exists()) {
            boolean ignore = targetFolder.mkdirs();
        }
        File sourceFolder = new File(sourceDir);
        File[] files = sourceFolder.listFiles();
        if (files == null) {
            return;
        }
        for (File sourceFile : files) {
            File targetFile = new File(targetFolder, sourceFile.getName());
            if (sourceFile.isFile()) {
                if (!targetFile.exists() || overwrite) {
                    try (FileOutputStream out = new FileOutputStream(targetFile)) {
                        Files.copy(sourceFile.toPath(), out);
                    }
                }
            } else if (sourceFile.isDirectory()) {
                copyFolderToFolder(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath(), overwrite);
            }
        }
    }

    private void writeJavaSourceFile(String classFullName, String code) throws IOException {
        JavaFileObject sourceFile = filer.createSourceFile(classFullName);
        try (Writer writer = sourceFile.openWriter()) {
            writer.write(code);
        }
    }

    private void printNote(String message) {
        printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void printWarn(String message) {
        printMessage(Diagnostic.Kind.WARNING, message);
    }

    private void printError(String message) {
        printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void printMessage(Diagnostic.Kind kind, String message) {
        messager.printMessage(kind, message);
    }

}
