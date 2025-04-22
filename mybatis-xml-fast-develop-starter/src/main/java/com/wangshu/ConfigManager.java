package com.wangshu;

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

import com.wangshu.annotation.EnableConfig;
import com.wangshu.annotation.Model;
import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.table.*;
import com.wangshu.tool.CacheTool;
import com.wangshu.tool.CommonParam;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangshu-g
 */
@Slf4j
public class ConfigManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private EnableConfig enableConfig;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.enableConfig = CommonParam.mainClazz.getAnnotation(EnableConfig.class);
        CommonParam.applicationContext = applicationContext;
        try {
            CommonParam.modelClazz = CommonParam.getTargetPackageModelClazz(this.enableConfig.modelPackage());
            this.cacheConfig();
            this.tableConfig();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void cacheConfig() {
        CacheTool.initModelCache(new ArrayList<>(CommonParam.modelClazz));
        CacheTool.initServiceCache(applicationContext.getBeansOfType(BaseDataService.class).values().stream().map(item -> item.getClass()).collect(Collectors.toList()));
        CacheTool.initControllerCache(applicationContext.getBeansOfType(BaseDataController.class).values().stream().map(item -> item.getClass()).collect(Collectors.toList()));
    }

    private void tableConfig() {
        if (this.enableConfig.enableAutoInitTable() && Objects.nonNull(this.enableConfig.modelPackage())) {
            List<String> targetDataSource = List.of(this.enableConfig.targetDataSource());
            Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);
            if (!targetDataSource.contains("*")) {
                dataSourceMap.entrySet().removeIf(next -> !targetDataSource.contains(next.getKey()));
            }
            dataSourceMap.forEach((k, v) -> {
                try (Connection connection = v.getConnection()) {
                    for (Class<? extends BaseModel> modelClazz : CommonParam.modelClazz) {
                        Model modelAnnotation = modelClazz.getAnnotation(Model.class);
                        GenerateTable generateTable = null;
                        switch (modelAnnotation.dataBaseType()) {
                            case oracle -> generateTable = new GenerateTableOracle(modelClazz);
                            case mssql -> generateTable = new GenerateTableMssql(modelClazz);
                            case postgresql -> generateTable = new GenerateTablePostgresql(modelClazz);
                            case mysql -> generateTable = new GenerateTableMysql(modelClazz);
                        }
                        if (Objects.isNull(generateTable)) {
                            log.warn("暂无对应数据库类型实现: {}", modelAnnotation.dataBaseType());
                        } else {
                            generateTable.createTable(connection);
                        }
                    }
                } catch (SQLException e) {
                    log.error("数据源: {}, 获取连接失败", k);
                }
            });
        }
    }

}
