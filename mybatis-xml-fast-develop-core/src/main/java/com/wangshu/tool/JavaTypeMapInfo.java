package com.wangshu.tool;

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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaTypeMapInfo {

    private static final Map<String, String> JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME = new HashMap<>();

    public static String getNameBySimpleName(String simpleName) throws IllegalArgumentException {
        String name = JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.get(simpleName);
        if (StrUtil.isBlank(name)) {
            throw new IllegalArgumentException("Unsupported SimpleName: " + simpleName);
        } else {
            return name;
        }
    }

    static {
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(String.class.getSimpleName(), String.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Long.class.getSimpleName(), Long.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(long.class.getSimpleName(), long.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Integer.class.getSimpleName(), Integer.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(int.class.getSimpleName(), int.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(BigDecimal.class.getSimpleName(), BigDecimal.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Short.class.getSimpleName(), Short.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(short.class.getSimpleName(), short.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Float.class.getSimpleName(), Float.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(float.class.getSimpleName(), float.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Double.class.getSimpleName(), Double.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(double.class.getSimpleName(), double.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Character.class.getSimpleName(), Character.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(char.class.getSimpleName(), char.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Boolean.class.getSimpleName(), Boolean.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(boolean.class.getSimpleName(), boolean.class.getName());
        JAVA_TYPE_SIMPLE_NAME_MAP_JAVA_TYPE_NAME.put(Date.class.getSimpleName(), Date.class.getName());
    }

}