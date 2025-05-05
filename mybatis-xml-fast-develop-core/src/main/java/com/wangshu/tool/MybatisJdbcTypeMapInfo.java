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

import org.apache.ibatis.type.JdbcType;

import java.util.HashMap;
import java.util.Map;

public class MybatisJdbcTypeMapInfo {

    private static final Map<String, JdbcType> JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE = new HashMap<>();

    static {
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(String.class.getTypeName(), JdbcType.VARCHAR);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Integer.class.getTypeName(), JdbcType.INTEGER);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(int.class.getTypeName(), JdbcType.INTEGER);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Long.class.getTypeName(), JdbcType.BIGINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(long.class.getTypeName(), JdbcType.BIGINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Float.class.getTypeName(), JdbcType.REAL);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(float.class.getTypeName(), JdbcType.REAL);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Double.class.getTypeName(), JdbcType.DOUBLE);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(short.class.getTypeName(), JdbcType.SMALLINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Short.class.getTypeName(), JdbcType.SMALLINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.math.BigDecimal.class.getTypeName(), JdbcType.DECIMAL);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(double.class.getTypeName(), JdbcType.DOUBLE);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Boolean.class.getTypeName(), JdbcType.BIT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(boolean.class.getTypeName(), JdbcType.BIT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.sql.Date.class.getTypeName(), JdbcType.DATE);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.sql.Time.class.getTypeName(), JdbcType.TIME);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.sql.Timestamp.class.getTypeName(), JdbcType.TIMESTAMP);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.util.Date.class.getTypeName(), JdbcType.TIMESTAMP);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(char.class.getTypeName(), JdbcType.CHAR);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Character.class.getTypeName(), JdbcType.CHAR);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(byte.class.getTypeName(), JdbcType.TINYINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(byte[].class.getTypeName(), JdbcType.VARBINARY);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Byte.class.getTypeName(), JdbcType.TINYINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Byte[].class.getTypeName(), JdbcType.VARBINARY);
    }

    public static JdbcType getJdbcTypeForJavaTypeName(String javaTypeName) {
        return JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.getOrDefault(javaTypeName, JdbcType.OTHER);
    }

}