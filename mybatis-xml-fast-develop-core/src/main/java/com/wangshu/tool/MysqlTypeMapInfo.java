package com.wangshu.tool;

import org.apache.ibatis.type.JdbcType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
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
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Boolean.class.getName(), "VARCHAR");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Character.class.getName(), "CHAR");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(BigDecimal.class.getName(), "DECIMAL");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(String.class.getName(), "VARCHAR");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Date.class.getName(), "TIMESTAMP");
        JAVA_TYPE_NAME_MAP_DB_COLUMN_TYPE.put(Byte[].class.getName(), "BLOB");
    }

    static {
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("INTEGER", Integer.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("INT", Integer.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DECIMAL", BigDecimal.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BIGINT", Long.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("SMALLINT", Short.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("FLOAT", Float.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DOUBLE", Double.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("CHAR", Character.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("VARCHAR", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("MEDIUMTEXT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("LONGTEXT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TEXT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("ENUM", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BIT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TINYINT", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("JSON", String.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("TIMESTAMP", Date.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DATETIME", Date.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("DATE", Date.class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("MEDIUMBLOB", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("BLOB", Byte[].class.getName());
        DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.put("VARBINARY", Byte[].class.getName());
    }

    static {
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TINYINT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("SMALLINT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("MEDIUMINT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("INTEGER", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BIGINT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("FLOAT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("DOUBLE", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("REAL", -1);
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
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("TEXT", 6553);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("MEDIUMTEXT", 6553);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("LONGTEXT", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("ENUM", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("SET", -1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("BOOLEAN", 1);
        DB_COLUMN_TYPE_MAP_DEFAULT_LENGTH.put("SERIAL", 4);
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
        if (StringUtil.isEmpty(mysqlType)) {
            throw new IllegalArgumentException("Unsupported javaTypeName: " + javaTypeName);
        }
        return mysqlType;
    }

    public static String getJavaTypeNameByDbColumnType(@NotNull String dbColumnType) {
        String javaType = DB_COLUMN_TYPE_MAP_JAVA_TYPE_NAME.get(dbColumnType.toUpperCase());
        if (StringUtil.isEmpty(javaType)) {
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
