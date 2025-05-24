package com.wangshu;

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

import com.wangshu.annotation.EnableConfig;
import com.wangshu.exception.IExceptionHandler;
import com.wangshu.tool.CommonParam;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class ConfigRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata, @NotNull BeanDefinitionRegistry registry) {
        try {
            Class<?> clazz = Class.forName(importingClassMetadata.getClassName());
            CommonParam.mainClazz = clazz;
            EnableConfig enableConfig = clazz.getAnnotation(EnableConfig.class);
            if (enableConfig.enableExceptionHandle()) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(IExceptionHandler.class);
                registry.registerBeanDefinition("iExceptionHandler", builder.getBeanDefinition());
            }
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ConfigManager.class);
            builder.setLazyInit(false);
            registry.registerBeanDefinition("configManager", builder.getBeanDefinition());
        } catch (ClassNotFoundException e) {
            log.error("获取启动类型失败");
            throw new RuntimeException(e);
        }
    }

}
