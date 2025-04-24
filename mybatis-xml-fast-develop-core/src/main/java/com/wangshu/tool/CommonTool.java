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

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.enu.DataBaseType;
import com.wangshu.enu.SqlStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

@Slf4j
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

    @SuppressWarnings("unchecked")
    public static @Nullable Class<? extends BaseModel> getQueryModel(@NotNull Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        while (Objects.nonNull(type) && !(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }
        if (Objects.isNull(type)) {
            return null;
        }
        List<Type> actualTypeArguments = List.of(((ParameterizedType) type).getActualTypeArguments());
        if (actualTypeArguments.isEmpty()) {
            return null;
        }
        return (Class<? extends BaseModel>) actualTypeArguments.getFirst();
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

    public static String getNewStrBySqlStyle(@NotNull SqlStyle sqlStyle, String str) {
        String newStr;
        switch (sqlStyle) {
            case lcc -> newStr = StrUtil.lowerFirst(str);
            case sc -> newStr = StrUtil.toUnderlineCase(str);
            case su -> newStr = StrUtil.toUnderlineCase(str).toUpperCase();
            default -> throw new IllegalArgumentException("暂无对应风格实现");
        }
        return newStr;
    }

    public static String getDbColumnTypeByField(@NotNull DataBaseType dataBaseType, @NotNull Field field) {
        String dbColumnType;
        switch (dataBaseType) {
            case oracle -> dbColumnType = OracleTypeMapInfo.getDbColumnTypeByField(field);
            case mssql -> dbColumnType = MssqlTypeMapInfo.getDbColumnTypeByField(field);
            case postgresql -> dbColumnType = PostgresqlTypeMapInfo.getDbColumnTypeByField(field);
            case mysql -> dbColumnType = MysqlTypeMapInfo.getDbColumnTypeByField(field);
            default -> throw new IllegalArgumentException("暂无对应数据库类型实现");
        }
        return dbColumnType;
    }

    public static String getDbColumnTypeByJavaTypeName(@NotNull DataBaseType dataBaseType, String javaTypeName) {
        String dbColumnType;
        switch (dataBaseType) {
            case oracle -> dbColumnType = OracleTypeMapInfo.getDbColumnTypeByJavaTypeName(javaTypeName);
            case mssql -> dbColumnType = MssqlTypeMapInfo.getDbColumnTypeByJavaTypeName(javaTypeName);
            case postgresql -> dbColumnType = PostgresqlTypeMapInfo.getDbColumnTypeByJavaTypeName(javaTypeName);
            case mysql -> dbColumnType = MysqlTypeMapInfo.getDbColumnTypeByJavaTypeName(javaTypeName);
            default -> throw new IllegalArgumentException("暂无对应数据库类型实现");
        }
        return dbColumnType;
    }

    public static int getDefaultLengthByDbColumnType(@NotNull DataBaseType dataBaseType, String dbColumnType) {
        int defaultLength;
        switch (dataBaseType) {
            case oracle -> defaultLength = OracleTypeMapInfo.getDefaultLengthByDbColumnType(dbColumnType);
            case mssql -> defaultLength = MssqlTypeMapInfo.getDefaultLengthByDbColumnType(dbColumnType);
            case postgresql -> defaultLength = PostgresqlTypeMapInfo.getDefaultLengthByDbColumnType(dbColumnType);
            case mysql -> defaultLength = MysqlTypeMapInfo.getDefaultLengthByDbColumnType(dbColumnType);
            default -> throw new IllegalArgumentException("暂无对应数据库类型实现");
        }
        return defaultLength;
    }

    public static @NotNull JdbcType getJdbcTypeByJavaTypeName(@NotNull String javaTypeName) {
        return MybatisJdbcTypeMapInfo.getJdbcTypeForJavaTypeName(javaTypeName);
    }

}
