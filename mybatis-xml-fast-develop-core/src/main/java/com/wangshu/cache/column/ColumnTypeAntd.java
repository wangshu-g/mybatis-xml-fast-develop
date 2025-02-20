package com.wangshu.cache.column;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;

/**
 * @author GSF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ColumnTypeAntd extends ColumnType {

    String dataIndex;
    String key;

    public ColumnTypeAntd(Field field) {
        super(field);
        this.dataIndex = this.name;
        this.key = this.name;
    }

}
