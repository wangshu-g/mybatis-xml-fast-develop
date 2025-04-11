package com.wangshu.generate.metadata.module;

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

import cn.hutool.core.util.StrUtil;

import java.io.File;

import static com.wangshu.tool.CommonStaticField.FILE_DOT;

public interface ModuleInfo extends Module {

    String getModuleName();

    String getModulePackageName();

    String getModulePath();

    default String getModuleJavaPath() {
        return StrUtil.concat(false, this.getModulePath(), "src", File.separator, "main", File.separator, "java", File.separator);
    }

    default String getModuleResourcePath() {
        return StrUtil.concat(false, this.getModulePath(), "src", File.separator, "main", File.separator, "resources", File.separator);
    }

    default String getModuleCompileClassesPath() {
        return StrUtil.concat(false, this.getModulePath(), "target", File.separator, "classes", File.separator);
    }

    default String getModuleCompileClassesXmlPath() {
        return StrUtil.concat(false, getModuleCompileClassesPath(), this.getModuleName(), "-mapper", File.separator);
    }

    default String getModulePackagePath() {
        return StrUtil.concat(false, this.getModulePath(), "src", File.separator, "main", File.separator, "java", File.separator, this.getModulePackageName().replaceAll(FILE_DOT, StrUtil.concat(false, "\\", File.separator)), File.separator);
    }

    default String getModuleModelPath() {
        return StrUtil.concat(false, this.getModulePackagePath(), "model", File.separator);
    }

    default String getModuleXmlPath() {
        return StrUtil.concat(false, this.getModuleResourcePath(), this.getModuleName(), "-mapper", File.separator);
    }

    default String getModuleMapperPath() {
        return StrUtil.concat(false, this.getModulePackagePath(), "mapper", File.separator);
    }

    default String getModuleServicePath() {
        return StrUtil.concat(false, this.getModulePackagePath(), "service", File.separator);
    }

    default String getModuleServiceImplPath() {
        return StrUtil.concat(false, this.getModulePackagePath(), "service", File.separator, "impl", File.separator);
    }

    default String getModuleControllerPath() {
        return StrUtil.concat(false, this.getModulePackagePath(), "controller", File.separator);
    }

    default String getModuleQueryPath() {
        return StrUtil.concat(false, this.getModulePackagePath(), "query", File.separator);
    }

    default String getModuleGeneratePath() {
        return StrUtil.concat(false, this.getModulePath(), "target", File.separator, "mybatis-xml-fast-develop-generate", File.separator);
    }

    default String getModuleGenerateJavaPath() {
        return StrUtil.concat(false, this.getModuleGeneratePath(), "src", File.separator, "main", File.separator, "java", File.separator);
    }

    default String getModuleGenerateResourcePath() {
        return StrUtil.concat(false, this.getModuleGeneratePath(), "src", File.separator, "main", File.separator, "resources", File.separator);
    }

    default String getModuleGenerateCompileClassesPath() {
        return StrUtil.concat(false, this.getModuleGeneratePath(), "target", File.separator, "classes", File.separator);
    }

    default String getModuleGeneratePackagePath() {
        return StrUtil.concat(false, this.getModuleGeneratePath(), "src", File.separator, "main", File.separator, "java", File.separator, this.getModulePackageName().replaceAll(FILE_DOT, StrUtil.concat(false, "\\", File.separator)), File.separator);
    }

    default String getModuleGenerateModelPath() {
        return StrUtil.concat(false, this.getModuleGeneratePackagePath(), "model", File.separator);
    }

    default String getModuleGenerateXmlPath() {
        return StrUtil.concat(false, this.getModuleGenerateResourcePath(), this.getModuleName(), "-mapper", File.separator);
    }

    default String getModuleGenerateMapperPath() {
        return StrUtil.concat(false, this.getModuleGeneratePackagePath(), "mapper", File.separator);
    }

    default String getModuleGenerateServicePath() {
        return StrUtil.concat(false, this.getModuleGeneratePackagePath(), "service", File.separator);
    }

    default String getModuleGenerateServiceImplPath() {
        return StrUtil.concat(false, this.getModuleGeneratePackagePath(), "service", File.separator, "impl", File.separator);
    }

    default String getModuleGenerateControllerPath() {
        return StrUtil.concat(false, this.getModuleGeneratePackagePath(), "controller", File.separator);
    }

    default String getModuleGenerateQueryPath() {
        return StrUtil.concat(false, this.getModuleGeneratePackagePath(), "query", File.separator);
    }

}
