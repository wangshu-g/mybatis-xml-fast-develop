package com.wangshu.generate.config;

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
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * @author wangshu-g
 *
 * <p>编译期配置，读取对应模块 resources 下的 application.yml 文件配置</p>
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GenerateConfigCompileTime extends GenerateConfig {

    public GenerateConfigCompileTime(String applicationYmlFilePath) {
        if (FileUtil.exist(applicationYmlFilePath)) {
            Dict dict = YamlUtil.loadByPath(applicationYmlFilePath);
            Boolean scanClassFileValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.scan-class-file", Boolean.class);
            Boolean forceOverwriteXml = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.force-overwrite-xml", Boolean.class);
            Boolean xmlValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.xml", Boolean.class);
            Boolean mapperValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.mapper", Boolean.class);
            Boolean serviceValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.service", Boolean.class);
            Boolean serviceImplValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.serviceImpl", Boolean.class);
            Boolean controllerValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.controller", Boolean.class);
            Boolean queryValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.query", Boolean.class);
            this.scanClassFile = !Objects.isNull(scanClassFileValue) && scanClassFileValue;
            this.scanClassFileModelPackage = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.scan-class-file-model-package", String.class);
            this.forceOverwriteXml = !Objects.isNull(forceOverwriteXml) && forceOverwriteXml;
            this.setXml(Objects.isNull(xmlValue) || xmlValue);
            this.setMapper(Objects.isNull(mapperValue) || mapperValue);
            this.setService(Objects.isNull(serviceValue) || serviceValue);
            this.setServiceImpl(Objects.isNull(serviceImplValue) || serviceImplValue);
            this.setController(Objects.isNull(controllerValue) || controllerValue);
            this.setQuery(Objects.isNull(queryValue) || queryValue);
        }
    }

    /**
     * <p>和默认扫描 java 文件冲突，二者选其一（简单来说，默认是扫描未被编译的，开启这个是扫描已编译的，对于模块化分层开发会用到，解耦 model 模块）。</p>
     * <p>该值为 true 时，必须指定 {@link GenerateConfigCompileTime#scanClassFileModelPackage}，注意这里 {@link com.wangshu.annotation.Model} 对于包名的要求</p>
     **/
    private boolean scanClassFile = false;
    /**
     * <p>{@link com.wangshu.annotation.Model}</p>
     **/
    private String scanClassFileModelPackage;

    /**
     * <p>强制覆盖 xml 文件</p>
     * <p>默认 false 避免覆盖被修改过的同名 xml 文件，推荐自定义 xml 文件放在 resources/mapper 文件夹下</p>
     * <p>为 true 则强制覆盖，避免每次都要删除 resources/xxx-module-mapper 中生成的 xml 文件</p>
     **/
    private boolean forceOverwriteXml = false;

}
