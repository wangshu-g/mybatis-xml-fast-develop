package com.module1.service.impl;

import com.module1.service.MyModule1Service;
import org.springframework.stereotype.Service;

@Service
public class MyModule1ServiceImpl extends Module1ServiceImpl implements MyModule1Service {

    @Override
    public void custom() {

    }

}
