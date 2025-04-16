package com.module1.dubbo;

import com.module1.model.Module1;
import jakarta.annotation.Nullable;

public interface Module1DubboService {

//    public interface Module1DubboService extends BaseDataService<Long, Module1> {

    public @Nullable Module1 selectOne(Long id);

}
