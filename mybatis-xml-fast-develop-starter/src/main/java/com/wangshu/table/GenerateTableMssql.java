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
import com.wangshu.base.model.BaseModel;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * @author wangshu-g
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class GenerateTableMssql extends GenerateTable {

    public GenerateTableMssql(Class<? extends BaseModel> clazz) {
        super(clazz);
    }

    @Override
    public boolean columnIsModify(@NotNull String currentColumnTypeName, @NotNull String columnJdbcType) {
        return !StrUtil.equals(currentColumnTypeName.toLowerCase(), columnJdbcType.toLowerCase());
    }

    @Override
    public String getCreateTableSql(@NotNull String tableName) {
        String sql = StrUtil.concat(false, "create table [", tableName, "] ( ");
        for (int index = 0; index < this.getFields().size(); index++) {
            Field item = this.getFields().get(index);
            String columnName = StrUtil.concat(false, "[", this.getSqlStyleName(item), "]");
            int length = this.getDefaultLength(item);
            String columnType = StrUtil.concat(false, this.getDbColumnType(item), length == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(length), ")"));
            boolean defaultNullFlag = this.isDefaultNull(item);
            String columnNull = defaultNullFlag ? "null" : "not null";
//            boolean primaryKeyFlag = this.isPrimaryKey(item);
            String columnAutoIncrement = this.isAutoIncrement(item) ? "IDENTITY" : "";
            String columnComment = StrUtil.concat(false, "comment '", this.getComment(item), "'");
            String columnPrimary = this.isPrimaryKey(item) ? "primary key" : "";
            String columnEnd = index == this.getFields().size() - 1 ? "" : ",";
            sql = StrUtil.concat(false, sql,
                    columnName, " ",
                    columnType, " ",
                    columnNull, " ",
                    columnAutoIncrement, " ",
//                    columnComment, " ",
                    columnPrimary, " ",
                    columnEnd
            );
        }
        sql = StrUtil.concat(false, sql, " );");
        return sql;
    }

    @Override
    public String getAlterColumnSql(String tableName, String columnName, String columnJdbcType, int columnLength) {
        return StrUtil.concat(false, "alter table [",
                tableName,
                "] alter column [",
                columnName,
                "] ",
                columnJdbcType,
                columnLength == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(columnLength), ")")
        );
    }

    @Override
    public String getAddColumnSql(String tableName, String columnName, String columnJdbcType, int columnLength) {
        return StrUtil.concat(false, "alter table [",
                tableName,
                "] add [",
                columnName,
                "] ",
                columnJdbcType,
                columnLength == -1 ? "" : StrUtil.concat(false, "(", String.valueOf(columnLength), ")")
        );
    }

}
