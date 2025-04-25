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
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OracleTypeMapInfo {

    private static final Map<String, String> JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE = new HashMap<>();
    private static final Map<String, Integer> DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH = new HashMap<>();

    static {
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Integer.class.getName(), "NUMBER");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Long.class.getName(), "NUMBER");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Short.class.getName(), "NUMBER");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Float.class.getName(), "BINARY_FLOAT");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Double.class.getName(), "BINARY_DOUBLE");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Boolean.class.getName(), "NUMBER");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Character.class.getName(), "CHAR");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(BigDecimal.class.getName(), "NUMBER");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(String.class.getName(), "VARCHAR2");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(java.util.Date.class.getName(), "TIMESTAMP");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(java.sql.Date.class.getName(), "DATE");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(java.sql.Time.class.getName(), "TIMESTAMP");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(java.sql.Timestamp.class.getName(), "TIMESTAMP");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Byte.class.getName(), "NUMBER");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Byte[].class.getName(), "BLOB");
    }

    static {
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("CHAR", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("VARCHAR2", 4000);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NVARCHAR2", 4000);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NCHAR", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NUMBER", 22);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("FLOAT", 126);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BINARY_FLOAT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BINARY_DOUBLE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DATE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP WITH TIME ZONE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP WITH LOCAL TIME ZONE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INTERVAL YEAR TO MONTH", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INTERVAL DAY TO SECOND", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("RAW", 2000);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("LONG RAW", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("CLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NCLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BFILE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("ROWID", 18);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("UROWID", 4000);
    }

    public static String getDbColumnTypeByField(@NotNull Field field) {
        String type = field.getType().getName();
        String oracleType = JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.get(type);
        if (StrUtil.isBlank(oracleType)) {
            throw new IllegalArgumentException("Unsupported field: " + field);
        }
        return oracleType;
    }

    public static String getDbColumnTypeByJavaTypeName(String javaTypeName) {
        String oracleType = JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.get(javaTypeName);
        if (StrUtil.isBlank(oracleType)) {
            throw new IllegalArgumentException("Unsupported javaTypeName: " + javaTypeName);
        }
        return oracleType;
    }

    public static @NotNull Integer getDefaultLengthByDbColumnType(String dbColumnType) {
        Integer length = DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.get(dbColumnType);
        if (Objects.isNull(length)) {
            throw new IllegalArgumentException("Unsupported dbColumnType: " + dbColumnType);
        }
        return length;
    }

}


