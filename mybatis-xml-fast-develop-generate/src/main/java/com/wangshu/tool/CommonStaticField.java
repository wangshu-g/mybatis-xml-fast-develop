package com.wangshu.tool;

import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;

public class CommonStaticField {

    public static final String XML_SUFFIX = ".xml";
    public static final String MYBATIS_XML_DOCTYPE = "-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd";
    public static final String WRAP = "\r\n";
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
    public static final String BASE_MODEL_PACKAGE_NAME = StringUtil.concat(BaseModel.class.getPackageName(), FILE_DOT, BASE_MODEL_CLAZZ_SIMPLE_NAME);
    public static final String BASE_MAPPER_CLAZZ_SIMPLE_NAME = BaseDataMapper.class.getSimpleName();
    public static final String BASE_MAPPER_PACKAGE_NAME = StringUtil.concat(BaseDataMapper.class.getPackageName(), FILE_DOT, BASE_MAPPER_CLAZZ_SIMPLE_NAME);
    public static final String BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME = BaseDataService.class.getSimpleName();
    public static final String BASE_DATA_SERVICE_CLAZZ_PACKAGE_NAME = StringUtil.concat(BaseDataService.class.getPackageName(), FILE_DOT, BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME);
    public static final String ABSTRACT_BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME = AbstractBaseDataService.class.getSimpleName();
    public static final String ABSTRACT_BASE_DATA_SERVICE_PACKAGE_NAME = StringUtil.concat(AbstractBaseDataService.class.getPackageName(), FILE_DOT, ABSTRACT_BASE_DATA_SERVICE_CLAZZ_SIMPLE_NAME);

}
