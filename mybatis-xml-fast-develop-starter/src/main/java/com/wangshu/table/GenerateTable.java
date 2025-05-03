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
import com.wangshu.base.model.BaseModelWithDefaultFields;
import com.wangshu.tool.CommonTool;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangshu-g
 *
 * <p>注意：这里除了 postgresql，目前其他支持的数据库，注意数值相关列要自行指定小数点后长度</p>
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
@Slf4j
public abstract class GenerateTable extends ModelInfo {

    public GenerateTable() {
    }

    public GenerateTable(Class<?> clazz) {
        super(clazz);
    }

    public Type getGenericPrimaryType(@NotNull Field field) {
        if (BaseModelWithDefaultFields.class.isAssignableFrom(this.getMetadata()) && StrUtil.equals(field.getName(), "id") && this.getMetadata().getGenericSuperclass() instanceof ParameterizedType temp) {
            return Arrays.stream(temp.getActualTypeArguments()).toList().getFirst();
        } else {
            throw new IllegalArgumentException("Unsupported field: " + field);
        }
    }

    public String getDbColumnType(@NotNull Field field) {
        String dbColumnType = null;
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            dbColumnType = column.dbColumnType();
        }
        if (StrUtil.isBlank(dbColumnType)) {
            if (field.getType().equals(Object.class)) {
                dbColumnType = CommonTool.getDbColumnTypeByJavaTypeName(this.getDataBaseType(), this.getGenericPrimaryType(field).getTypeName());
            } else {
                dbColumnType = CommonTool.getDbColumnTypeByField(this.getDataBaseType(), field);
            }
        }
        return dbColumnType;
    }

    public int getDefaultLength(@NotNull Field field) {
        return CommonTool.getDefaultLengthByDbColumnType(this.getDataBaseType(), this.getDbColumnType(field).toUpperCase());
    }

    public boolean isAutoIncrement(@NotNull Field field) {
        if (!this.isPrimaryKey(field)) {
            return false;
        }
        if (field.getType().equals(Object.class)) {
            String typeName = this.getGenericPrimaryType(field).getTypeName();
            return StrUtil.equals(typeName, Long.class.getTypeName()) || StrUtil.equals(typeName, Integer.class.getTypeName());
        } else {
            return Objects.equals(field.getType(), Long.class);
        }
    }

    public boolean isDefaultNull(@NotNull Field field) {
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

    public String getSqlStyleName(@NotNull Field field) {
        return CommonTool.getNewStrBySqlStyle(this.getSqlStyle(), field.getName());
    }

    public ResultSet getTablesResultSetFromDatabaseMetaData(@NotNull Connection connection, String tableName) throws SQLException {
        String catalog = connection.getCatalog();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        return databaseMetaData.getTables(null, null, tableName, new String[]{"TABLE"});
    }

    public void execute(@NotNull Connection connection) throws SQLException {
        log.info("当前数据源: {} {}", connection.getCatalog(), connection.getMetaData().getURL());
        List<String> names = this.getNames();
        if (Objects.isNull(names) || names.isEmpty()) {
            names = List.of(this.getTableName());
        }
        for (String tableName : names) {
            boolean flag = false;
            try {
                ResultSet tables = getTablesResultSetFromDatabaseMetaData(connection, tableName);
                flag = tables.next();
                if (flag) {
                    this.alterTable(connection, tableName);
                } else {
                    this.createTable(connection, tableName);
                }
            } catch (SQLException e) {
                if (flag) {
                    log.error(StrUtil.concat(false, "修改表 ", tableName, " 失败"), e);
                } else {
                    log.error(StrUtil.concat(false, "创建表 ", tableName, " 失败"), e);
                }
            }
        }
    }

    public void createTable(@NotNull Connection connection, @NotNull String tableName) throws SQLException {
        log.info("创建表: {}", tableName);
        String sql = this.getCreateTableSql(tableName);
        log.info("执行sql: {}", sql);
        Statement statement = connection.createStatement();
        statement.execute(sql);
        log.info("");
    }

    public ResultSet getColumnsResultSetFromDatabaseMetaData(@NotNull Connection connection, String tableName) throws SQLException {
        String catalog = connection.getCatalog();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        return databaseMetaData.getColumns(null, null, tableName, null);
    }

    public void alterTable(@NotNull Connection connection, @NotNull String tableName) throws SQLException {
        log.info("表 {} 已存在", tableName);
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String catalog = connection.getCatalog();
        ResultSet columnsResult = getColumnsResultSetFromDatabaseMetaData(connection, tableName);
        Map<String, Field> columnMap = this.getFields().stream().collect(Collectors.toMap(this::getSqlStyleName, v -> v));
        while (columnsResult.next()) {
            String column = columnsResult.getString("COLUMN_NAME");
            Field columnInfo = columnMap.get(column);
            if (Objects.nonNull(columnInfo)) {
                String currentColumnTypeName = columnsResult.getString("TYPE_NAME");
                String columnJdbcType = this.getDbColumnType(columnInfo);
                if (this.columnIsModify(currentColumnTypeName, columnJdbcType)) {
                    String columnName = this.getSqlStyleName(columnInfo);
                    int columnLength = this.getDefaultLength(columnInfo);
                    log.warn("修改列: {}", columnInfo.getName());
                    String sql = this.getAlterColumnSql(tableName, columnName, columnJdbcType, columnLength);
                    log.warn("执行sql: {}", sql);
                    Statement statement = connection.createStatement();
                    try {
                        statement.execute(sql);
                    } catch (SQLException e) {
                        log.error("修改列: {},失败", columnName);
                        log.error("异常: ", e);
                    }
                }
                columnMap.remove(column);
            }
        }
        for (Field value : columnMap.values()) {
            String columnName = this.getSqlStyleName(value);
            log.warn("添加列: {}", columnName);
            String sql = this.getAddColumnSql(tableName, columnName, this.getDbColumnType(value), this.getDefaultLength(value));
            log.warn("执行sql: {}", sql);
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }
        log.info("");
    }

    public abstract boolean columnIsModify(@NotNull String currentColumnTypeName, @NotNull String columnJdbcType);

    public abstract String getCreateTableSql(@NotNull String tableName);

    public abstract String getAlterColumnSql(String tableName, String columnName, String columnJdbcType, int columnLength);

    public abstract String getAddColumnSql(String tableName, String columnName, String columnJdbcType, int columnLength);

}
