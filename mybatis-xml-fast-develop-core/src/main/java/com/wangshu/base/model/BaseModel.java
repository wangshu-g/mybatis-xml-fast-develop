package com.wangshu.base.model;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.base.entity.EntityTool;
import com.wangshu.tool.CacheTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangshu-g
 * <p>BaseModel</p>
 */
public class BaseModel extends EntityTool {

    @Override
    public List<Field> modelFields() {
        return CacheTool.getModelFields(this.getClass());
    }

    public @Nullable Field modelPrimaryField() {
        return CacheTool.getModelPrimaryField(this.getClass());
    }

    public void setModelValuesFromMapByFieldNameWithTitle(@NotNull Map<String, Object> map) {
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
