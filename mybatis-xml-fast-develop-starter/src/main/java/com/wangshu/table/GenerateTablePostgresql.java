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
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.SqlStyle;
import com.wangshu.tool.PostgresqlTypeMapInfo;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangshu-g
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
@Slf4j
public class GenerateTablePostgresql extends GenerateTable {

    public GenerateTablePostgresql(Class<? extends BaseModel> clazz) {
        super(clazz);
    }

    @Override
    public void createTable(@NotNull Connection connection) throws SQLException {
        log.info("当前数据源: {} {}", connection.getCatalog(), connection.getMetaData().getURL());
        if (Objects.nonNull(this.getNames()) && !this.getNames().isEmpty()) {
            this.getNames().forEach(item -> this.execute(connection, item));
        } else {
            this.execute(connection, this.getTable());
        }
    }

    public void execute(@NotNull Connection connection, String tableName) {
        boolean flag = false;
        try {
            String database = connection.getCatalog();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = databaseMetaData.getTables(database, "", tableName, new String[]{"TABLE"});
            flag = tables.next();
            if (flag) {
                this.executeAlterTable(connection, tableName);
            } else {
                this.executeCreateTable(connection, tableName);
            }
        } catch (SQLException e) {
            if (flag) {
                log.error(StrUtil.concat(false, "修改表 ", tableName, " 失败"), e);
            } else {
                log.error(StrUtil.concat(false, "创建表 ", tableName, " 失败"), e);
            }
        }
    }

    public void executeCreateTable(@NotNull Connection connection, String tableName) throws SQLException {
        log.info("创建表: {}", tableName);
        String sql = this.generateCreateTable(tableName);
        log.info("执行sql: {}", sql);
        Statement statement = connection.createStatement();
        statement.execute(sql);
        log.info("");
    }

    public void executeAlterTable(@NotNull Connection connection, String tableName) throws SQLException {
        log.info("表格 {} 已存在", tableName);
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String database = connection.getCatalog();
        ResultSet columnsResult = databaseMetaData.getColumns(database, "", tableName, null);
        Map<String, Field> columnMap = this.getFields().stream().collect(Collectors.toMap(this::getSqlStyleName, v -> v));
        while (columnsResult.next()) {
            String column = columnsResult.getString("COLUMN_NAME");
            Field columnInfo = columnMap.get(column);
            if (Objects.nonNull(columnInfo)) {
                String type = columnsResult.getString("TYPE_NAME");
                String columnJdbcType = this.getJdbcType(columnInfo);
                int columnLength = this.getDefaultLength(columnInfo);
                if (!StrUtil.equals(type.toLowerCase(), columnJdbcType.toLowerCase())) {
                    String columnName = this.getSqlStyleName(columnInfo);
                    log.warn("修改列: {}", columnInfo.getName());
                    String sql = this.generateAlterColumn(tableName, columnName, columnJdbcType, columnLength);
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
            log.warn("添加列: {}", value.getName());
            String sql = this.generateAddColumn(tableName, value.getName(), this.getJdbcType(value), this.getDefaultLength(value));
            log.warn("执行sql: {}", sql);
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }
        log.info("");
    }

    public String generateCreateTable(String tableName) {
        String sql = StrUtil.concat(false, "create table \"", tableName, "\" ( ");
        for (int index = 0; index < this.getFields().size(); index++) {
            Field item = this.getFields().get(index);
            String columnName = StrUtil.concat(false, "\"", this.getSqlStyleName(item), "\"");
            int length = this.getDefaultLength(item);
            String columnType = StrUtil.concat(false, this.getJdbcType(item), length == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(length), ")"));
            boolean defaultNullFlag = this.isDefaultNull(item);
            String columnNull = defaultNullFlag ? "null" : "not null";
            boolean primaryKeyFlag = this.isPrimaryKey(item);

            if (primaryKeyFlag) {
                if (item.getType().equals(Long.class)) {
                    columnType = "bigserial";
                } else if (item.getType().equals(Integer.class)) {
                    columnType = "serial";
                }
            }

            String columnComment = StrUtil.concat(false, "comment '", this.getComment(item), "'");
            String columnPrimary = this.isPrimaryKey(item) ? "primary key" : "";
            String columnEnd = index == this.getFields().size() - 1 ? "" : ",";
            sql = StrUtil.concat(false, sql,
                    columnName, " ",
                    columnType, " ",
                    columnNull, " ",
//                    columnComment, " ",
                    columnPrimary, " ",
                    columnEnd);
        }
        sql = StrUtil.concat(false, sql, " );");
        return sql;
    }

    public String getSqlStyleName(Field field) {
        return Objects.equals(this.getSqlStyle(), SqlStyle.lcc) ? StrUtil.lowerFirst(field.getName()) : StrUtil.toUnderlineCase(field.getName());
    }

    public String generateAddColumn(String tableName, String columnName, String columnJdbcType, int columnLength) {
        return StrUtil.concat(false, "alter table \"", tableName, "\" add column \"", columnName, "\" ", columnJdbcType,
                columnLength == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(columnLength), ")"));
    }

    public String generateAlterColumn(String tableName, String columnName, String columnJdbcType, int columnLength) {
        return StrUtil.concat(false, "alter table \"", tableName, "\" alter column \"", columnName, "\" set data type ",
                columnJdbcType, columnLength == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(columnLength), ")"));
    }

    @Override
    public String getJdbcType(@NotNull Field field) {
        String jdbcType = null;
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            jdbcType = column.jdbcType();
        }
        if (StrUtil.isBlank(jdbcType)) {
            jdbcType = PostgresqlTypeMapInfo.getDbColumnTypeByField(field);
        }
        return jdbcType;
    }

    @Override
    public int getDefaultLength(@NotNull Field field) {
        return PostgresqlTypeMapInfo.getDbColumnTypeDefaultLengthByMybatisJdbcType(this.getJdbcType(field).toUpperCase());
    }

}
