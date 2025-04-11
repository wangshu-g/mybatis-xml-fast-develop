package com.wangshu.table;

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
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author wangshu-g
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
public abstract class GenerateTable extends ModelInfo {

    public GenerateTable() {
    }

    public GenerateTable(Class<?> clazz) {
        super(clazz);
    }

    public abstract String getJdbcType(Field field);

    public abstract int getDefaultLength(Field field);

    public boolean isDefaultNull(Field field) {
        return !isPrimaryKey(field);
    }

    public boolean isPrimaryKey(@NotNull Field field) {
        if (Objects.nonNull(field.getAnnotation(Column.class))) {
            return field.getAnnotation(Column.class).primary();
        }
        return StrUtil.equals(field.getName(), "id");
    }

    public String getComment(@NotNull Field field) {
        String comment = null;
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            comment = column.comment();
            if (StrUtil.isBlank(comment)) {
                comment = column.title();
            }
        }
        if (StrUtil.isBlank(comment)) {
            comment = field.getName();
        }
        return comment;
    }

    public abstract void createTable(Connection connection) throws SQLException;

}
