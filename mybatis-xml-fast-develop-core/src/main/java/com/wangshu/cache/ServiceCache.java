package com.wangshu.cache;

import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.tool.CommonTool;
import lombok.Data;

@Data
public class ServiceCache {

    public Class<? extends BaseModel> modelGeneric;
    public Class<? extends BaseDataMapper<? extends BaseModel>> mapperGeneric;

    public ServiceCache(Class<? extends BaseDataService> serviceClazz) {
        this.modelGeneric = CommonTool.getServiceModel(serviceClazz);
        this.mapperGeneric = CommonTool.getServiceMapper(serviceClazz);
    }

}
