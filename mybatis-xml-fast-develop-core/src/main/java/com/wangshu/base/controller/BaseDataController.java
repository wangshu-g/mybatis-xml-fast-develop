package com.wangshu.base.controller;

import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;

public interface BaseDataController<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseController {

    S getService();

    T getModel();

}
