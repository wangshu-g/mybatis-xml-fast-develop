package com.module1.dubbo.impl;

import com.module1.dubbo.Module1DubboService;
import com.module1.model.Module1;
import com.module1.service.MyModule1Service;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Objects;

@DubboService
public class Module1DubboServiceImpl implements Module1DubboService {

//public class Module1DubboServiceImpl extends MyModule1ServiceImpl implements Module1DubboService {

    @Resource
    private MyModule1Service myModule1Service;

    @Override
    public @Nullable Module1 selectOne(Long id) {
        Module1 module1 = myModule1Service._select(id);
        if (Objects.nonNull(module1)) {
            module1.setIdCard(null);
        }
        return module1;
    }

}