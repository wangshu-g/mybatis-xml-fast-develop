package com.wangshu.table;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Model;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


@lombok.Data
public abstract class ModelInfo {

    private List<Field> fields;
    private List<String> names;
    private Class<?> metadata;
    private Model model;
    private String table;
    private String modelName;
    private String modelFullName;

    public ModelInfo() {
    }

    public ModelInfo(Class<?> clazz) {
        this.init(clazz);
    }

    public void init(@NotNull Class<?> clazz) {
        this.model = clazz.getAnnotation(Model.class);
        this.table = this.initTableName(clazz);
        this.modelName = clazz.getSimpleName();
        this.modelFullName = clazz.getTypeName();
        this.initNames(clazz);
        this.initFields(clazz);
    }

    public void initFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Objects.nonNull(field.getAnnotation(Column.class))) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        this.setFields(fields);
    }

    public void initNames(@NotNull Class<?> clazz) {
        Model modelAnnotation = clazz.getAnnotation(Model.class);
        if (Objects.nonNull(modelAnnotation)) {
            this.setNames(Stream.of(modelAnnotation.names()).map(String::toLowerCase).toList());
        }
    }

    public String initTableName(@NotNull Class<?> clazz) {
        Model modelAnnotation = clazz.getAnnotation(Model.class);
        String table = modelAnnotation.table().toLowerCase();
        if (StrUtil.isBlank(table)) {
            table = clazz.getSimpleName().toLowerCase();
        }
        return table;
    }

}
