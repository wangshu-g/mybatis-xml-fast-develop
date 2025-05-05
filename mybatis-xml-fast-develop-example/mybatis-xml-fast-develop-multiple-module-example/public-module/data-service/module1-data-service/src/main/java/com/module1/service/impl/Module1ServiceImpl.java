package com.module1.service.impl;

import com.module1.mapper.Module1Mapper;
import com.module1.model.Module1;
import com.module1.service.Module1Service;
import com.wangshu.base.service.AbstractBaseDataService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class Module1ServiceImpl extends AbstractBaseDataService<Long, Module1Mapper, Module1> implements Module1Service {
    @Resource
    public Module1Mapper module1Mapper;

    @Override
    public Module1Mapper getMapper() {
        return module1Mapper;
    }
}
