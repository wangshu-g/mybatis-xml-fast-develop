package com.wangshu.generate.java;

import com.wangshu.generate.GenerateInfo;

public abstract class GenerateJava implements GenerateInfo {

    public abstract String getModelCode();

    public abstract String getMapperCode();

    public abstract String getServiceCode();

    public abstract String getControllerCode();

    public abstract boolean writeModel();

    public abstract boolean writeMapper();

    public abstract boolean writeService();

    public abstract boolean writeServiceImpl();

    public abstract boolean writeController();

}
