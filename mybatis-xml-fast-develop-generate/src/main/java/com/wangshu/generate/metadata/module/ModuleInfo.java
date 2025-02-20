package com.wangshu.generate.metadata.module;

import com.wangshu.tool.StringUtil;

import java.io.File;

import static com.wangshu.tool.CommonStaticField.FILE_DOT;

public interface ModuleInfo extends Module {

    String getModuleName();

    String getModulePackageName();

    String getModulePath();

    default String getModuleJavaPath() {
        return StringUtil.concat(this.getModulePath(), "src", File.separator, "main", File.separator, "java", File.separator);
    }

    default String getModuleResourcePath() {
        return StringUtil.concat(this.getModulePath(), "src", File.separator, "main", File.separator, "resources", File.separator);
    }

    default String getModuleCompileClassesPath() {
        return StringUtil.concat(this.getModulePath(), "target", File.separator, "classes", File.separator);
    }

    default String getModuleCompileClassesXmlPath() {
        return StringUtil.concat(getModuleCompileClassesPath(), this.getModuleName(), "-mapper", File.separator);
    }

    default String getModulePackagePath() {
        return StringUtil.concat(this.getModulePath(), "src", File.separator, "main", File.separator, "java", File.separator, this.getModulePackageName().replaceAll(FILE_DOT, StringUtil.concat("\\", File.separator)), File.separator);
    }

    default String getModuleModelPath() {
        return StringUtil.concat(this.getModulePackagePath(), "model", File.separator);
    }

    default String getModuleXmlPath() {
        return StringUtil.concat(this.getModuleResourcePath(), this.getModuleName(), "-mapper", File.separator);
    }

    default String getModuleMapperPath() {
        return StringUtil.concat(this.getModulePackagePath(), "mapper", File.separator);
    }

    default String getModuleServicePath() {
        return StringUtil.concat(this.getModulePackagePath(), "service", File.separator);
    }

    default String getModuleServiceImplPath() {
        return StringUtil.concat(this.getModulePackagePath(), "service", File.separator, "impl", File.separator);
    }

    default String getModuleControllerPath() {
        return StringUtil.concat(this.getModulePackagePath(), "controller", File.separator);
    }

    default String getModuleGeneratePath() {
        return StringUtil.concat(this.getModulePath(), "target", File.separator, "ws-generate", File.separator);
    }

    default String getModuleGenerateJavaPath() {
        return StringUtil.concat(this.getModuleGeneratePath(), "src", File.separator, "main", File.separator, "java", File.separator);
    }

    default String getModuleGenerateResourcePath() {
        return StringUtil.concat(this.getModuleGeneratePath(), "src", File.separator, "main", File.separator, "resources", File.separator);
    }

    default String getModuleGenerateCompileClassesPath() {
        return StringUtil.concat(this.getModuleGeneratePath(), "target", File.separator, "classes", File.separator);
    }

    default String getModuleGeneratePackagePath() {
        return StringUtil.concat(this.getModuleGeneratePath(), "src", File.separator, "main", File.separator, "java", File.separator, this.getModulePackageName().replaceAll(FILE_DOT, StringUtil.concat("\\", File.separator)), File.separator);
    }

    default String getModuleGenerateModelPath() {
        return StringUtil.concat(this.getModuleGeneratePackagePath(), "model", File.separator);
    }

    default String getModuleGenerateXmlPath() {
        return StringUtil.concat(this.getModuleGenerateResourcePath(), this.getModuleName(), "-mapper", File.separator);
    }

    default String getModuleGenerateMapperPath() {
        return StringUtil.concat(this.getModuleGeneratePackagePath(), "mapper", File.separator);
    }

    default String getModuleGenerateServicePath() {
        return StringUtil.concat(this.getModuleGeneratePackagePath(), "service", File.separator);
    }

    default String getModuleGenerateServiceImplPath() {
        return StringUtil.concat(this.getModuleGeneratePackagePath(), "service", File.separator, "impl", File.separator);
    }

    default String getModuleGenerateControllerPath() {
        return StringUtil.concat(this.getModuleGeneratePackagePath(), "controller", File.separator);
    }

}
