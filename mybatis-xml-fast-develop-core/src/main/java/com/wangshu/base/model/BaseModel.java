package com.wangshu.base.model;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.wangshu.annotation.Column;
import com.wangshu.tool.CacheTool;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author GSF
 * <p>BaseModel</p>
 */
public class BaseModel implements Serializable {

    public List<Field> modelFields() {
        return CacheTool.getModelFields(this.getClass());
    }

    public Map<String, Field> modelFieldsMap() {
        return this.modelFields().stream().collect(Collectors.toMap(Field::getName, value -> value));
    }

    public void setModelAnyValueByFieldName(String name, Object value) {
        this.setModelValuesFromMapByFieldName(new HashMap<>() {{
            put(name, value);
        }});
    }

    public Object modelAnyValueByFieldName(String name) {
        return this.toMap().get(name);
    }

    public @Nullable Field modelPrimaryField() {
        return CacheTool.getModelPrimaryField(this.getClass());
    }

    public void setModelValuesFromMapByFieldName(Map<String, Object> map) {
        if (Objects.nonNull(map)) {
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
    }

    public void setModelValuesFromMapByFieldNameWithTitle(Map<String, Object> map) {
        if (Objects.nonNull(map)) {
            Map<String, Field> fields = this.modelFieldsMap();
            fields.forEach((fieldName, field) -> {
                String title = fieldName;
                Column annotation = field.getAnnotation(Column.class);
                if (Objects.nonNull(annotation) && StrUtil.isNotBlank(annotation.title())) {
                    title = annotation.title();
                }
                if (Objects.nonNull(map.get(title))) {
                    field.setAccessible(true);
                    try {
                        field.set(this, Convert.convert(field.getType(), map.get(title)));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
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

    public Map<String, Object> toMap() {
        List<Field> fields = this.modelFields();
        Map<String, Object> map = new HashMap<>(fields.size());
        fields.forEach(field -> {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return map;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public String toJson(JSONWriter.Context context) {
        return JSON.toJSONString(this, context);
    }

    public String toJson(JSONWriter.Feature... features) {
        return JSON.toJSONString(this, features);
    }

    public String toJson(ValueFilter filter, JSONWriter.Feature... features) {
        return JSON.toJSONString(this, filter, features);
    }

    public String toJson(ValueFilter[] filters, JSONWriter.Feature... features) {
        return JSON.toJSONString(this, filters, features);
    }

    public String toJson(String format, JSONWriter.Feature[] features) {
        return JSON.toJSONString(this, format, features);
    }

    public String toJson(String format, ValueFilter[] filters, JSONWriter.Feature[] features) {
        return JSON.toJSONString(this, format, filters, features);
    }

    public String toJsonDateFormat(String format) {
        return this.toJson(format, null);
    }

    public String toJsonyMdHms() {
        return this.toJsonDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public String toJsonyMd() {
        return this.toJsonDateFormat("yyyy-MM-dd");
    }

}
