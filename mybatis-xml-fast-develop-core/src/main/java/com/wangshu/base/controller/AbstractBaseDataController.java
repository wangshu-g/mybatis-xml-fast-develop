package com.wangshu.base.controller;

import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractBaseDataController<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> implements BaseDataController<S, T> {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<String, Object> getRequestParams(HttpServletRequest request) {
        Map<String, Object> requestParams = BaseDataController.super.getRequestParams(request);
        log.info("请求参数: {}", requestParams);
        return requestParams;
    }

}
