package com.wangshu.table;

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.base.model.BaseModel;
import com.wangshu.tool.MysqlTypeMapInfo;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author GSF
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
@Slf4j
public class GenerateTableMysql extends GenerateTable {

    public GenerateTableMysql(Class<? extends BaseModel> clazz) {
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
            ResultSet tables = databaseMetaData.getTables(database, databaseMetaData.getUserName(), tableName, new String[]{"TABLE"});
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
        ResultSet columnsResult = databaseMetaData.getColumns(database, databaseMetaData.getUserName(), tableName, null);
        Map<String, Field> columnMap = this.getFields().stream().collect(Collectors.toMap(k -> k.getName().toLowerCase(), v -> v));
        while (columnsResult.next()) {
            String column = columnsResult.getString("COLUMN_NAME").toLowerCase();
            Field columnInfo = columnMap.get(column);
            if (Objects.nonNull(columnInfo)) {
                String type = columnsResult.getString("TYPE_NAME").toLowerCase();
                String columnJdbcType = this.getJdbcType(columnInfo).toLowerCase();
                int columnLength = this.getDefaultLength(columnInfo);
                if (!StrUtil.equals(type, columnJdbcType.toLowerCase())) {
                    log.warn("修改列: {}", columnInfo.getName());
                    String sql = this.generateAlterColumn(tableName, columnInfo.getName().toLowerCase(), columnJdbcType, columnLength);
                    log.warn("执行sql: {}", sql);
                    Statement statement = connection.createStatement();
                    try {
                        statement.execute(sql);
                    } catch (SQLException e) {
                        log.error("修改列: {},失败", columnInfo.getName());
                        log.error("异常: ", e);
                    }
                }
                columnMap.remove(column);
            }
        }
        for (Field value : columnMap.values()) {
            log.warn("添加列: {}", value.getName());
            String sql = this.generateAddColumn(tableName, value.getName(), this.getJdbcType(value).toLowerCase(), this.getDefaultLength(value));
            log.warn("执行sql: {}", sql);
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }
        log.info("");
    }

    public String generateCreateTable(String tableName) {
        String sql = StrUtil.concat(false, "create table `", tableName, "` ( ");
        for (int index = 0; index < this.getFields().size(); index++) {
            Field item = this.getFields().get(index);
            String columnName = StrUtil.concat(false, "`", item.getName(), "`");
            int length = this.getDefaultLength(item);
            String columnType = StrUtil.concat(false, this.getJdbcType(item).toLowerCase(), length == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(length), ")"));
            boolean defaultNullFlag = this.isDefaultNull(item);
            String columnNull = defaultNullFlag ? "null" : "not null";
            boolean primaryKeyFlag = this.isPrimaryKey(item);
            String columnAutoIncrement = (primaryKeyFlag && (item.getType().equals(Long.class) || item.getType().equals(Integer.class))) ? "auto_increment" : "";
            String columnComment = StrUtil.concat(false, "comment '", this.getComment(item), "'");
            String columnPrimary = this.isPrimaryKey(item) ? "primary key" : "";
            String columnEnd = index == this.getFields().size() - 1 ? "" : ",";
            sql = StrUtil.concat(false, sql,
                    columnName, " ",
                    columnType, " ",
                    columnNull, " ",
                    columnAutoIncrement, " ",
                    columnComment, " ",
                    columnPrimary, " ",
                    columnEnd
            );
        }
        sql = StrUtil.concat(false, sql, " ) collate = utf8mb4_bin;");
        return sql;
    }

    public String generateAddColumn(String tableName, String columnName, String columnJdbcType, int columnLength) {
        return StrUtil.concat(false,
                "alter table `", tableName,
                "` add `", columnName, "` ",
                columnJdbcType, columnLength == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(columnLength), ")")
        );
    }

    public String generateAlterColumn(String tableName, String columnName, String columnJdbcType, int columnLength) {
        return StrUtil.concat(false,
                "alter table `", tableName,
                "` modify `", columnName, "` ",
                columnJdbcType, columnLength == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(columnLength), ")")
        );
    }

    @Override
    public String getJdbcType(@NotNull Field field) {
        String jdbcType = null;
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            jdbcType = column.jdbcType();
        }
        if (StrUtil.isBlank(jdbcType)) {
            jdbcType = MysqlTypeMapInfo.getDbColumnTypeByField(field);
        }
        return jdbcType;
    }

    @Override
    public int getDefaultLength(@NotNull Field field) {
        int length = field.getAnnotation(Column.class).length();
        if (length == -1) {
            length = MysqlTypeMapInfo.getDbColumnTypeDefaultLengthByMybatisJdbcType(this.getJdbcType(field).toUpperCase());
        }
        return length;
    }

}