package com.wangshu.db;

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
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MariadbTypeMapInfo {

    private static final Map<String, String> JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE = new HashMap<>();
    private static final Map<String, Integer> DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH = new HashMap<>();

    static {
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Integer.class.getName(), "INT");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Long.class.getName(), "BIGINT");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Short.class.getName(), "SMALLINT");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Float.class.getName(), "FLOAT");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Double.class.getName(), "DOUBLE");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Boolean.class.getName(), "TINYINT");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Character.class.getName(), "CHAR");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(BigDecimal.class.getName(), "DECIMAL");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(String.class.getName(), "VARCHAR");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(java.util.Date.class.getName(), "TIMESTAMP");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(java.sql.Date.class.getName(), "TIMESTAMP");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Byte.class.getName(), "TINYINT");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Byte[].class.getName(), "BLOB");
    }

    static {
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TINYINT", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("SMALLINT", 2);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("MEDIUMINT", 3);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INT", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INTEGER", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BIGINT", 8);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("FLOAT", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DOUBLE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DECIMAL", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NUMERIC", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DATE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIME", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DATETIME", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("YEAR", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("CHAR", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("VARCHAR", 255);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BINARY", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("VARBINARY", 255);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TINYBLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("MEDIUMBLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("LONGBLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TINYTEXT", 255);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TEXT", 65535);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("MEDIUMTEXT", 16777215);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("LONGTEXT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("ENUM", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("SET", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BOOLEAN", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("SERIAL", 8);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("JSON", -1);
    }

    public static String getDbColumnTypeByField(@NotNull Field field) {
        String type = field.getType().getName();
        String mssqlType = JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.get(type);
        if (StrUtil.isBlank(mssqlType)) {
            throw new IllegalArgumentException("Unsupported field: " + field);
        }
        return mssqlType;
    }

    public static String getDbColumnTypeByJavaTypeName(String javaTypeName) {
        String mssqlType = JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.get(javaTypeName);
        if (StrUtil.isBlank(mssqlType)) {
            throw new IllegalArgumentException("Unsupported javaTypeName: " + javaTypeName);
        }
        return mssqlType;
    }

    public static @NotNull Integer getDefaultLengthByDbColumnType(String dbColumnType) {
        Integer length = DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.get(dbColumnType);
        if (Objects.isNull(length)) {
            throw new IllegalArgumentException("Unsupported dbColumnType: " + dbColumnType);
        }
        return length;
    }

}
