package com.wangshu.cache;

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
