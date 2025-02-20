package com.wangshu.base.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.wangshu.exception.IException;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author GSF
 * <p>BaseController</p>
 */
public interface BaseController {

    default Map<String, Object> getRequestParams(@NotNull HttpServletRequest request) throws IOException {
        if (StrUtil.equals(request.getMethod(), RequestMethod.POST.name())) {
            return JSON.parseObject(IoUtil.readUtf8(request.getInputStream()));
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    default String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
