package com.module2.service.impl;

import com.module1.dubbo.Module1DubboService;
import com.module2.model.Module2;
import com.module2.service.MyModule2Service;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MyModule2ServiceImpl extends Module2ServiceImpl implements MyModule2Service {

    @DubboReference
    private Module1DubboService module1DubboService;

    @Override
    public Module2 custom() {
        Module2 module2 = this._select(1L);
        if (Objects.nonNull(module2)) {
            module2.setModule1(module1DubboService.selectOne(1L));
        }
        return module2;
    }

}
