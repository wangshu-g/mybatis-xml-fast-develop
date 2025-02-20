package com.wangshu.cache;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

@Data
public class ColumnType {

    String name;
    String title;
    String dataType;
    Integer order;

    public ColumnType() {

    }

    public ColumnType(@NotNull Field field) {
        this.name = field.getName();
        this.dataType = field.getType().getSimpleName();
        this.order = fieldOrder(field);
        this.title = fieldTitle(field);
    }

    private String fieldTitle(@NotNull Field field) {
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            if (StrUtil.isBlank(column.title())) {
                return column.comment();
            }
            return column.title();
        }
        return field.getName();
    }

    private @NotNull Integer fieldOrder(@NotNull Field field) {
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            return column.order();
        }
        return 0;
    }

}
