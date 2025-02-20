package com.wangshu.generate.metadata.module;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class ModuleTemplateInfo extends AbstractModuleInfo {
    public ModuleTemplateInfo() {

    }

    public ModuleTemplateInfo(String moduleName, String modulePackageName, String modulePath) {
        super(moduleName, modulePackageName, modulePath);
    }
}
