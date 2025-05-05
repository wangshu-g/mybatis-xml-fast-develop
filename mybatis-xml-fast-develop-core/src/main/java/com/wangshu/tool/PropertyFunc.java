package com.wangshu.tool;

import java.io.Serializable;

@FunctionalInterface
public interface PropertyFunc<T, R> extends Serializable {
    R apply(T t);
}