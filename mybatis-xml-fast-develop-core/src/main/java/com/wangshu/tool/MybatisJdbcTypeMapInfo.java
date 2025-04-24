package com.wangshu.tool;

import org.apache.ibatis.type.JdbcType;

import java.util.HashMap;
import java.util.Map;

public class MybatisJdbcTypeMapInfo {

    private static final Map<String, JdbcType> JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE = new HashMap<>();

    static {
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(String.class.getTypeName(), JdbcType.NVARCHAR);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Integer.class.getTypeName(), JdbcType.INTEGER);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(int.class.getTypeName(), JdbcType.INTEGER);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Long.class.getTypeName(), JdbcType.BIGINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(long.class.getTypeName(), JdbcType.BIGINT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Float.class.getTypeName(), JdbcType.REAL);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(float.class.getTypeName(), JdbcType.REAL);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Double.class.getTypeName(), JdbcType.DOUBLE);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(double.class.getTypeName(), JdbcType.DOUBLE);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(Boolean.class.getTypeName(), JdbcType.BIT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(boolean.class.getTypeName(), JdbcType.BIT);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.sql.Date.class.getTypeName(), JdbcType.DATE);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.sql.Time.class.getTypeName(), JdbcType.TIME);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.sql.Timestamp.class.getTypeName(), JdbcType.TIMESTAMP);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.util.Date.class.getTypeName(), JdbcType.TIMESTAMP);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(byte[].class.getTypeName(), JdbcType.VARBINARY);
        JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.put(java.math.BigDecimal.class.getTypeName(), JdbcType.DECIMAL);
    }

    public static JdbcType getJdbcTypeForJavaTypeName(String javaTypeName) {
        return JAVA_TYPE_NAME_MAP_MYBATIS_JDBC_TYPE.getOrDefault(javaTypeName, JdbcType.OTHER);
    }

}