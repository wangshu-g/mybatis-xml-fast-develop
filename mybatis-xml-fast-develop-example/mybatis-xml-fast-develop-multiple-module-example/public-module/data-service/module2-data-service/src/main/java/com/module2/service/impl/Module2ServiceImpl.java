package com.module2.service.impl;

import com.module2.mapper.Module2Mapper;
import com.module2.model.Module2;
import com.wangshu.base.service.AbstractBaseDataService;
import com.module2.service.Module2Service;
import jakarta.annotation.Resource;
import java.lang.Long;
import java.lang.Override;
import org.springframework.stereotype.Service;

@Service
public class Module2ServiceImpl extends AbstractBaseDataService<Long, Module2Mapper, Module2> implements Module2Service {
  @Resource
  public Module2Mapper module2Mapper;

  @Override
  public Module2Mapper getMapper() {
    return module2Mapper;
  }
}
