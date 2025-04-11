package com.wangshu.generate.metadata.module;

// MIT License
//
// Copyright (c) 2025 2560334673@qq.com wangshu-g https://github.com/wangshu-g/mybatis-xml-fast-develop
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

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
