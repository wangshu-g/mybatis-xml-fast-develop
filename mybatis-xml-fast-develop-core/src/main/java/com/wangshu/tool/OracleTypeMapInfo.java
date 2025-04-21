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
import org.apache.ibatis.type.JdbcType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OracleTypeMapInfo {

    private static final Map<String, String> JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE = new HashMap<>();
    private static final Map<String, String> DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME = new HashMap<>();
    private static final Map<String, Integer> DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH = new HashMap<>();
    private static final Map<String, JdbcType> DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE = new HashMap<>();

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
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Byte[].class.getName(), "BLOB");
    }

    static {
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("CHAR", Character.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("VARCHAR2", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("NVARCHAR2", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("NCHAR", Character.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("NUMBER", BigDecimal.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("FLOAT", Double.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BINARY_FLOAT", Float.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BINARY_DOUBLE", Double.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DATE", java.sql.Date.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TIMESTAMP", java.sql.Timestamp.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TIMESTAMP WITH TIME ZONE", java.sql.Timestamp.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TIMESTAMP WITH LOCAL TIME ZONE", java.sql.Timestamp.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("INTERVAL YEAR TO MONTH", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("INTERVAL DAY TO SECOND", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("RAW", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("LONG RAW", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BLOB", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("CLOB", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("NCLOB", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BFILE", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("ROWID", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("UROWID", String.class.getName());
    }

    static {
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("CHAR", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("VARCHAR2", 4000);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NVARCHAR2", 4000);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NCHAR", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NUMBER", 22);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("FLOAT", 126);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BINARY_FLOAT", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BINARY_DOUBLE", 8);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DATE", 7);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP", 11);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP WITH TIME ZONE", 13);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP WITH LOCAL TIME ZONE", 11);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INTERVAL YEAR TO MONTH", 5);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INTERVAL DAY TO SECOND", 11);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("RAW", 2000);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("LONG RAW", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("CLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NCLOB", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BFILE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("ROWID", 18);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("UROWID", 4000);
    }

    static {
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("CHAR", JdbcType.CHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("VARCHAR2", JdbcType.VARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("NVARCHAR2", JdbcType.NVARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("NCHAR", JdbcType.NCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("NUMBER", JdbcType.NUMERIC);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("FLOAT", JdbcType.FLOAT);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("BINARY_FLOAT", JdbcType.FLOAT);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("BINARY_DOUBLE", JdbcType.DOUBLE);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("DATE", JdbcType.DATE);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("TIMESTAMP", JdbcType.TIMESTAMP);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("TIMESTAMP WITH TIME ZONE", JdbcType.TIMESTAMP);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("TIMESTAMP WITH LOCAL TIME ZONE", JdbcType.TIMESTAMP);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("INTERVAL YEAR TO MONTH", JdbcType.VARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("INTERVAL DAY TO SECOND", JdbcType.VARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("RAW", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("LONG RAW", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("BLOB", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("CLOB", JdbcType.CLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("NCLOB", JdbcType.NCLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("BFILE", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("ROWID", JdbcType.VARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("UROWID", JdbcType.VARCHAR);
    }

    @NotNull
    public static String getDbColumnTypeByField(@NotNull Field field) {
        String type = field.getType().getName();
        String oracleType = JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.get(type);
        if (oracleType == null) {
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

    public static String getJavaTypeNameByDbColumnType(@NotNull String dbColumnType) {
        String javaType = DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.get(dbColumnType.toUpperCase());
        if (StrUtil.isBlank(javaType)) {
            throw new IllegalArgumentException("Unsupported dbColumnType: " + dbColumnType);
        }
        return javaType;
    }

    public static Integer getDbColumnTypeDefaultLengthByField(Field field) {
        Integer length = DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.get(getDbColumnTypeByField(field));
        if (Objects.isNull(length)) {
            throw new IllegalArgumentException("Unsupported field: " + field);
        }
        return length;
    }

    public static Integer getDbColumnTypeDefaultLengthByMybatisJdbcType(String dbColumnType) {
        Integer length = DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.get(dbColumnType);
        if (Objects.isNull(length)) {
            throw new IllegalArgumentException("Unsupported dbColumnType: " + dbColumnType);
        }
        return length;
    }

    public static Integer getDbColumnTypeDefaultLengthByJavaTypeName(String javaTypeName) {
        Integer length = DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.get(getDbColumnTypeByJavaTypeName(javaTypeName));
        if (Objects.isNull(length)) {
            throw new IllegalArgumentException("Unsupported javaTypeName: " + javaTypeName);
        }
        return length;
    }

    public static JdbcType getMybatisJdbcTypeByDbColumnType(@NotNull String dbColumnType) {
        JdbcType jdbcType = DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.get(dbColumnType.toUpperCase());
        if (Objects.isNull(jdbcType)) {
            throw new IllegalArgumentException("Unsupported dbColumnType: " + dbColumnType);
        }
        return jdbcType;
    }

    @NotNull
    public static String getMybatisJdbcTypeStrByDbColumnType(@NotNull String dbColumnType) {
        JdbcType jdbcType = DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.get(dbColumnType.toUpperCase());
        if (Objects.isNull(jdbcType)) {
            throw new IllegalArgumentException("Unsupported dbColumnType: " + dbColumnType);
        }
        return jdbcType.name();
    }
}


