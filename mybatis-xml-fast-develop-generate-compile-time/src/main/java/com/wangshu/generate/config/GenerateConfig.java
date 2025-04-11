package com.wangshu.generate.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;

import java.util.Objects;

@Data
public class GenerateConfig {

    public GenerateConfig(String applicationYmlFilePath) {
        if (FileUtil.exist(applicationYmlFilePath)) {
            Dict dict = YamlUtil.loadByPath(applicationYmlFilePath);
            Boolean scanClassFileValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.scan-class-file", Boolean.class);
            Boolean xmlValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.xml", Boolean.class);
            Boolean mapperValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.mapper", Boolean.class);
            Boolean serviceValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.service", Boolean.class);
            Boolean serviceImplValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.serviceImpl", Boolean.class);
            Boolean controllerValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.controller", Boolean.class);
            Boolean queryValue = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.query", Boolean.class);
            this.scanClassFile = !Objects.isNull(scanClassFileValue) && scanClassFileValue;
            this.scanClassFileModelPackage = dict.getByPath("mybatis-xml-fast-develop.generate-compile-time.scan-class-file-model-package", String.class);
            this.xml = Objects.isNull(xmlValue) || xmlValue;
            this.mapper = Objects.isNull(mapperValue) || mapperValue;
            this.service = Objects.isNull(serviceValue) || serviceValue;
            this.serviceImpl = Objects.isNull(serviceImplValue) || serviceImplValue;
            this.controller = Objects.isNull(controllerValue) || controllerValue;
            this.query = Objects.isNull(queryValue) || queryValue;
        }
    }

    private boolean scanClassFile = false;
    private String scanClassFileModelPackage;
    private boolean xml = true;
    private boolean mapper = true;
    private boolean service = true;
    private boolean serviceImpl = true;
    private boolean controller = true;
    private boolean query = true;

}
