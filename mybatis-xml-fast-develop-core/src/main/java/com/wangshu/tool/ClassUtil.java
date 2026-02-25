package com.wangshu.tool;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.HashSet;
import java.util.Set;

public final class ClassUtil {

    private ClassUtil() {
    }

    public static Set<Class<?>> scanPackage(String basePackage) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        Set<Class<?>> result = new HashSet<>();
        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            result.add(Class.forName(bd.getBeanClassName()));
        }
        return result;
    }

}
