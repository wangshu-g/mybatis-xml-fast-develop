package com.wangshu.base.controller;

import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;

public abstract class AbstractBaseDataController<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> implements BaseDataController<S, T> {

}
