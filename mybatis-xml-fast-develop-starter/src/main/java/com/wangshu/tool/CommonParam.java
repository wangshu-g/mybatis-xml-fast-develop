package com.wangshu.tool;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Model;
import com.wangshu.base.model.BaseModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author GSF
 */
@Slf4j
public class CommonParam {

    public static Class<?> mainClazz;

    public static ApplicationContext applicationContext;

    public static String contextPath;

    static {
        ApplicationHome applicationHome = new ApplicationHome();
        String path = Paths.get(URLDecoder.decode(applicationHome.getDir().getAbsolutePath(), StandardCharsets.UTF_8)).normalize().toString();
        if (StrUtil.isBlank(path)) {
            throw new RuntimeException("获取项目运行路径失败");
        }
        CommonParam.contextPath = path;
    }

    public static String getContextRunPath() {
        return CommonParam.contextPath;
    }

    @NotNull
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static Set<Class<? extends BaseModel>> modelClazz = new HashSet<>();

    public static String getUUID(boolean replace) {
        if (replace) {
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
        return UUID.randomUUID().toString();
    }

    public static @NotNull Set<Class<? extends BaseModel>> getTargetPackageModelClazz(String packagePath) throws IOException, ClassNotFoundException {
        Set<Class<? extends BaseModel>> modelClazz = new HashSet<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = StrUtil.concat(false, ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packagePath), "/**/*.class");
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (Resource resource : resources) {
            MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
            String className = reader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(className);
            if (BaseModel.class.isAssignableFrom(clazz) && Objects.nonNull(clazz.getAnnotation(Model.class))) {
                modelClazz.add((Class<? extends BaseModel>) clazz);
            }
        }
        return modelClazz;
    }

    public static @NotNull Set<Class<? extends BaseModel>> getTargetPackageModelClazz(@NotNull String[] packages) throws IOException, ClassNotFoundException {
        Set<Class<? extends BaseModel>> modelClazz = new HashSet<>();
        for (String modelPackage : packages) {
            modelClazz.addAll(getTargetPackageModelClazz(modelPackage));
        }
        return modelClazz;
    }

}
