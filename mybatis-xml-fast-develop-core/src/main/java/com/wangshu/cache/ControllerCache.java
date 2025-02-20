package com.wangshu.cache;

import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.tool.CommonTool;
import lombok.Data;

@Data
public class ControllerCache {

    public Class<? extends BaseModel> modelGeneric;
    public Class<? extends BaseDataService> serviceGeneric;

    public ControllerCache(Class<? extends BaseDataController> controllerClazz) {
        this.modelGeneric = CommonTool.getControllerModel(controllerClazz);
        this.serviceGeneric = CommonTool.getControllerService(controllerClazz);
    }

}
