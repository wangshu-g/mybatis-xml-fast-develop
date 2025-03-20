package com.wangshu.base.entity;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.wangshu.exception.IException;
import com.wangshu.tool.CommonTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EntityTool implements Serializable {

    public List<Field> modelFields() {
        return CommonTool.getClazzFields(this.getClass());
    }

    public Map<String, Field> modelFieldsMap() {
        return this.modelFields().stream().collect(Collectors.toMap(Field::getName, value -> value));
    }

    public void setModelAnyValueByFieldName(String name, Object value) {
        this.setModelValuesFromMapByFieldName(new HashMap<>() {{
            put(name, value);
        }});
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T modelAnyValueByFieldName(String name, Class<T> clazz) throws IllegalAccessException {
        Object object = this.toMap().get(name);
        if (Objects.isNull(object)) {
            return null;
        }
        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T safeModelAnyValueByFieldName(String name, Class<T> clazz) {
        Object object = this.safeToMap().get(name);
        if (Objects.isNull(object)) {
            return null;
        }
        return (T) object;
    }

    public @Nullable Object modelAnyValueByFieldName(String name) throws IllegalAccessException {
        return this.toMap().get(name);
    }

    public @Nullable Object safeModelAnyValueByFieldName(String name) {
        return this.safeToMap().get(name);
    }

    public void setModelValuesFromMapByFieldName(@NotNull Map<String, Object> map) {
        Map<String, Field> fields = this.modelFieldsMap();
        fields.forEach((fieldName, field) -> {
            if (Objects.nonNull(map.get(fieldName))) {
                field.setAccessible(true);
                try {
                    field.set(this, Convert.convert(field.getType(), map.get(fieldName)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setModelValuesFromModelJsonStr(String json) {
        this.setModelValuesFromMapByFieldName(JSON.parseObject(json));
    }

    public boolean fieldIsExist(String name) {
        return this.modelFieldsMap().containsKey(name);
    }

    public boolean fieldIsExist(Field field) {
        return this.modelFields().contains(field);
    }

    public <T extends EntityTool> T toEntity(@NotNull Class<T> clazz) {
        try {
            T instance = clazz.getConstructor().newInstance();
            instance.setModelValuesFromMapByFieldName(this.safeToMap());
            return instance;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Map<String, Object> toMap() throws IllegalAccessException {
        List<Field> fields = this.modelFields();
        Map<String, Object> map = new HashMap<>(fields.size());
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(this));
        }
        return map;
    }

    public Map<String, Object> safeToMap() {
        List<Field> fields = this.modelFields();
        Map<String, Object> map = new HashMap<>(fields.size());
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(this));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public String toJson(JSONWriter.Context context) {
        return JSON.toJSONString(this, context);
    }

    public String toJson(@NotNull JSONWriter.Feature... features) {
        return JSON.toJSONString(this, features);
    }

    public String toJson(ValueFilter filter, @NotNull JSONWriter.Feature... features) {
        return JSON.toJSONString(this, filter, features);
    }

    public String toJson(ValueFilter[] filters, @NotNull JSONWriter.Feature... features) {
        return JSON.toJSONString(this, filters, features);
    }

    public String toJson(String format, @NotNull JSONWriter.Feature[] features) {
        return JSON.toJSONString(this, format, features);
    }

    public String toJson(String format, ValueFilter[] filters, @NotNull JSONWriter.Feature[] features) {
        return JSON.toJSONString(this, format, filters, features);
    }

    public String toJsonDateFormat(String format) {
        return this.toJson(format, new JSONWriter.Feature[]{});
    }

    public String toJsonyMdHms() {
        return this.toJsonDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public String toJsonyMd() {
        return this.toJsonDateFormat("yyyy-MM-dd");
    }

}
