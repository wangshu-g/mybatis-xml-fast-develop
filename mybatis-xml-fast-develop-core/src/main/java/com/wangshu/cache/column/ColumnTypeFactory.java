package com.wangshu.cache.column;

import com.wangshu.enu.ColumnType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author GSF
 */
@Slf4j
public class ColumnTypeFactory {

    private ColumnTypeFactory() {
    }

    private static ColumnTypeFactory factory = new ColumnTypeFactory();

    private static Map<ColumnType, Class<ColumnTypeAntd>> map = new HashMap<>();

    static {
        map.put(ColumnType.antd, ColumnTypeAntd.class);
    }

    public ColumnTypeAntd create(ColumnType columnType, Field field) {
        Class<ColumnTypeAntd> clazz = map.get(columnType);
        if (Objects.nonNull(clazz)) {
            try {
                return clazz.getDeclaredConstructor(Field.class).newInstance(field);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static ColumnTypeFactory getInstance() {
        return factory;
    }

}
