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

public class MysqlTypeMapInfo {

    private static final Map<String, String> JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE = new HashMap<>();
    private static final Map<String, String> DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME = new HashMap<>();
    private static final Map<String, Integer> DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH = new HashMap<>();
    private static final Map<String, JdbcType> DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE = new HashMap<>();

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
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Byte[].class.getName(), "BLOB");
    }

    static {
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TINYINT", Integer.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("SMALLINT", Short.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("MEDIUMINT", Integer.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("INT", Integer.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("INTEGER", Integer.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BIGINT", Long.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("FLOAT", Float.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DOUBLE", Double.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DECIMAL", BigDecimal.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("NUMERIC", BigDecimal.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("CHAR", Character.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("VARCHAR", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TEXT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("MEDIUMTEXT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("LONGTEXT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("ENUM", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("SET", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BIT", Boolean.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("JSON", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TIMESTAMP", java.util.Date.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DATETIME", java.util.Date.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DATE", java.util.Date.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BLOB", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("MEDIUMBLOB", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("LONGBLOB", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TINYBLOB", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("VARBINARY", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BINARY", Byte[].class.getName());
    }

    static {
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TINYINT", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("SMALLINT", 2);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("MEDIUMINT", 3);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INT", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INTEGER", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BIGINT", 8);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("FLOAT", 4);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DOUBLE", 8);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DECIMAL", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("NUMERIC", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DATE", 3);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIME", 3);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DATETIME", 8);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TIMESTAMP", 4);
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

    static {
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("CHAR", JdbcType.CHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("VARCHAR", JdbcType.VARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("LONGTEXT", JdbcType.LONGNVARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("MEDIUMTEXT", JdbcType.LONGNVARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("NUMERIC", JdbcType.NUMERIC);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("DECIMAL", JdbcType.DECIMAL);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("INT", JdbcType.INTEGER);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("SMALLINT", JdbcType.SMALLINT);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("BIGINT", JdbcType.BIGINT);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("REAL", JdbcType.REAL);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("FLOAT", JdbcType.FLOAT);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("DOUBLE", JdbcType.DOUBLE);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("DATE", JdbcType.DATE);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("TIME", JdbcType.TIME);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("TIMESTAMP", JdbcType.TIMESTAMP);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("BOOLEAN", JdbcType.BOOLEAN);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("BLOB", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("MEDIUMBLOB", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("LONGBLOB", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("TINYBLOB", JdbcType.BLOB);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("JSON", JdbcType.VARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("ENUM", JdbcType.VARCHAR);
        DB_COLUMN_TYPE_MAP_MYBATIS_JDBC_TYPE.put("SET", JdbcType.VARCHAR);
    }

    @NotNull
    public static String getDbColumnTypeByField(@NotNull Field field) {
        String type = field.getType().getName();
        String mysqlType = JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.get(type);
        if (mysqlType == null) {
            throw new IllegalArgumentException("Unsupported field: " + field);
        }
        return mysqlType;
    }

    public static String getDbColumnTypeByJavaTypeName(String javaTypeName) {
        String mysqlType = JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.get(javaTypeName);
        if (StrUtil.isBlank(mysqlType)) {
            throw new IllegalArgumentException("Unsupported javaTypeName: " + javaTypeName);
        }
        return mysqlType;
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
