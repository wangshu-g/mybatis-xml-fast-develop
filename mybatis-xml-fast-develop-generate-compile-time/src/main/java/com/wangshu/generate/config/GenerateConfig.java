package com.wangshu.generate.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;

import java.util.Objects;

/**
 * @author wangshu-g
 *
 * <p>编译期配置，读取对应模块 resources 下的 application.yml 文件配置</p>
 **/
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

    /**
     * <p>
     * 和默认扫描 java 文件冲突，二者选其一（简单来说，默认是扫描未被编译的，开启这个是扫描已编译的，对于模块化分层开发会用到，解耦 model 模块）。
     * 该值为 true 时，必须指定 {@link GenerateConfig#scanClassFileModelPackage}，注意这里 {@link com.wangshu.annotation.Model} 对于包名的要求
     * </p>
     **/
    private boolean scanClassFile = false;
    /**
     * <p>你的模型务必要放在 com.xxx.model 下，{@link com.wangshu.annotation.Model}</p>
     **/
    private String scanClassFileModelPackage;
    private boolean xml = true;
    private boolean mapper = true;
    private boolean service = true;
    private boolean serviceImpl = true;
    private boolean controller = true;
    private boolean query = true;

}
