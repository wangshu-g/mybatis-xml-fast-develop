package com.wangshu.tool;

import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

public class CommonTool {

    @SuppressWarnings("unchecked")
    public static @Nullable Class<? extends BaseModel> getServiceModel(@NotNull Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        while (Objects.nonNull(type) && !(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }
        if (Objects.isNull(type)) {
            return null;
        }
        List<Type> actualTypeArguments = List.of(((ParameterizedType) type).getActualTypeArguments());
        if (actualTypeArguments.size() < 3) {
            return null;
        }
        return (Class<? extends BaseModel>) actualTypeArguments.get(2);
    }

    @SuppressWarnings("unchecked")
    public static @Nullable Class<? extends BaseDataMapper<? extends BaseModel>> getServiceMapper(@NotNull Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        while (Objects.nonNull(type) && !(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }
        if (Objects.isNull(type)) {
            return null;
        }
        List<Type> actualTypeArguments = List.of(((ParameterizedType) type).getActualTypeArguments());
        if (actualTypeArguments.size() < 3) {
            return null;
        }
        return (Class<? extends BaseDataMapper<? extends BaseModel>>) actualTypeArguments.get(1);
    }

    @SuppressWarnings("unchecked")
    public static @Nullable Class<? extends BaseModel> getControllerModel(@NotNull Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        while (Objects.nonNull(type) && !(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }
        if (Objects.isNull(type)) {
            return null;
        }
        List<Type> actualTypeArguments = List.of(((ParameterizedType) type).getActualTypeArguments());
        if (actualTypeArguments.size() < 2) {
            return null;
        }
        return (Class<? extends BaseModel>) actualTypeArguments.get(1);
    }

    @SuppressWarnings("unchecked")
    public static @Nullable Class<? extends AbstractBaseDataService<?, ? extends BaseDataMapper<? extends BaseModel>, ? extends BaseModel>> getControllerService(@NotNull Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        while (Objects.nonNull(type) && !(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }
        if (Objects.isNull(type)) {
            return null;
        }
        List<Type> actualTypeArguments = List.of(((ParameterizedType) type).getActualTypeArguments());
        if (actualTypeArguments.size() < 2) {
            return null;
        }
        return (Class<? extends AbstractBaseDataService<?, ? extends BaseDataMapper<? extends BaseModel>, ? extends BaseModel>>) actualTypeArguments.getFirst();
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends BaseModel> getMapperModel(@NotNull Class<?> clazz) {
        Class<? extends BaseModel> model = null;
        if (BaseDataMapper.class.isAssignableFrom(clazz)) {
            for (Type genericInterface : clazz.getGenericInterfaces()) {
                if (genericInterface instanceof ParameterizedType) {
                    Type[] actualTypeArguments = ((ParameterizedType) genericInterface).getActualTypeArguments();
                    if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?> temp) {
                        if (BaseModel.class.isAssignableFrom(temp)) {
                            model = (Class<? extends BaseModel>) temp;
                        }
                    }
                }
            }
        }
        return model;
    }

    public static @NotNull List<Field> getClazzFields(@NotNull Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (Objects.isNull(map.get(declaredField.getName()))) {
                    map.put(declaredField.getName(), declaredField);
                    fields.add(declaredField);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static @NotNull List<Field> getClazzBaseFields(@NotNull Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (Objects.nonNull(declaredField.getAnnotation(Column.class)) && Objects.isNull(map.get(declaredField.getName()))) {
                    map.put(declaredField.getName(), declaredField);
                    fields.add(declaredField);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static @NotNull List<Field> getClazzJoinFields(@NotNull Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (Objects.nonNull(declaredField.getAnnotation(Join.class)) && Objects.isNull(map.get(declaredField.getName()))) {
                    map.put(declaredField.getName(), declaredField);
                    fields.add(declaredField);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static @Nullable Field getClazzFieldByName(@NotNull Class<?> clazz, @NotNull String fieldName) {
        Field field = null;
        while (clazz != null && field == null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }

    public static <T> Field getField(@NotNull Function<T, ?> function) {
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = function.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(function);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        String implMethodName = serializedLambda.getImplMethodName();
        if (!implMethodName.startsWith("is") && !implMethodName.startsWith("get")) {
            throw new RuntimeException("get方法名不符合规范");
        }
        int index = implMethodName.startsWith("is") ? 2 : 3;
        String fieldName = implMethodName.substring(index);
        String firstChar = fieldName.substring(0, 1);
        fieldName = fieldName.replaceFirst(firstChar, firstChar.toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return field;
    }

}
