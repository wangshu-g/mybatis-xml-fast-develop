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
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;

public class CommonStaticField {

    public static final String XML_SUFFIX = ".xml";
    public static final String MYBATIS_XML_DOCTYPE = "-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd";
    public static final String BREAK_WRAP = "\r\n";
    public static final String SAVE_METHOD_NAME = "_save";
    public static final String BATCH_SAVE_METHOD_NAME = "_batchSave";
    public static final String DELETE_METHOD_NAME = "_delete";
    public static final String UPDATE_METHOD_NAME = "_update";
    public static final String SELECT_METHOD_NAME = "_select";
    public static final String GET_LIST_METHOD_NAME = "_getList";
    public static final String GET_NEST_LIST_METHOD_NAME = "_getNestList";
    public static final String GET_TOTAL_METHOD_NAME = "_getTotal";
    public static final String FILE_DOT = "\\.";
    public static final String JAVA_SUFFIX = ".java";
    public static final String BASE_MODEL_CLAZZ_SIMPLE_NAME = BaseModel.class.getSimpleName();
    public static final String BASE_MODEL_PACKAGE_NAME = StrUtil.concat(false, BaseModel.class.getPackageName(), FILE_DOT, BASE_MODEL_CLAZZ_SIMPLE_NAME);
    public static final String BASE_MAPPER_CLAZZ_SIMPLE_NAME = BaseDataMapper.class.getSimpleName();
    public static final String BASE_MAPPER_PACKAGE_NAME = StrUtil.concat(false, BaseDataMapper.class.getPackageName(), FILE_DOT, BASE_MAPPER_CLAZZ_SIMPLE_NAME);
    public static final String BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME = BaseDataService.class.getSimpleName();
    public static final String BASE_DATA_SERVICE_CLAZZ_PACKAGE_NAME = StrUtil.concat(false, BaseDataService.class.getPackageName(), FILE_DOT, BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME);
    public static final String ABSTRACT_BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME = AbstractBaseDataService.class.getSimpleName();
    public static final String ABSTRACT_BASE_DATA_SERVICE_PACKAGE_NAME = StrUtil.concat(false, AbstractBaseDataService.class.getPackageName(), FILE_DOT, ABSTRACT_BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME);

}
