package com.wangshu.base.controller;

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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author wangshu-g
 * <p>BaseController，生产环境应注意接口权限问题</p>
 */
public interface BaseController {

    default Map<String, Object> getRequestParams(@NotNull HttpServletRequest request) throws IOException {
        Map<String, Object> params = new HashMap<>();
        if (StrUtil.equals(request.getMethod(), RequestMethod.POST.name())) {
            params = JSON.parseObject(IoUtil.read(request.getInputStream(), Charset.forName(request.getCharacterEncoding())));
            if (Objects.isNull(params)) {
                params = new HashMap<>();
            }
            return params;
        } else {
            Map<String, String[]> paramMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values == null) {
                    params.put(key, null);
                } else if (values.length == 1) {
                    params.put(key, values[0]);
                } else {
                    params.put(key, values);
                }
            }
        }
        return params;
    }

    @Deprecated
    default String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    default String getUlId() {
        return UlidCreator.getUlid().toLowerCase();
    }

}
