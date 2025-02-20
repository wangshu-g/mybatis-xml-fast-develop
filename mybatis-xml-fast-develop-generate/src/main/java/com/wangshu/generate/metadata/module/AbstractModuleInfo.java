package com.wangshu.generate.metadata.module;

@lombok.Data
public abstract class AbstractModuleInfo implements ModuleInfo {

    private String moduleName;
    private String modulePackageName;
    private String modulePath;

    public AbstractModuleInfo() {

    }

    public AbstractModuleInfo(String moduleName, String modulePackageName, String modulePath) {
        this.moduleName = moduleName;
        this.modulePackageName = modulePackageName;
        this.modulePath = modulePath;
    }

    @Override
    public String getModuleName() {
        return this.moduleName;
    }

    @Override
    public String getModulePackageName() {
        return this.modulePackageName;
    }

    @Override
    public String getModulePath() {
        return this.modulePath;
    }

}
