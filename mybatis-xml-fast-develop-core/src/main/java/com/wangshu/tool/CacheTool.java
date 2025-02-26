package com.wangshu.tool;

import com.wangshu.base.controller.AbstractBaseDataController;
import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.cache.ColumnType;
import com.wangshu.cache.ControllerCache;
import com.wangshu.cache.ModelCache;
import com.wangshu.cache.ServiceCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CacheTool {

    public static Map<Class<? extends BaseModel>, ModelCache> modelCacheMap = new HashMap<>();
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

    private static @NotNull ModelCache getOrCreateModelCache(Class<? extends BaseModel> modelClazz) {
        ModelCache modelCache = modelCacheMap.get(modelClazz);
        if (Objects.isNull(modelCache)) {
            modelCache = new ModelCache(modelClazz);
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
