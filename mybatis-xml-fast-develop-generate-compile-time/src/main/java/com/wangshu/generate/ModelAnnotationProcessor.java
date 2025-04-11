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
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.google.auto.service.AutoService;
import com.wangshu.annotation.Model;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.java.GenerateJavaMMSSCQ;
import com.wangshu.generate.metadata.field.ColumnElementInfo;
import com.wangshu.generate.metadata.model.ModelElementInfo;
import com.wangshu.generate.metadata.module.ModuleTemplateInfo;
import com.wangshu.generate.xml.GenerateXml;
import com.wangshu.generate.xml.GenerateXmlMssql;
import com.wangshu.generate.xml.GenerateXmlMysql;
import com.wangshu.generate.xml.GenerateXmlPostgresql;
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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author wangshu-g
 */
@SupportedAnnotationTypes("com.wangshu.annotation.Model")
@AutoService(Processor.class)
public class ModelAnnotationProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;

    private final URL path = ModelAnnotationProcessor.class.getClassLoader().getResource("");

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elementUtils = processingEnv.getElementUtils();
        this.typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        if (SourceVersion.latest().compareTo(SourceVersion.RELEASE_8) > 0) {
            return SourceVersion.latest();
        }
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, @NotNull RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        try {
            Consumer<MessageException> messageExceptionConsumer = (MessageException message) -> {
                switch (message.getMessageType()) {
                    case ERROR:
                        this.printError(message.getMessage());
                        break;
                    case WARN:
                        this.printWarn(message.getMessage());
                        break;
                    case NOTE:
                        this.printNote(message.getMessage());
                        break;
                }
            };
            assert this.path != null;
            String classesPath = this.path.getPath();
            String applicationYmlFilePath = StrUtil.concat(false, classesPath, File.separator, "application.yml");
            boolean xml = true;
            boolean mapper = true;
            boolean service = true;
            boolean serviceImpl = true;
            boolean controller = true;
            boolean query = true;
            if (FileUtil.exist(applicationYmlFilePath)) {
                Dict dict = YamlUtil.loadByPath(applicationYmlFilePath);
                xml = dict.get("mybatis-xml-fast-develop.generate-compile-time.xml", true);
                mapper = dict.get("mybatis-xml-fast-develop.generate-compile-time.mapper", true);
                service = dict.get("mybatis-xml-fast-develop.generate-compile-time.service", true);
                serviceImpl = dict.get("mybatis-xml-fast-develop.generate-compile-time.serviceImpl", true);
                controller = dict.get("mybatis-xml-fast-develop.generate-compile-time.controller", true);
                query = dict.get("mybatis-xml-fast-develop.generate-compile-time.query", true);
            }
            String modulePath = classesPath.replace("target/classes/", "");
            String[] dirNamePath = modulePath.replaceFirst("/", "").split("/");
            String moduleName = dirNamePath[dirNamePath.length - 1];
            ModuleTemplateInfo moduleTemplateInfo = null;
            for (Element element : roundEnv.getElementsAnnotatedWith(Model.class)) {
                if (element instanceof TypeElement temp) {
                    if (Objects.isNull(moduleTemplateInfo)) {
                        String modulePackageName = element.asType().toString().replace(StrUtil.concat(false, ".model.", element.getSimpleName().toString()), "");
                        moduleTemplateInfo = new ModuleTemplateInfo(moduleName, modulePackageName, modulePath);
                        FileUtil.del(moduleTemplateInfo.getModuleGeneratePath());
                    }
                    Model model = element.getAnnotation(Model.class);
                    if (Objects.nonNull(model)) {
                        ModelElementInfo modelElementInfo = new ModelElementInfo(moduleTemplateInfo, temp, this.typeUtils);
                        GenerateJavaMMSSCQ<ModelElementInfo, ColumnElementInfo> generateJava = new GenerateJavaMMSSCQ<>(modelElementInfo, messageExceptionConsumer);
                        GenerateXml<ModelElementInfo, ColumnElementInfo> generateXml = null;
                        switch (modelElementInfo.getDataBaseType()) {
                            case mysql -> generateXml = new GenerateXmlMysql<>(modelElementInfo, messageExceptionConsumer);
                            case postgresql -> generateXml = new GenerateXmlPostgresql<>(modelElementInfo, messageExceptionConsumer);
                            case mssql -> generateXml = new GenerateXmlMssql<>(modelElementInfo, messageExceptionConsumer);
                            default -> printError("暂未支持的数据库类型");
                        }
                        if (Objects.nonNull(generateXml)) {
                            if (!FileUtil.exist(modelElementInfo.getMapperFilePath()) && mapper) {
                                generateJava.writeMapper();
                                this.writeJavaSourceFile(modelElementInfo.getMapperFullName(), generateJava.getMapperCode());
                            }
                            if (!FileUtil.exist(modelElementInfo.getServiceFilePath()) && service) {
                                generateJava.writeService();
                                this.writeJavaSourceFile(modelElementInfo.getServiceFullName(), generateJava.getServiceCode());
                            }
                            if (!FileUtil.exist(modelElementInfo.getServiceImplFilePath()) && serviceImpl) {
                                generateJava.writeServiceImpl();
                                this.writeJavaSourceFile(modelElementInfo.getServiceImplFullName(), generateJava.getServiceImplCode());
                            }
                            if (!FileUtil.exist(modelElementInfo.getControllerFilePath()) && controller) {
                                generateJava.writeController();
                                this.writeJavaSourceFile(modelElementInfo.getControllerFullName(), generateJava.getControllerCode());
                            }
                            if (!FileUtil.exist(modelElementInfo.getQueryFilePath()) && query) {
                                generateJava.writeQuery();
                                this.writeJavaSourceFile(modelElementInfo.getQueryFullName(), generateJava.getQueryCode());
                            }
                            if (!FileUtil.exist(modelElementInfo.getXmlFilePath()) && xml) {
                                generateXml.writeXml();
                            }
                        }
                    }
                }
            }
            if (Objects.nonNull(moduleTemplateInfo)) {
                copyFolderToFolder(new File(moduleTemplateInfo.getModuleGeneratePath()).getAbsolutePath(), new File(moduleTemplateInfo.getModulePath()).getAbsolutePath(), false);
                if (!FileUtil.exist(moduleTemplateInfo.getModuleCompileClassesXmlPath())) {
                    copyFolderToFolder(new File(moduleTemplateInfo.getModuleGenerateXmlPath()).getAbsolutePath(), new File(moduleTemplateInfo.getModuleCompileClassesXmlPath()).getAbsolutePath(), false);
                }
            }
        } catch (Exception e) {
            this.printError("生成失败");
            this.printError(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void copyFolderToFolder(String folder1, String folder2, boolean coverExistFile) throws IOException {
        FileUtil.mkdir(folder2);
        File fileList = new File(folder1);
        String[] fileNameArr = fileList.list();
        File temp;
        if (Objects.isNull(fileNameArr)) {
            return;
        }
        for (String fileName : fileNameArr) {
            if (folder1.endsWith(File.separator)) {
                temp = new File(StrUtil.concat(false, folder1, fileName));
            } else {
                temp = new File(StrUtil.concat(false, folder1, File.separator, fileName));
            }
            if (temp.isFile()) {
                File file = new File(StrUtil.concat(false, folder2, File.separator, temp.getName()));
                if (!file.exists() || coverExistFile) {
                    Files.copy(Path.of(temp.getAbsolutePath()), new FileOutputStream(file));
                }
            } else if (temp.isDirectory()) {
                copyFolderToFolder(StrUtil.concat(false, folder1, File.separator, fileName), StrUtil.concat(false, folder2, File.separator, fileName), coverExistFile);
            }
        }
    }

    private void writeJavaSourceFile(String classFullName, String code) throws IOException {
        JavaFileObject sourceFile = this.filer.createSourceFile(classFullName);
        try (Writer writer = sourceFile.openWriter()) {
            writer.write(code);
        }
    }

    private void printNote(String message) {
        this.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void printWarn(String message) {
        this.printMessage(Diagnostic.Kind.WARNING, message);
    }

    private void printError(String message) {
        this.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void printMessage(Diagnostic.Kind kind, String message) {
        if (Objects.nonNull(message)) {
            this.messager.printMessage(kind, message);
        }
    }

}
