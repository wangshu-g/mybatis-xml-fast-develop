package com.wangshu.tool;

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

import com.wangshu.base.controller.AbstractBaseDataController;
import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.query.Query;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.cache.*;
import com.wangshu.enu.DataBaseType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CacheTool {

    public static Map<Class<? extends BaseModel>, ModelCache> modelCacheMap = new HashMap<>();
    public static Map<Class<? extends Query>, QueryCache> queryCacheMap = new HashMap<>();
    public static Map<Class<? extends BaseDataService>, ServiceCache> serviceCacheMap = new HashMap<>();
    public static Map<Class<? extends BaseDataController>, ControllerCache> controllerCacheMap = new HashMap<>();

    public static void initModelCache(@NotNull List<Class<? extends BaseModel>> models) {
        for (Class<? extends BaseModel> model : models) {
            modelCacheMap.put(model, new ModelCache(model));
        }
    }

    public static void initServiceCache(@NotNull List<Class<? extends BaseDataService>> services) {
        for (Class<? extends BaseDataService> service : services) {
            serviceCacheMap.put(service, new ServiceCache(service));
        }
    }

    public static void initControllerCache(@NotNull List<Class<? extends BaseDataController>> controllers) {
        for (Class<? extends BaseDataController> controller : controllers) {
            controllerCacheMap.put(controller, new ControllerCache(controller));
        }
    }

    private static @NotNull ServiceCache getOrCreateServiceCache(Class<? extends BaseDataService> serviceClazz) {
        ServiceCache serviceCache = serviceCacheMap.get(serviceClazz);
        if (Objects.isNull(serviceCache)) {
            serviceCache = new ServiceCache(serviceClazz);
            serviceCacheMap.put(serviceClazz, serviceCache);
        }
        return serviceCache;
    }

    public static Class<? extends BaseModel> getServiceModelGeneric(@NotNull Class<? extends BaseDataService> serviceClazz) {
        return getOrCreateServiceCache(serviceClazz).modelGeneric;
    }

    public static Class<? extends BaseDataMapper<? extends BaseModel>> getServiceMapperGeneric(@NotNull Class<? extends BaseDataService> serviceClazz) {
        return getOrCreateServiceCache(serviceClazz).mapperGeneric;
    }

    public static @NotNull List<Field> getServiceModelGenericFields(@NotNull Class<? extends BaseDataService> serviceClazz) {
        Class<? extends BaseModel> serviceModelGeneric = getServiceModelGeneric(serviceClazz);
        return getModelFields(serviceModelGeneric);
    }

    public static @NotNull List<Field> getServiceModelGenericBaseFields(@NotNull Class<? extends BaseDataService> serviceClazz) {
        Class<? extends BaseModel> serviceModelGeneric = getServiceModelGeneric(serviceClazz);
        return getModelBaseFields(serviceModelGeneric);
    }

    private static @NotNull QueryCache getOrCreateQueryCache(Class<? extends Query> queryClazz) {
        QueryCache queryCache = queryCacheMap.get(queryClazz);
        if (Objects.isNull(queryCache)) {
            queryCache = new QueryCache(queryClazz);
            queryCacheMap.put(queryClazz, queryCache);
        }
        return queryCache;
    }

    public static @NotNull List<Field> getQueryFields(@NotNull Class<? extends Query> queryClazz) {
        QueryCache queryCache = getOrCreateQueryCache(queryClazz);
        return queryCache.fields;
    }

    public static Class<? extends BaseModel> getQueryModelGeneric(@NotNull Class<? extends Query> queryClazz) {
        return getOrCreateQueryCache(queryClazz).modelGeneric;
    }

    public static @NotNull List<Field> getQueryModelGenericFields(@NotNull Class<? extends Query> queryClazz) {
        Class<? extends BaseModel> queryModelGeneric = getQueryModelGeneric(queryClazz);
        return getModelFields(queryModelGeneric);
    }

    private static @NotNull ModelCache getOrCreateModelCache(Class<? extends BaseModel> modelClazz) {
        ModelCache modelCache = modelCacheMap.get(modelClazz);
        if (Objects.isNull(modelCache)) {
            modelCache = new ModelCache(modelClazz);
            modelCacheMap.put(modelClazz, modelCache);
        }
        return modelCache;
    }

    public static @NotNull List<Field> getModelFields(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).fields;
    }

    public static @NotNull List<Field> getModelBaseFields(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).baseFields;
    }

    public static @Nullable Field getModelPrimaryField(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).primaryField;
    }

    public static @Nullable Field getModelCreatedAtField(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).createdField;
    }

    public static @Nullable Field getModelUpdatedField(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).updatedField;
    }

    public static @Nullable Field getModelDeletedField(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).deletedField;
    }

    public static @NotNull DataBaseType getModelDataBaseType(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).dataBaseType;
    }

    public static @NotNull List<ColumnType> getModelColumnTypes(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).columnTypes;
    }

    public static @NotNull List<String> getModelOrderColumnPossibleParameterName(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).orderColumnPossibleParameterName;
    }

    public static @NotNull List<String> getModelDeleteMethodPossibleWhereParameterName(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).deleteMethodPossibleWhereParameterName;
    }

    public static @NotNull List<String> getModelUpdateMethodPossibleWhereParameterName(@NotNull Class<? extends BaseModel> modelClazz) {
        return getOrCreateModelCache(modelClazz).updateMethodPossibleWhereParameterName;
    }

    private static @NotNull ControllerCache getOrCreateControllerCache(Class<? extends BaseDataController> controllerClazz) {
        ControllerCache controllerCache = controllerCacheMap.get(controllerClazz);
        if (Objects.isNull(controllerCache)) {
            controllerCache = new ControllerCache(controllerClazz);
            controllerCacheMap.put(controllerClazz, controllerCache);
        }
        return controllerCache;
    }

    public static Class<? extends BaseModel> getControllerModelGeneric(@NotNull Class<? extends BaseDataController> controllerClazz) {
        return getOrCreateControllerCache(controllerClazz).modelGeneric;
    }

    public static Class<? extends BaseDataService> getControllerServiceGeneric(@NotNull Class<? extends AbstractBaseDataController> controllerClazz) {
        return getOrCreateControllerCache(controllerClazz).serviceGeneric;
    }

    public static @NotNull List<ColumnType> getControllerModelGenericColumnType(@NotNull Class<? extends BaseDataController> controllerClazz) {
        Class<? extends BaseModel> controllerModelGeneric = getControllerModelGeneric(controllerClazz);
        return getModelColumnTypes(controllerModelGeneric);
    }

}
