package com.test.config.request;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.test.config.request.inteceptor.LogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@ControllerAdvice
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public FastJsonConfig getFastJsonConfig() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteMapNullValue);
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteNullStringAsEmpty);
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteNullListAsEmpty);
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteNullBooleanAsFalse);
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteNullNumberAsZero);
        return fastJsonConfig;
    }

    @Bean
    public LogInterceptor getLogInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(getLogInterceptor());
    }

    @Override
    public void extendMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
//        log.info("已注册消息转换器: {}", converters.stream().map(item -> item.getClass().getTypeName()).toList());
    }

    @Override
    public void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(getFastJsonConfig());
//        log.info("注册fastJson消息转换器: {}", FastJsonHttpMessageConverter.class.getTypeName());
        converters.add(fastJsonHttpMessageConverter);
    }

}
