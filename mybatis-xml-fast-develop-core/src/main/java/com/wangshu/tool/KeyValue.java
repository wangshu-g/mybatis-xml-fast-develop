package com.wangshu.tool;

import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Data
public class KeyValue<T, R> {

    private final String key;
    private final Object value;

    public KeyValue(PropertyFunc<T, R> getter, Object value) {
        this.key = CommonTool.getFuncFieldName(getter);
        this.value = value;
    }

    public KeyValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <T, R> KeyValue<T, R> KV(PropertyFunc<T, R> getter, Object value) {
        return new KeyValue<T, R>(getter, value);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static KeyValue KV(String key, Object value) {
        return new KeyValue(key, value);
    }

}